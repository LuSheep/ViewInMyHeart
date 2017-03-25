package com.scu.lly.customviews.view.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.scu.lly.customviews.R;
import com.scu.lly.customviews.utils.DiskLruCache;
import com.scu.lly.customviews.utils.ImageUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图片加载框架
 * Created by lusheep on 2016/8/9.
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//磁盘缓存50M
    private static int IO_BUFFER_SIZE = 8 * 1024;
    private static int DISK_CACHE_INDEX = 0;//磁盘缓存每个Entry只需要保存一个值就ok
    private boolean mIsDiskLruCacheCreated = false;
    private Context mContext;
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;

    /**
     * 下面是线程池的配置
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;//核心线程数
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;//最大线程数
    private static final long KEEP_ALIVE = 10L;

    //构造一个线程工厂
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);

    /**
     * 下面是Handler的构造(这个handler绑定的是主线程的Looper，因此ImageLoader可以在子线程中创建)
     */
    private static final int TAG_KEY_URL = R.id.imageloader_id;//在Android4.0以后，setTage可以类似key,value的形式
    private static final int MESSAGE_POST_RESULT = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageHolder holder = (ImageHolder) msg.obj;
            ImageView imageView = holder.imageView;
            String url = (String) imageView.getTag(TAG_KEY_URL);
            if(url.equals(holder.url)){
                imageView.setImageBitmap(holder.bitmap);
            }else{
                Log.w(TAG,"ImageView url changed！");
            }
        }
    };

    private ImageLoader(Context context){
        mContext = context.getApplicationContext();//通过使用ApplicationContext，可以防止内存泄露
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);//获得每个应用分配的最大可用内存(单位KB)
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if(!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }
        if(getUserableSpace(diskCacheDir) > DISK_CACHE_SIZE){
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拿到一个ImageLoader的实例
     * @param context
     * @return
     */
    public static ImageLoader build(Context context){
        return new ImageLoader(context);
    }

    /**
     * 异步方式，从内存缓存或磁盘缓存或网络获取bitmap，并将bitmap绑定到ImageView上(不压缩图片)
     * @param url
     * @param imageView
     */
    public void bindBitmap(final String url, final ImageView imageView){
        bindBitmap(url, imageView, 0 ,0);
    }

    /**
     * 异步方式，从内存缓存或磁盘缓存或网络获取bitmap，并将bitmap绑定到ImageView上(按要求压缩图片)
     * @param url
     * @param imageView
     * @param desiredWidth
     * @param desiredHeight
     */
    public void bindBitmap(final String url, final ImageView imageView, final int desiredWidth, final int desiredHeight) {
        imageView.setTag(TAG_KEY_URL, url);
        //先尝试从内存缓存中获取
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        //内存缓存中没有获取到，则尝试从磁盘缓存或者网络获取，因为磁盘或者网络中获取都是耗时操作，需要在子线程中操作
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, desiredWidth, desiredHeight);
                if(bitmap != null){
                    ImageHolder holder = new ImageHolder(imageView, url, bitmap);
                    mHandler.obtainMessage(MESSAGE_POST_RESULT, holder).sendToTarget();
                }
            }
        };

        //放入到线程池中运行
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 同步操作，调用这个方法，外部需要在自己写的子线程中运行。
     * 从内存缓存或磁盘缓存或网络获取bitmap
     * @param url
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public Bitmap loadBitmap(String url, int desiredWidth, int desiredHeight){
        //先尝试从内存缓存中获取
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if(bitmap != null){
            return bitmap;
        }

        try {
            bitmap = loadBitmapFromDiskCache(url, desiredWidth, desiredHeight);
            //从磁盘文件缓存中获取到了缓存
            if(bitmap != null){
                Log.d(TAG, "loadBitmapFromDisk,url:" + url);
                return bitmap;
            }

            //现在只有从网络获取了
            bitmap = loadBitmapFromHttp(url, desiredWidth, desiredHeight);
            Log.d(TAG, "loadBitmapFromHttp,url:" + url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bitmap == null && !mIsDiskLruCacheCreated){
            Log.d(TAG, "encounter error,DiskLruCache is not created.");
            bitmap = downloadBitmapFromUrl(url);
        }

        return bitmap;
    }

    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(conn.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Error in downladBitmap:" + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error in downladBitmap:" + e);
        }finally{
            if(conn != null){
                conn.disconnect();
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromHttp(String url, int desiredWidth, int desiredHeight) throws IOException {
        if(Looper.myLooper() == Looper.getMainLooper()){
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if(mDiskLruCache == null){
            return null;
        }
        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if(editor != null){
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if(downloadUrlToStream(url, outputStream)){
                editor.commit();
            }else{
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url, desiredWidth, desiredHeight);
    }

    /**
     *
     * @param urlString
     * @param outputStream
     * @return
     */
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection conn = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(conn.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while((b = in.read()) != -1){
                out.write(b);
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.disconnect();
            }
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private Bitmap loadBitmapFromDiskCache(String url, int desiredWidth, int desiredHeight) throws IOException {
        if(Looper.myLooper() == Looper.getMainLooper()){
            Log.w(TAG,"load bitmap from UI Thread, it's not recommended");
        }
        if(mDiskLruCache == null){
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if(snapshot != null){
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = ImageUtils.getBitmapFromFileDescriptor(fileDescriptor, desiredWidth, desiredHeight);
            //从文件缓存中获取到缓存后，将其加入到内存缓存中
            if(bitmap != null){
                addBitmapToMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if(getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从内存缓存中获取缓存内容
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromMemCache(String url) {
        String key = hashKeyFromUrl(url);
        Bitmap bitmap = getBitmapFromMemCache(key);
        return bitmap;
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKey = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
            e.printStackTrace();
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length; i++){
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if(hex.length() == 1){
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 获取文件路径下的可用容量大小
     * @param path
     * @return
     */
    private long getUserableSpace(File path){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long)stats.getBlockSize() * (long)stats.getAvailableBlocks();
    }

    /**
     * 获取磁盘缓存目录
     * @param mContext
     * @param name
     * @return
     */
    private File getDiskCacheDir(Context mContext, String name) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if(externalStorageAvailable){
            cachePath = mContext.getExternalCacheDir().getPath();
        }else{
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + name);
    }

    private static class ImageHolder{
        public ImageView imageView;
        public String url;
        public Bitmap bitmap;

        public ImageHolder(ImageView iv, String url, Bitmap bm){
            this.imageView = iv;
            this.url = url;
            this.bitmap = bm;
        }
    }
}
