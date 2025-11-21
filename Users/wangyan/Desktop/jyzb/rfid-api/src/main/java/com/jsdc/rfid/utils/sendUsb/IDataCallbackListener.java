package com.jsdc.rfid.utils.sendUsb;

public interface IDataCallbackListener {

    /**
     * 用于接收android端发送来的数据
     *
     * @param data 数据
     */
    public void onDataReceived(String data);
}
