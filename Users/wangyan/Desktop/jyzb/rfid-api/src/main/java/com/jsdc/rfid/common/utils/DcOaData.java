package com.jsdc.rfid.common.utils;

public class DcOaData {
    private static DcOaData mInstance=null;

    private String tokenValue;

    private DcOaData() {
    }



    public static DcOaData Instance(){
        if (null==mInstance){
            mInstance=new DcOaData();
        }
        return mInstance;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
