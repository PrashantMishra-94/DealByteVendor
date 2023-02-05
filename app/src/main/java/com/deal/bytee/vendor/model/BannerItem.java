package com.deal.bytee.vendor.model;

public class BannerItem {

    private String mBimg;
    private String mId;

    public BannerItem(String mBimg, String mId) {
        this.mBimg = mBimg;
        this.mId = mId;
    }

    public String getBimg() {
        return mBimg;
    }

    public void setBimg(String bimg) {
        mBimg = bimg;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

}