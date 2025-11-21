package com.jsdc.rfid.utils.sendUsb;

import java.io.DataInputStream;
import java.io.IOException;

public class ReceiveMsgThread extends Thread {

    private IDataCallbackListener dataCallbackListener;

    private DataInputStream dataInputStream;

    // 是否接收消息
    private boolean isMsgReceive = false;


    public ReceiveMsgThread(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public boolean isMsgReceive() {
        return isMsgReceive;
    }

    public void setMsgReceive(boolean isMsgReceive) {
        this.isMsgReceive = isMsgReceive;
    }

    public IDataCallbackListener getDataCallbackListener() {
        return dataCallbackListener;
    }

    public void setDataCallbackListener(IDataCallbackListener dataCallbackListener) {
        this.dataCallbackListener = dataCallbackListener;
    }

    public void run() {
        byte[] dataBuffer = new byte[512];
        while (isMsgReceive) {
            try {
                if (dataInputStream != null) {
                    synchronized (dataInputStream) {
                        int len = dataInputStream.read(dataBuffer);
                        if (len > 0) {
                            String strData = new String(dataBuffer, 0, len, "UTF-8");
                            if (strData != null && strData.length() > 0) {
                                if (dataCallbackListener != null) {
                                    dataCallbackListener.onDataReceived(strData);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        isMsgReceive = false;
        dataCallbackListener = null;
        try {
            if (dataInputStream != null) {
                synchronized (dataInputStream) {
                    dataInputStream.close();
                    dataInputStream = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
