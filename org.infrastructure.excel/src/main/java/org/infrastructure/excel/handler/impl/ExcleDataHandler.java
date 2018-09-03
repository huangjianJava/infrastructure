package org.infrastructure.excel.handler.impl;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.infrastructure.excel.bean.ExcelhandlerBean;
import org.infrastructure.excel.handler.ExcelHandler;
import org.infrastructure.excel.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 处理Excel数据
 *
 * @author liuzw
 */
public class ExcleDataHandler implements ExcelHandler {

    private static Logger logger = LoggerFactory.getLogger(ExcleDataHandler.class);

    @Override
    public void handler(ExcelhandlerBean excelhandlerBean) {
        logger.info("-------------生成excel列表数据----------");
        setExcelData(excelhandlerBean);
        excelhandlerBean.getExcelhandlerChain().handler(excelhandlerBean);
    }

    /**
     * 设置excel表格数据
     */
    private void setExcelData(ExcelhandlerBean excelhandlerBean) {
        HSSFRow row;
        // 产生单元格
        HSSFCell cell;
        CellStyle cellStyle;
        Integer rowIndex = excelhandlerBean.getMax();
        Map<String, CellStyle> filedFormatMap = excelhandlerBean.getFieldFormatMap();
        List<String> fieldSumList = excelhandlerBean.getFieldSumList();
        Map<String,  Map<String, Object>> indexMap = new HashMap<>(16);
        Map<String,  Object> map;
        //此处设置数据格式
        List<?> dataList = excelhandlerBean.getDataList();
        if (dataList != null && dataList.size() > 0) {
            Iterator<?> it = dataList.iterator();
            while (it.hasNext()) {
                row = excelhandlerBean.getSheet().createRow(rowIndex);
                Integer celIndex = 0;
                Object t = it.next();
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                List<String> fieldList = excelhandlerBean.getFieldList();
                String getMethodName;
                for (String field : fieldList) {
                    if (field != null && !"".equals(field)) {
                        getMethodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                        try {
                            Method getMethod = t.getClass().getMethod(getMethodName);
                            Object value = getMethod.invoke(t);
                            cell = row.createCell(celIndex);
                            cellStyle = filedFormatMap.get(field);
                            if (cellStyle != null) {
                                cell.setCellStyle(cellStyle);
                            }
                            if (value != null) {
                                if (value instanceof String) {
                                    cell.setCellValue(String.valueOf(value));
                                } else {
                                    if (fieldSumList.contains(field)) {
                                        map = new HashMap<>(4);
                                        map.put("celIndex", celIndex);
                                        map.put("cellStyle", cellStyle);
                                        indexMap.put(field, map);
                                    }
                                    cell.setCellValue(Double.parseDouble(String.valueOf(value)));
                                }
                            } else {
                                cell.setCellValue("");
                            }
                            celIndex++;
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                rowIndex++;
            }
            Boolean flag = excelhandlerBean.getIsShowTotal();
            if (flag) {
                //合计
                row = excelhandlerBean.getSheet().createRow(rowIndex + 2);
                cell = row.createCell(0);
                cellStyle = excelhandlerBean.getWorkbook().createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(cellStyle);
                cell.setCellValue("合计");
                StringBuilder sb;
                for (Map<String,  Object> m : indexMap.values()) {
                    Integer index = (Integer) m.get("celIndex");
                    cell = row.createCell(index);
                    sb = new StringBuilder("SUM(");
                    String s = ExcelUtil.getIndexLabel(index);
                    sb.append(s).append(excelhandlerBean.getMax() + 1).append(":").append(s).append(rowIndex).append(")");
                    cell.setCellStyle((CellStyle) m.get("cellStyle"));
                    cell.setCellFormula(sb.toString());
                    System.out.println("-------formula-------" + sb.toString());
                }
            }
        }
    }
}
