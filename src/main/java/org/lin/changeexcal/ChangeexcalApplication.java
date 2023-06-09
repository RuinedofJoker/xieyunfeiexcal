package org.lin.changeexcal;

import org.lin.changeexcal.pojo.FileReadAndWrite;
import org.lin.changeexcal.service.FileService;
import org.lin.changeexcal.view.AppMain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class ChangeexcalApplication {

    public static void main(String[] args) {
        runInVisualization(args);
        //runInCmd(args);
    }

    public static void runInVisualization(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ChangeexcalApplication.class);
        System.setProperty("java.awt.headless", "false");
        ConfigurableApplicationContext applicationContext = builder.headless(false).run(args);

        applicationContext.getBean(AppMain.class);

    }
    public static void runInCmd(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ChangeexcalApplication.class, args);

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入读取文件路径(输入no采用默认路径):");
        String readPath = scanner.nextLine();
        System.out.println("请输入输出文件路径(输入no采用默认路径):");
        String writePath = scanner.nextLine();

        System.out.println("------------------------任务开始,请不要打开读取目录中的文件-----------------------");

        FileReadAndWrite fileReadAndWrite = applicationContext.getBean(FileReadAndWrite.class);
        fileReadAndWrite.setReadAndWritePath(readPath, writePath);

        FileService fileService = applicationContext.getBean(FileService.class);

        fileService.selectAndGenerateExcalDirs();

        System.out.println("任务完成,请按回车继续...");
        try {
            System.in.read();
        } catch (IOException e) {}
    }

}
