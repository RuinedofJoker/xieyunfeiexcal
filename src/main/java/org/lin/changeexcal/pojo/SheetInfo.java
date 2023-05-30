package org.lin.changeexcal.pojo;

import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

@Data
public class SheetInfo {
    private String title;
    private String sheetIndex;
    private String sheetName;
    private Sheet sheet;
    private List entityList;
}
