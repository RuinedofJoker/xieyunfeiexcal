package org.lin.changeexcal;

import org.lin.changeexcal.pojo.FileReadAndWrite;
import org.lin.changeexcal.service.FileService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class ChangeexcalApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ChangeexcalApplication.class, args);

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入读取文件路径:");
        String readPath = scanner.next();
        System.out.println("请输入输出文件路径:");
        String writePath = scanner.next();

        System.out.println("------------------------任务开始,请不要打开读取目录中的文件-----------------------");

        FileReadAndWrite fileReadAndWrite = applicationContext.getBean(FileReadAndWrite.class);
        fileReadAndWrite.setReadAndWritePath(readPath, writePath);

        FileService fileService = applicationContext.getBean(FileService.class);

        fileService.selectAndGenerateExcalDirs();

        System.out.println("任务完成,按任意键继续...");
        scanner.next();
    }

}
