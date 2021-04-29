package com.demo.test.demo.app;

import com.demo.test.demo.entity.VideoEntity;
import com.demo.test.demo.utils.EntityUtil;
import com.demo.test.demo.utils.ExcleUtil;
import com.demo.test.demo.utils.httpclientUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class MyChessFrame extends JFrame implements MouseListener {

    JLabel urlLabel = new JLabel("URL:");
    JTextField urlText = new JTextField(20);
    JLabel rowLabel = new JLabel("查询行数:");
    JTextField rowText = new JTextField(20);
    JLabel JSESSIONIDLabel = new JLabel("JSESSIONID:");
    JTextField JSESSIONIDText = new JTextField(20);
    httpclientUtil hu = new httpclientUtil();
    ExcleUtil excleUtil = new ExcleUtil();
    JLabel resultLabel = new JLabel();

    private static List<VideoEntity> list;

    public static void main(String[] args) {
        MyChessFrame myChessFrame = new MyChessFrame();
    }

    public MyChessFrame() {
        super("我的程序");
        Container con = this.getContentPane();
        con.setLayout(null);
        urlLabel.setBounds(25,20,110,25);
        con.add(urlLabel);
        //文本输入框url
        urlText.setBounds(100,20,550,25);
        urlText.setText("http://182.138.30.132:8080/cimp/jsp/program/programraw_page?program.elementType=Series");
        con.add(urlText);

        JSESSIONIDLabel.setBounds(25,60,100,25);
        con.add(JSESSIONIDLabel);
        //文本输入框JSESSIONID
        JSESSIONIDText.setBounds(100,60,550,25);
        con.add(JSESSIONIDText);

        rowLabel.setBounds(25,100,100,25);
        con.add(rowLabel);
        //文本输入框row
        rowText.setBounds(100,100,550,25);
        con.add(rowText);
        // 创建登录按钮
        JButton createButton = new JButton("开始生成");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createButton();
            }
        });
        createButton.setBounds(250, 140, 100, 25);
        createButton.setVisible(true);
        con.add(createButton);
        //结果显示
        resultLabel.setBounds(25,180,550,25);
        con.add(resultLabel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭按钮
        this.addMouseListener(this);//添加监听
        this.setSize(700, 400);
        this.setVisible(true);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setBounds((width - 700) / 2,
                (height - 400) / 2, 700, 400);
    }

    public void createButton(){
        //开始清空全局变量
        list = new ArrayList<>();
        boolean boo = true;
        int page = 1;
        String row = rowText.getText();
        String JSESSIONID = JSESSIONIDText.getText();
        while (boo){
            String url = urlText.getText() + "&page="+page+"&rows="+row;
            List<VideoEntity> datas = hu.send(url, "UTF-8", JSESSIONID);
            list.addAll(datas);
            page ++;
            if(datas.size() == 0){
                boo = false;
            }
        }
        list = new EntityUtil().turnInChinese(list);
        System.out.println("集合数：----------------"+list.size());
        String fileUrl = excleUtil.exportExcelPaper(list);
        if(!"".equals(fileUrl)){
            resultLabel.setText("文件生成成功！文件路径：" + fileUrl);
        }else{
            resultLabel.setText("文件生成失败！");
        };
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
