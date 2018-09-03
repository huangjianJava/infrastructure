package org.infrastructure.excel.handler;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.infrastructure.excel.bean.ExcelhandlerBean;
import org.infrastructure.excel.handler.impl.*;

import java.util.List;

/**
 * @author liuzw
 */
public class ExcelCreateFactory {

    public static void create(String excelName, List<?> list, Class<?> clazz) {

        //ExcelhandlerChain链条
        ExcelhandlerChain excelhandlerChain = new ExcelhandlerChain();
        //采用的是链式调用
        excelhandlerChain.next(new HeaderHandler())
                .next(new ExcelHeaderHandler())
                .next(new ExcleDataHandler())
                .next(new ExcelCreateHandler());

        HSSFWorkbook workbook = new HSSFWorkbook();

        ExcelhandlerBean excelhandlerBean = ExcelhandlerBean.builder()
                .workbook(workbook).sheet(workbook.createSheet()).clazz(clazz)
                .dataList(list).excelName(excelName)
                .excelhandlerChain(excelhandlerChain).build();
        //链式调用
        excelhandlerChain.handler(excelhandlerBean);
    }
}
