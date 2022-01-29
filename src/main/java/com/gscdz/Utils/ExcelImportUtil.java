package com.gscdz.Utils;

import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;
import com.xuxueli.poi.excel.util.FieldReflectionUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hu
 * @date 2021-09-20 11:50
 * @description:
 */
public class ExcelImportUtil extends com.xuxueli.poi.excel.ExcelImportUtil {

    public static List<Object> importExcel(Class<?> sheetClass, Workbook workbook) {
        try {
            ExcelSheet excelSheet = (ExcelSheet) sheetClass.getAnnotation(ExcelSheet.class);
            String sheetName = excelSheet != null && excelSheet.name() != null && excelSheet.name().trim().length() > 0 ? excelSheet.name().trim() : sheetClass.getSimpleName();
            List<Field> fields = new ArrayList();
            int rowIndex;
            if (sheetClass.getDeclaredFields() != null && sheetClass.getDeclaredFields().length > 0) {
                Field[] var5 = sheetClass.getDeclaredFields();
                int var6 = var5.length;

                for (rowIndex = 0; rowIndex < var6; ++rowIndex) {
                    Field field = var5[rowIndex];
                    if (!Modifier.isStatic(field.getModifiers())) {
                        fields.add(field);
                    }
                }
            }

            if (fields != null && fields.size() != 0) {
                Sheet sheet = workbook.getSheet(sheetName);
                Iterator<Row> sheetIterator = sheet.rowIterator();
                rowIndex = 0;

                ArrayList dataList;

                ArrayList headList = new ArrayList();
                for1:
                for (dataList = new ArrayList(); sheetIterator.hasNext(); ++rowIndex) {

                    Row rowX = (Row) sheetIterator.next();
                    if (rowIndex == 0) {
                        Iterator<Cell> cellIterator = rowX.cellIterator();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            //cell.setCellType(CellType.STRING);
                            String stringCellValue = cell.getStringCellValue();
                            if (!StringUtils.isEmpty(stringCellValue)) {
                                headList.add(stringCellValue);
                            }
                        }
                    }
                    if (rowIndex > 0) {
                        Object rowObj = sheetClass.newInstance();
                        for2:
                        for (int i = 0; i < fields.size(); ++i) {
                            Field field = (Field) fields.get(i);
                            ExcelField excelField = field.getAnnotation(ExcelField.class);
                            if (excelField == null) {
                                continue;
                            }
                            String name = excelField.name().trim();
                            if (StringUtils.isEmpty(name)) {
                                continue;
                            }
                            int index = headList.indexOf(name);
                            if (index < 0) {
                                continue;
                            }
                            Cell cell = rowX.getCell(index);
                            if (cell != null) {

                                try {
                                    cell.setCellType(CellType.STRING);
                                    String fieldValueStr = cell.getStringCellValue();
                                    if (i == 0 && StringUtils.isEmpty(fieldValueStr)) {
                                        break for1;
                                    }//当前行第一列为空跳出循环for1
                                    Object fieldValue = FieldReflectionUtil.parseValue(field, fieldValueStr);
                                    field.setAccessible(true);
                                    field.set(rowObj, fieldValue);
                                } catch (Exception ex) {
                                    field.setAccessible(true);
                                    field.set(rowObj, null);
                                }
                            } else {
                                if (i == 0) {
                                    break for1;
                                }//当前行第一列为空跳出循环for1
                            }
                        }

                        dataList.add(rowObj);
                    }
                }

                return dataList;
            } else {
                throw new RuntimeException(">>>>>>>>>>> xxl-excel error, data field can not be empty.");
            }
        } catch (Exception var15) {
            throw new RuntimeException(var15);
        }
    }

    public static List<Object> importExcel(Class<?> sheetClass, InputStream inputStream) {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            List<Object> dataList = importExcel(sheetClass, workbook);
            return dataList;
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        } catch (InvalidFormatException var5) {
            throw new RuntimeException(var5);
        }
    }
}
