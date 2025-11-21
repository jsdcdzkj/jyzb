//package com.jsdc.rfid.utils.sendUsb;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class UI extends JFrame implements ActionListener {
//
//    JPanel jpannelConnect = null;
//    JPanel jpannelReceiverTitle = null;
//    JPanel jpannelStop = null;
//    JPanel jpannelReceiverContent = null;  // 用于显示收到的内容
//
//    //定义组件
//    JButton jBtnConnect = null;  // 连接android设备
//    JButton jBtnReceiveData = null;  // 接收数据
//
//    JButton jBtnStopReceive = null;  // 停止接收数据
//
//    JLabel jLbConnectState;  // 连接状态的显示
//    JLabel jLbReceiveState;  // 数据接收状态的显示
//
//    JTextArea jTxtReceiveContent = null;  //接收数据内容的显示
//    private IDataCallbackListener dataCallbackListener = new IDataCallbackListener() {
//
//        @Override
//        public void onDataReceived(String data) {
//            if (jTxtReceiveContent != null && data != null && data.length() > 0) {
//                jTxtReceiveContent.setText(data);
//            }
//        }
//    };
//
//    //构造函数
//    public UI()    //不能申明为void!!!!!否则弹不出新界面
//    {
//
//        jpannelConnect = new JPanel();
//        jpannelReceiverTitle = new JPanel();
//        jpannelReceiverContent = new JPanel();
//        jpannelStop = new JPanel();
//
//        //创建组件
//        jBtnConnect = new JButton("建立连接");
//        jBtnReceiveData = new JButton("接收数据");
//        jBtnStopReceive = new JButton("停止接收数据");
//
//        jLbConnectState = new JLabel("未连接");
//        jLbReceiveState = new JLabel("未接收");
//        jTxtReceiveContent = new JTextArea(10, 20);
//
//        jTxtReceiveContent.setText("这里显示接收到的信息");
//
//        jpannelConnect.add(jBtnConnect, LEFT_ALIGNMENT);
//        jpannelConnect.add(jLbConnectState, LEFT_ALIGNMENT);
//        jpannelStop.add(jBtnStopReceive);
//
//        jpannelReceiverTitle.add(jBtnReceiveData);
//        jpannelReceiverTitle.add(jLbReceiveState);
//        jpannelReceiverContent.add(jTxtReceiveContent);
//
//        this.add(jpannelConnect);
//        this.add(jpannelReceiverTitle);
//        this.add(jpannelReceiverContent);
//        this.add(jpannelStop);
//
//        initListener();
//
//        //设置布局管理器
//        this.setLayout(new GridLayout(3, 3, 50, 50));
//        this.setTitle("PC端和Android端的通信小工具");
//        this.setSize(500, 400);
//        this.setLocation(300, 300);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setVisible(true);
//
//    }
//
//    public static void main(String[] args) {
//    }
//
//    private void initListener() {
//        jBtnConnect.addActionListener(this);
//        jBtnReceiveData.addActionListener(this);
//        jBtnStopReceive.addActionListener(this);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent event) {
//        String command = event.getActionCommand();
//        if (command.equals("建立连接")) {
//            DeviceManager.getInstance().init();
//            boolean isOk = DeviceManager.getInstance().initLoaclSocket(7777, 1234);
//            if (isOk) {
//                jLbConnectState.setText("已连接");
//            }
//        } else if (command.equals("接收数据")) {
//            DeviceManager.getInstance().setDataCallbackListener(dataCallbackListener);
//            DeviceManager.getInstance().receiveData();
//            DeviceManager.getInstance().sendData("start"); // 通知服务端开始发送数据
//            jLbReceiveState.setText("数据接收中...");
//        } else if (command.endsWith("停止接收数据")) {
//            DeviceManager.getInstance().sendData("stop"); // 通知服务端停止发送数据
//            DeviceManager.getInstance().stopReceiveThread();
//            jLbReceiveState.setText("数据接收停止");
//        }
//    }
//
//    public void connect1() {
//        DeviceManager.getInstance().init();
//        boolean isOk = DeviceManager.getInstance().initLoaclSocket(7777, 8888);
//        if (isOk) {
//            jLbConnectState.setText("已连接");
//        }
//    }
//
//    public void receiveData() {
//        DeviceManager.getInstance().setDataCallbackListener(dataCallbackListener);
//        DeviceManager.getInstance().receiveData();
//        DeviceManager.getInstance().sendData("start"); // 通知服务端开始发送数据
//        jLbReceiveState.setText("数据接收中...");
//    }
//
//    public void stopReceiveData() {
//        DeviceManager.getInstance().sendData("stop"); // 通知服务端停止发送数据
//        DeviceManager.getInstance().stopReceiveThread();
//        jLbReceiveState.setText("数据接收停止");
//    }
//}
