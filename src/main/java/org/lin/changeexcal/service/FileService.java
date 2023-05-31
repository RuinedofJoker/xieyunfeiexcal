package org.lin.changeexcal.service;

import org.lin.changeexcal.common.ExcalFileReader;
import org.lin.changeexcal.pojo.FileReadAndWrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileService {

    @Autowired
    FileReadAndWrite fileReadAndWrite;
    @Autowired
    ExcalService excalService;
    @Autowired
    ExcalFileReader excalFileReader;

    public void selectAndGenerateExcalDirs() {
        String readPath = fileReadAndWrite.getReadAndWritePath().get("readPath");
        File readFile = new File(readPath);
        if (readFile.exists()) {
            generateExcal(readPath);
        }else {
            System.out.println("文件不存在!");
        }
    }

    public void generateExcal(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            File[] files = file.listFiles();
            fileReadAndWrite.addCurrentPath("\\" + file.getName());

            for (File cur : files) {
                generateExcal(cur.getPath());
            }

            fileReadAndWrite.subCurrentPath();
        }else {
            try {
                File excalDir = new File(fileReadAndWrite.getReadAndWritePath().get("writePath") + fileReadAndWrite.getCurrentPath());
                excalDir.mkdirs();

                excalService.writeExcalFileByCondition(fileReadAndWrite.getReadAndWritePath().get("writePath") + fileReadAndWrite.getCurrentPath(),
                        excalService.readExcalFile(path));
            }catch (Exception e) {}
        }
    }
}
