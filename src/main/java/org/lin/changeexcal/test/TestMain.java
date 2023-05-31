package org.lin.changeexcal.test;

import org.lin.changeexcal.common.ExcalFileReader;
import org.lin.changeexcal.service.ExcalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class TestMain {

    @Value("${file.default.writer.path}")
    public String defaultWriterPath;

    @Value("${file.default.reader.path}")
    public String readPath;

    @Autowired
    ExcalService excalService;
    @Autowired
    ExcalFileReader excalFileReader;
    public void testReader() {

        File tempDir = new File("C:\\temp");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }

        excalService.writeExcalFileByCondition(defaultWriterPath, excalService.readExcalFile(readPath + "\\茂县2023年5月份慈善捐赠审批表.xlsx"));

    }
}
