package org.lin.changeexcal.common;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lin.changeexcal.annotation.ExcelField;
import org.lin.changeexcal.annotation.ExcelTable;
import org.lin.changeexcal.pojo.ExcalMetaInfo;
import org.lin.changeexcal.pojo.SheetInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class ExcalFileReader {

    public static final String FILE_TYPE = ".xlsx";

    public ExcalMetaInfo readExcalGenerateObject(String path, Class excalEntity) {

        ExcalMetaInfo readeExcalMetaInfo = new ExcalMetaInfo(path, excalEntity);
        getExcalMetaInfo(readeExcalMetaInfo);

        return readeExcalMetaInfo;
    }

    private void getExcalMetaInfo(ExcalMetaInfo excalMetaInfo) {
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(excalMetaInfo.getFile());
            excalMetaInfo.setSheetInfo(new ArrayList());

            List<String> sheetIndexAndName = getSheetIndexAndName(workbook, excalMetaInfo.getExcalEntityClass());

            for (int i = 0; i < sheetIndexAndName.size(); i++) {
                SheetInfo sheetInfo = new SheetInfo();
                sheetInfo.setSheetIndex(Integer.toString(i));
                sheetInfo.setSheetName(sheetIndexAndName.get(i));
                sheetInfo.setSheet(workbook.getSheet(sheetInfo.getSheetName()));
                sheetInfo.setTitle(getSheetTitle(sheetInfo.getSheet(), excalMetaInfo.getExcalEntityClass()));
                sheetInfo.setEntityList(getEntityList(sheetInfo.getSheet(), excalMetaInfo.getExcalEntityClass()));
                excalMetaInfo.getSheetInfo().add(i, sheetInfo);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private List<String> getSheetIndexAndName(Workbook workbook, Class excalEntity) {
        List<String> sheetIndexAndName = new ArrayList<>();
        if (excalEntity.isAnnotationPresent(ExcelTable.class)) {
            ExcelTable tableAnnotation = (ExcelTable) excalEntity.getAnnotation(ExcelTable.class);
            for (int i = 0; i < tableAnnotation.name().length; i++) {
                sheetIndexAndName.add(workbook.getSheetIndex(tableAnnotation.name()[i]), tableAnnotation.name()[i]);
            }
        }
        return sheetIndexAndName;
    }

    private String getSheetTitle(Sheet sheet, Class excalEntity) {
        if (excalEntity.isAnnotationPresent(ExcelTable.class)) {
            ExcelTable tableAnnotation = (ExcelTable) excalEntity.getAnnotation(ExcelTable.class);
            if (tableAnnotation.title().length == 2) {
                Cell cell = sheet.getRow(0).getCell(0);
                try {
                    return cell.getStringCellValue();
                }catch (Exception e){
                    return "";
                }
            }
        }
        return null;
    }

    private List getEntityList(Sheet sheet, Class excalEntity) {

        List entityList = new ArrayList();
        Field[] excalFields = excalEntity.getDeclaredFields();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            try {
                Object instance = excalEntity.newInstance();
                entityList.add(i, instance);
                for (Field field : excalFields) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(ExcelField.class)) {
                        ExcelField cellAnnotation = field.getAnnotation(ExcelField.class);
                        Cell currentCell = sheet.getRow(i).getCell(CellReference.convertColStringToIndex(cellAnnotation.value()));
                        field.set(entityList.get(i), currentCell);
                    }
                }
            } catch (Exception e) {}
        }

        return entityList;
    }
}
