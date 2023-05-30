package org.lin.changeexcal.pojo;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class ExcalMetaInfo {
    private Class excalEntityClass;
    private String filePath;
    private String fileName;
    private File file;
    private List<SheetInfo> sheetInfo;

    public ExcalMetaInfo(String path, Class excalEntityClass) {
        this.excalEntityClass = excalEntityClass;
        this.filePath = path;
        this.file = new File(filePath);
        String[] fileSplitArray = file.getName().split("\\.");
        StringBuffer fileNameTemp = new StringBuffer();
        for (int i = 0; i < fileSplitArray.length - 1; i++) {
            fileNameTemp.append(fileSplitArray[i]);
        }
        this.fileName = fileNameTemp.toString();
    }
}
