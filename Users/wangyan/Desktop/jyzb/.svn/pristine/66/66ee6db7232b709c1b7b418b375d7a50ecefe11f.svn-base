//package com.jsdc.rfid.utils.sendUsb;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class Login extends JFrame {
//    public final static String NAME = "111";
//    public final static String PWD = "111";
//    //定义组件
//    JPanel jp1, jp2, jp3;//面板
//    JLabel jlUserName;//标签,用户名
//    JLabel jlPwd;//标签,密码
//    JButton jbLogin;  // 登录按钮
//    JButton jbRest;  // 重置按钮
//    JTextField jtUserName;//文本
//    JPasswordField jpwd;//密码
//
//    //构造函数
//    public Login() {
//        //创建面板
//        jp1 = new JPanel();
//        jp2 = new JPanel();
//        jp3 = new JPanel();
//        //创建标签
//
//        jlUserName = new JLabel("用户名");
//        jlPwd = new JLabel("密    码");
//
//        //创建按钮
//        jbLogin = new JButton("登录");
//        jbRest = new JButton("重置");
//        //创建文本框
//        jtUserName = new JTextField(10);
//        //创建密码框
//        jpwd = new JPasswordField(10);
//
//        //设置布局管理
//        this.setLayout(new GridLayout(3, 1));//网格式布局
//
//        //加入各个组件
//        jp1.add(jlUserName);
//        jp1.add(jtUserName);
//
//        jp2.add(jlPwd);
//        jp2.add(jpwd);
//
//        jp3.add(jbLogin);
//        jp3.add(jbRest);
//
//        //加入到JFrame
//        this.add(jp1);
//        this.add(jp2);
//        this.add(jp3);
//
//        //设置窗体
//        this.setTitle("用户登录");//窗体标签
//        this.setSize(300, 150);//窗体大小
//        this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭JFrame
//        this.setVisible(true);//显示窗体
//
//        this.jbLogin.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                String command = actionEvent.getActionCommand();
//                if (command != null && "登录".endsWith(command)) {
//                    String userName = jtUserName.getText();
//                    String userPwd = jpwd.getText();
//                    String errorMsg = "";
//
//                    if (userName == null || "".equals(userName)) {
//                        errorMsg = "用户名不能为空!" + "\r\n";
//                    }
//                    if (userName == null || "".equals(userName)) {
//                        errorMsg += "密码不能为空!";
//                    }
//                    if (errorMsg != null && !"".equals(errorMsg)) {
//                        JOptionPane.showMessageDialog(null, errorMsg, "提示消息", JOptionPane.WARNING_MESSAGE);
//                        return;
//                    }
//
//                    if (NAME.equals(userName) && PWD.equals(userPwd)) {
//                        JOptionPane.showMessageDialog(null, "登录成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
//                        dispose();
//                        UI ui = new UI();
//                    } else {
//                        JOptionPane.showMessageDialog(null, "用户名或密码不正确！", "提示消息", JOptionPane.WARNING_MESSAGE);
//                    }
//                }
//            }
//        });
//        this.jbRest.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                String command = actionEvent.getActionCommand();
//                System.out.println("jbRest,command = " + command);
//                if ("重置".equals(command)) {
//                    clear();
//                }
//            }
//        });
//
//
//        //锁定窗体
//        this.setResizable(false);
//    }
//
//    public static void main(String[] args) {
//        new Login();
//    }
//
//    /**
//     *
//     */
//    private void clear() {
//        jtUserName.setText("");
//        jpwd.setText("");
//    }
//
//}
