package com.jsdc.rfid.utils.sendUsb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class DeviceManager {

    private static DeviceManager deviceManager = null;

    /**
     * 和android设备之间的连接
     */
    private Socket mSocket;

    /**
     * 输入流，接收来自android设备的数据
     */
    private DataInputStream inputStream;

    /**
     * 输出流，向android设备发出数据
     */
    private DataOutputStream outputStream;

    /**
     * 接收数据的线程
     */
    private ReceiveMsgThread receiveMsgThread = null;

    /**
     * 发送数据的线程
     */
    private SendMsgThread sendMsgThread = null;

    /**
     * 用于接收android端返回的数据
     */
    private IDataCallbackListener dataCallbackListener;

    private DeviceManager() {
    }

    public static DeviceManager getInstance() {
        if (deviceManager == null) {
            synchronized (DeviceManager.class) {
                if (deviceManager == null) {
                    deviceManager = new DeviceManager();
                }
            }
        }
        return deviceManager;
    }


    public IDataCallbackListener getDataCallbackListener() {
        return dataCallbackListener;
    }

    public void setDataCallbackListener(IDataCallbackListener dataCallbackListener) {
        this.dataCallbackListener = dataCallbackListener;
    }


    public void init() {
        stopSendThread();
        stopReceiveThread();
        releaseSocket();
    }

    /**
     * 初始化本地设备
     *
     * @param localPort   本地socket端口
     * @param androidPort android设备端socketserver端口
     * @return true 成功 ，false 失败
     */
    public boolean initLoaclSocket(int localPort, int androidPort) {
        boolean isOk = false;
        try {
            Runtime.getRuntime().exec("adb forward tcp:" + localPort + " tcp:" + androidPort);
            isOk = true;
        } catch (IOException e) {
            e.printStackTrace();
            isOk = false;
        }
        if (isOk) {
            isOk = connectAndroidDevice(localPort);
        }
        return isOk;
    }

    /**
     * 连接Android设备 ， adb forward ,端口转发
     */
    private boolean connectAndroidDevice(int port) {
        boolean isOk = false;
        String ip;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("ip = " + ip + ", port = " + port);
            ip = "127.0.0.1";
            mSocket = new Socket(ip, port);
            inputStream = new DataInputStream(mSocket.getInputStream());
            outputStream = new DataOutputStream(mSocket.getOutputStream());
            isOk = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            isOk = false;
        } catch (IOException e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }

    /**
     * 通过输入流接收android设备传来的数据
     */
    public void receiveData() {
        if (receiveMsgThread == null) {
            receiveMsgThread = new ReceiveMsgThread(inputStream);
            receiveMsgThread.setMsgReceive(true);
            receiveMsgThread.setDataCallbackListener(new IDataCallbackListener() {
                @Override
                public void onDataReceived(String data) {
//                    System.out.println("receiveData(),data = " + data);
                    if (dataCallbackListener != null) {
                        dataCallbackListener.onDataReceived(data);
                    }
                }
            });
        }
        receiveMsgThread.start();
    }

    /**
     * 发送数据
     *
     * @param msg
     */
    public void sendData(String msg) {
        if (sendMsgThread == null) {
            sendMsgThread = new SendMsgThread(outputStream);
            sendMsgThread.start();
        }
        sendMsgThread.sendMsg(msg);
    }

    /**
     * 停止接收数据的线程
     */
    public void stopReceiveThread() {
        if (receiveMsgThread != null) {
            receiveMsgThread.release();
            receiveMsgThread = null;
        }
    }

    /**
     * 停止发送数据的线程
     */
    public void stopSendThread() {
        if (sendMsgThread != null) {
            sendMsgThread.release();
            sendMsgThread = null;
        }
        try {
            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void releaseSocket() {
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
             }

        } catch (IOException e) {
            System.out.println("releaseSocket(),IOException :" + e.getMessage());
        }

    }
}
