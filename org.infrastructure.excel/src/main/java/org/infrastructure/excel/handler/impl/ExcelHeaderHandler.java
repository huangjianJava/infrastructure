package org.infrastructure.excel.handler.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.infrastructure.excel.bean.ExcelBean;
import org.infrastructure.excel.bean.ExcelhandlerBean;
import org.infrastructure.excel.handler.ExcelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 生成Excel表头
 *
 * @author liuzw
 */
public class ExcelHeaderHandler implements ExcelHandler {

    private static Logger logger = LoggerFactory.getLogger(ExcelHeaderHandler.class);

    private Map<String, CellStyle> fieldFormatMap = new HashMap<>(20);

    private List<String> fieldSumList = new ArrayList<>();

    @Override
    public void handler(ExcelhandlerBean excelhandlerBean) {
        logger.info("--------生成excel表头----------");
        // 产生工作薄对象
        HSSFWorkbook workbook = excelhandlerBean.getWorkbook();
        // 产生工作表对象
        HSSFSheet sheet = excelhandlerBean.getSheet();
        sheet.setForceFormulaRecalculation(true);
        // 设置表头信息
        setExcelHeader(workbook, sheet, excelhandlerBean.getObjects());

        excelhandlerBean.setFieldFormatMap(fieldFormatMap);
        excelhandlerBean.setFieldSumList(fieldSumList);

        excelhandlerBean.getExcelhandlerChain().handler(excelhandlerBean);
    }

    /**
     * 设置表头信息
     */
    private void setExcelHeader(HSSFWorkbook workbook, HSSFSheet sheet, Object[] title) {

        // 设置工作表的名称.
        ////workbook.setSheetName(0, sheetName);

        //设置字体
        HSSFFont font = workbook.createFont();
        //设置样式
        CellStyle cellStyle = workbook.createCellStyle();
        // 此处设置数据格式
        HSSFDataFormat df = workbook.createDataFormat();
        // 产生单元格
        HSSFCell cell;

        for (int i = 0; i < title.length; i++) {
            // 产生一行
            HSSFRow row = sheet.createRow(i);
            @SuppressWarnings("unchecked")
            List<ExcelBean> list = (List<ExcelBean>) title[i];
            for (int j = 0; j < list.size(); j++) {

                ExcelBean excelBean = list.get(j);
                //设置单元格宽度
                sheet.setColumnWidth(j, excelBean.getWidth() * 256);
                if (Boolean.TRUE.equals(excelBean.getIsMerge())) {
                    sheet.addMergedRegion(new CellRangeAddress(excelBean.getFirstRow(), excelBean.getLastRow(), excelBean.getFirstCol(), excelBean.getLastCol()));
                }
                cell = row.createCell(excelBean.getFirstCol());
                setStyle(font, cellStyle, df, excelBean);
                //设置单元格样式
                cell.setCellStyle(cellStyle);
                //设置列名
                cell.setCellValue(excelBean.getColumnName());
            }

        }
    }

    /**
     * 设置样式
     */
    private void setStyle(HSSFFont font, CellStyle cellStyle, HSSFDataFormat df, ExcelBean excelBean) {

        cellStyle.setFont(font);
        // 居中
        switch (excelBean.getStyle()) {
            case CENTER:
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                break;
            case RIGHT:
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                break;
            case LEFT:
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                break;
            default:
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                break;
        }
        //设置单元格格式

        if (StringUtils.isNotEmpty(excelBean.getFormat())) {
            cellStyle.setDataFormat(df.getFormat(excelBean.getFormat()));
        }
        fieldFormatMap.put(excelBean.getField(),cellStyle);

        if (Boolean.TRUE.equals(excelBean.getIsSum())) {
            fieldSumList.add(excelBean.getField());
        }
    }
}
