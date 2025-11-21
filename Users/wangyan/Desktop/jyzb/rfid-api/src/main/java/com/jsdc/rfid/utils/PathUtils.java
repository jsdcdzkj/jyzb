package com.jsdc.rfid.utils;


/**
 * 存放小程序跳转地址
 */
enum PathUtils {

    //固定资产
    SIGN_MEETING("pages/SignIn/qiandao/index");


    //名称
    private String name;

    PathUtils(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }}
