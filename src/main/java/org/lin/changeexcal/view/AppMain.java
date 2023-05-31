package org.lin.changeexcal.view;

import org.lin.changeexcal.common.AwareContent;
import org.lin.changeexcal.pojo.FileReadAndWrite;
import org.lin.changeexcal.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

@Component
@Lazy
public class AppMain extends JFrame {

    @Autowired
    AwareContent awareContent;

    public AppMain() throws HeadlessException {

        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        JLabel readLabel = new JLabel("请输入读取文件路径:");
        JLabel writeLabel = new JLabel("请输入输出文件路径:");
        JTextField readPathArea = new JTextField(50);
        JTextField writePathArea = new JTextField(50);
        JButton readButton = new JButton("浏览");
        JButton writeButton = new JButton("浏览");
        JButton submit = new JButton("提交");

        readButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.showDialog(new JLabel(), "选择");
            File file = chooser.getSelectedFile();
            readPathArea.setText(file.getAbsoluteFile().toString());
        });

        writeButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.showDialog(new JLabel(), "选择");
            File file = chooser.getSelectedFile();
            writePathArea.setText(file.getAbsoluteFile().toString());
        });

        submit.addActionListener(e -> {

            submit.setEnabled(false);

            System.out.println("------------------------任务开始,请不要打开读取目录中的文件------------------------");
            ConfigurableApplicationContext applicationContext = awareContent.getApplicationContext();

            FileReadAndWrite fileReadAndWrite = applicationContext.getBean(FileReadAndWrite.class);
            fileReadAndWrite.setReadAndWritePath(readPathArea.getText(), writePathArea.getText());

            FileService fileService = applicationContext.getBean(FileService.class);

            fileService.selectAndGenerateExcalDirs();

            System.out.println("------------------------任务完成------------------------");

            System.exit(0);
        });

        add(readLabel);
        add(readPathArea);
        add(readButton);

        add(writeLabel);
        add(writePathArea);
        add(writeButton);

        add(submit);

        setBounds(700, 300, 800, 500);//设置位置与大小
        setVisible(true);//设置可见
        setBackground(new Color(77,77,77));
        setResizable(false);//设置不可改变大小
        setLayout(layout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
