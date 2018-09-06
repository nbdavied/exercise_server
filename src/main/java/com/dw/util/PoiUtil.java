package com.dw.util;

import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PoiUtil {
    public static String getString(Cell cell){
        if(cell == null){
            return "";
        }
        int type = cell.getCellType();
        switch(type){
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_NUMERIC:
                DecimalFormat format = new DecimalFormat("####################.#########");
                double value =  cell.getNumericCellValue();
                return format.format(value);
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue()?"true":"false";
            case Cell.CELL_TYPE_ERROR:
                return "error";
            default:
                return "";
        }
    }
    public static BigDecimal getDecimal(Cell cell){
        if(cell == null){
            return null;
        }
        int type = cell.getCellType();
        switch(type){
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_NUMERIC:
                double value = cell.getNumericCellValue();
                return new BigDecimal(Double.toString(value));
            case Cell.CELL_TYPE_STRING:
                try {
                    BigDecimal d = new BigDecimal(cell.getStringCellValue());
                    return d;
                } catch (Exception e) {
                    throw new RuntimeException("数据格式不正确");
                }
            default:
                throw new RuntimeException("数据格式不正确");
        }
    }
    public static Integer getInteger(Cell cell){
        if(cell == null){
            return null;
        }
        int type = cell.getCellType();
        switch(type){
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_NUMERIC:
                int value = (int)cell.getNumericCellValue();
                return value;
            case Cell.CELL_TYPE_STRING:
                try {
                    int d = Integer.parseInt(cell.getStringCellValue());
                    return d;
                } catch (Exception e) {
                    throw new RuntimeException("数据格式不正确");
                }
            default:
                throw new RuntimeException("数据格式不正确");
        }
    }

}
