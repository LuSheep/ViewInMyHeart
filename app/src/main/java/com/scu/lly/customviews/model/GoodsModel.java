package com.scu.lly.customviews.model;

/**
 * Created by lusheep on 2016/7/4.
 */
public class GoodsModel {

    private String goodsName;

    private int goodsPic;

    public GoodsModel(String goodsName, int goodsPic) {
        this.goodsName = goodsName;
        this.goodsPic = goodsPic;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(int goodsPic) {
        this.goodsPic = goodsPic;
    }
}
