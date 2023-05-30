package org.lin.changeexcal.test;

import org.apache.poi.ss.usermodel.Cell;
import org.lin.changeexcal.pojo.ExcalMetaInfo;
import org.lin.changeexcal.pojo.ExcalReadEntity;
import org.lin.changeexcal.common.ExcalFileReader;
import org.lin.changeexcal.pojo.SheetInfo;
import org.lin.changeexcal.service.ExcalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestMain {

    @Value("${file.default.writer.path}")
    public String defaultWriterPath;

    public static final String READ_PATH = "C:\\Users\\61640\\Downloads\\四川省\\阿坝\\茂县2023年5月份慈善捐赠审批表.xlsx";

    @Autowired
    ExcalService excalService;
    @Autowired
    ExcalFileReader excalFileReader;
    public void testReader() {

        ExcalMetaInfo excalMetaInfo = excalService.readExcalFile(READ_PATH);

        excalService.writeExcalFileByCondition(defaultWriterPath, excalMetaInfo);

    }
}
