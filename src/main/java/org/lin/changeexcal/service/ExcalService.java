package org.lin.changeexcal.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lin.changeexcal.annotation.ExcelField;
import org.lin.changeexcal.common.ExcalFileReader;
import org.lin.changeexcal.pojo.ExcalMetaInfo;
import org.lin.changeexcal.pojo.ExcalReadEntity;
import org.lin.changeexcal.pojo.SheetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcalService {

    @Autowired
    ExcalFileReader excalFileReader;

    static List<SimpleDateFormat> dateFormatList = new ArrayList<>(Arrays.asList(
            new SimpleDateFormat("yyyy.MM.dd"),
            new SimpleDateFormat("yyyyMMdd")
    ));

    public ExcalMetaInfo readExcalFile(String readPath) {
        return excalFileReader.readExcalGenerateObject(readPath, ExcalReadEntity.class);
    }

    public void writeExcalFileByCondition(String writePath, ExcalMetaInfo readMetaInfo) {

        List<SheetInfo> sheetsInfo = readMetaInfo.getSheetInfo();
        Workbook workbook = new XSSFWorkbook();

        List passEntityList = new ArrayList();
        List noPassEntityList = new ArrayList();

        SheetInfo passSheetInfo = sheetsInfo.get(0);
        Sheet passSheet = workbook.createSheet(passSheetInfo.getSheetName());

        List passReadEntityList = passSheetInfo.getEntityList();
        int passSheetInfoLen = 0;
        int noPassSheetInfoLen = 0;

        for (int i = passSheetInfo.getTitle() == null ? 1 : 2; i < passReadEntityList.size(); i++) {
            ExcalReadEntity readEntity = (ExcalReadEntity) passReadEntityList.get(i);
            Date currentCellDate = null;
            try {
                currentCellDate = readEntity.getBirthday().getDateCellValue();
            }catch (Exception e) {
                String currentCellDateString = readEntity.getBirthday().getStringCellValue();
                for (SimpleDateFormat dateFormat : dateFormatList) {
                    try {
                        currentCellDate = dateFormat.parse(currentCellDateString);
                    } catch (ParseException ex) {}
                    if (currentCellDate != null) {
                        break;
                    }
                }
            }
            readEntity.getBirthday().setCellValue(currentCellDate);

            Calendar startTimeCalendar = Calendar.getInstance();
            Calendar endTimeCalendar = Calendar.getInstance();
            Calendar currentTimeCalendar = Calendar.getInstance();
            currentTimeCalendar.setTime(currentCellDate);

            startTimeCalendar.add(Calendar.YEAR, -3);
            endTimeCalendar.add(Calendar.MONTH, -6);

            if (!(currentCellDate.before(endTimeCalendar.getTime()) && currentCellDate.after(startTimeCalendar.getTime()))) {
                readEntity.getApprovalResult().setCellValue("未通过");
                noPassEntityList.add(readEntity);
                noPassSheetInfoLen++;
                try {
                    readEntity.getSerialNumber().setCellValue(noPassSheetInfoLen);
                }catch (Exception e){}
            }else {
                passEntityList.add(readEntity);
                passSheetInfoLen++;
                try {
                    readEntity.getSerialNumber().setCellValue(passSheetInfoLen);
                }catch (Exception e){}
            }
        }

        SheetInfo noPassSheetInfo = sheetsInfo.get(1);
        List noPassReadEntityList = noPassSheetInfo.getEntityList();
        Sheet noPassSheet = workbook.createSheet(noPassSheetInfo.getSheetName());

        for (int i = noPassSheetInfo.getTitle() == null ? 1 : 2; i < noPassReadEntityList.size(); i++) {
            ExcalReadEntity readEntity = (ExcalReadEntity) noPassReadEntityList.get(i);
            noPassEntityList.add(readEntity);
            noPassSheetInfoLen++;
            try {
                readEntity.getSerialNumber().setCellValue(noPassSheetInfoLen);
            }catch (Exception e) {}
        }

        Calendar currentCalendar = Calendar.getInstance();
        String[] regexSplit = passSheetInfo.getTitle().split("(?:([\\d]{4}|[\\d]{2})年)?(?:(?:[\\t\\s]?)([\\d]{1,2})月)?(?:(?:[\\t\\s]?)([\\d]{1,2})日)?");

        for (int i = 0; i < regexSplit.length; i++) {
            if ("".equals(regexSplit[i])) {
                regexSplit[i] = currentCalendar.get(Calendar.YEAR) + "年" + (currentCalendar.get(Calendar.MONTH) + 1) + "月";
            }
        }
        StringBuffer title = new StringBuffer();
        Arrays.stream(regexSplit).forEach(item -> title.append(item));

        //通过名单标题
        Iterator<Cell> cellIterator = readMetaInfo.getSheetInfo().get(0).getSheet().getRow(0).cellIterator();
        {
            Row row = passSheet.createRow(0);
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = row.createCell(i);
                Cell next = cellIterator.next();
                cell.setCellValue(i == 0 ? title.toString() : next.getStringCellValue());
                cell.getCellStyle().cloneStyleFrom(next.getCellStyle());
                i++;
            }
        }

        //未通过名单标题
        cellIterator = readMetaInfo.getSheetInfo().get(1).getSheet().getRow(0).cellIterator();
        {
            Row row = noPassSheet.createRow(0);
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = row.createCell(i);
                Cell next = cellIterator.next();
                cell.setCellValue(i == 0 ? title.toString() : next.getStringCellValue());
                cell.getCellStyle().cloneStyleFrom(next.getCellStyle());
                i++;
            }
        }

        //通过名单属性
        cellIterator = readMetaInfo.getSheetInfo().get(0).getSheet().getRow(1).cellIterator();
        {
            Row row = passSheet.createRow(1);
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = row.createCell(i);
                Cell next = cellIterator.next();
                cell.setCellValue(i == 1 ? "市" : next.getStringCellValue());
                cell.getCellStyle().cloneStyleFrom(next.getCellStyle());
                i++;
            }
        }

        //未通过名单属性
        cellIterator = readMetaInfo.getSheetInfo().get(1).getSheet().getRow(1).cellIterator();
        {
            Row row = noPassSheet.createRow(1);
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = row.createCell(i);
                Cell next = cellIterator.next();
                cell.setCellValue(i == 1 ? "市" : next.getStringCellValue());
                cell.getCellStyle().cloneStyleFrom(next.getCellStyle());
                i++;
            }
        }

        //通过名单内容
        for (int i = 0; i < passEntityList.size(); i++) {
            Row row = passSheet.createRow(i + 2);
            copyContentByObject(row, (ExcalReadEntity) passEntityList.get(i));
        }

        //未通过名单内容
        for (int i = 0; i < noPassEntityList.size(); i++) {
            Row row = noPassSheet.createRow(i + 2);
            copyContentByObject(row, (ExcalReadEntity) noPassEntityList.get(i));
        }

        File writeFile = new File(writePath + "\\" + title + ExcalFileReader.FILE_TYPE);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(writeFile);
        } catch (FileNotFoundException e) {}
        try {
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    static final Map<String, Integer> valueIndex = new HashMap<String, Integer>(){{
        put("A", 0);
        put("B", 1);
        put("C", 2);
        put("D", 3);
        put("E", 4);
        put("F", 5);
        put("G", 6);
        put("H", 7);
        put("I", 8);
        put("J", 9);
        put("K", 10);
        put("L", 11);
        put("M", 12);
        put("N", 13);
    }};
    private void copyContentByObject(Row row, ExcalReadEntity entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            ExcelField annotation = field.getAnnotation(ExcelField.class);
            try {
                Cell cell = (Cell) field.get(entity);
                CellType cellType = cell.getCellType();
                if (cellType != null) {
                    if (cellType.toString().equals("STRING")) {
                        row.createCell(valueIndex.get(annotation.value())).setCellValue(cell.getStringCellValue());
                    }else if (cellType.toString().equals("NUMERIC")) {
                        if (DateUtil.isCellDateFormatted(cell)) {
                            row.createCell(valueIndex.get(annotation.value())).setCellValue(cell.getDateCellValue());
                        }else {
                            row.createCell(valueIndex.get(annotation.value())).setCellValue(cell.getNumericCellValue());
                        }
                    }else {
                        row.createCell(valueIndex.get(annotation.value())).setCellValue("");
                    }
                }else {
                    row.createCell(valueIndex.get(annotation.value())).setCellValue("");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        });

    }
}
