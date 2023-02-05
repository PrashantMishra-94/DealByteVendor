package com.deal.bytee.vendor.model;

public class SoldVoucherModel {

    String  cv_id,cv_voucher_id,cv_discount,cv_status,cv_code,cv_price,cv_business_id;

    int discountamount,cv_amount_applied;

    public String getCv_id() {
        return cv_id;
    }

    public void setCv_id(String cv_id) {
        this.cv_id = cv_id;
    }

    public String getCv_voucher_id() {
        return cv_voucher_id;
    }

    public void setCv_voucher_id(String cv_voucher_id) {
        this.cv_voucher_id = cv_voucher_id;
    }

    public String getCv_discount() {
        return cv_discount;
    }

    public void setCv_discount(String cv_discount) {
        this.cv_discount = cv_discount;
    }

    public String getCv_status() {
        return cv_status;
    }

    public void setCv_status(String cv_status) {
        this.cv_status = cv_status;
    }

    public String getCv_code() {
        return cv_code;
    }

    public void setCv_code(String cv_code) {
        this.cv_code = cv_code;
    }

    public String getCv_price() {
        return cv_price;
    }

    public void setCv_price(String cv_price) {
        this.cv_price = cv_price;
    }

    public String getCv_business_id() {
        return cv_business_id;
    }

    public void setCv_business_id(String cv_business_id) {
        this.cv_business_id = cv_business_id;
    }

    public int getDiscountamount() {
        return discountamount;
    }

    public void setDiscountamount(int discountamount) {
        this.discountamount = discountamount;
    }

    public int getCv_amount_applied() {
        return cv_amount_applied;
    }

    public void setCv_amount_applied(int cv_amount_applied) {
        this.cv_amount_applied = cv_amount_applied;
    }
}
