package org.infrastructure.excel.handler;


import org.infrastructure.excel.bean.ExcelhandlerBean;

/**
 * Excel导出责任链
 *
 * @author liuzw
 */
public interface ExcelHandler {

    /**
     * 处理EXCEL
     *
     * @param excelhandlerBean 参数封装对象
     */
    void handler(ExcelhandlerBean excelhandlerBean);

}
