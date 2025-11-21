package com.jsdc.rfid.utils.sendUsb;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class SendMsgThread extends Thread {

    private LinkedBlockingQueue<String> blockingQueue;

    private DataOutputStream dataOutputStream;

    private boolean isStart = false;

    public SendMsgThread(DataOutputStream dataOutputStream) {
        blockingQueue = new LinkedBlockingQueue<String>();
        this.dataOutputStream = dataOutputStream;
    }

    public void sendMsg(String msg) {
        try {
            blockingQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String command;
        isStart = true;
        while (isStart) {
            try {
                command = blockingQueue.take();  // 没有读到消息时，这里会阻塞
                if (command != null && command.length() > 0) {
                    if (dataOutputStream != null) {
                        synchronized (dataOutputStream) {
                            dataOutputStream.write(command.getBytes("UTF-8"));
                            dataOutputStream.flush();
//                            System.out.println("command = " + command);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void release() {
        isStart = false;
        try {
            if (dataOutputStream != null) {
                synchronized (dataOutputStream) {
                    dataOutputStream.close();
                    dataOutputStream = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        blockingQueue.clear();
        blockingQueue = null;
    }
}
