package org.lin.changeexcal.service;

import org.lin.changeexcal.common.ExcalFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileService {

    @Value("${file.default.writer.path}")
    public String defaultWriterPath;

    @Autowired
    ExcalService excalService;
    @Autowired
    ExcalFileReader excalFileReader;

    public void selectAndGenerateExcalDirs(String readPath) {
        File readFile = new File(readPath);
        if (readFile.exists()) {
            generateExcal(readPath);
        }
    }

    public void generateExcal(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            File[] files = file.listFiles();
            for (File cur : files) {
                generateExcal(file.getPath());
            }
        }else {

        }
    }
}
