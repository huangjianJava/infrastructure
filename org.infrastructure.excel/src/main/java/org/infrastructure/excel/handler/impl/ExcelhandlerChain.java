package org.infrastructure.excel.handler.impl;

import org.infrastructure.excel.bean.ExcelhandlerBean;
import org.infrastructure.excel.handler.ExcelHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * excle责任链条
 *
 * @author liuzw
 */
public class ExcelhandlerChain implements ExcelHandler {

    private List<ExcelHandler> handlerList = new ArrayList<>();

    private Integer index = 0;

    @Override
    public void handler(ExcelhandlerBean excelhandlerBean) {
        if (index == handlerList.size()) {
            return;
        }
        ExcelHandler excelHandler = handlerList.get(index);
        index++;
        excelHandler.handler(excelhandlerBean);

    }

    public ExcelhandlerChain next(ExcelHandler excelHandler) {
        handlerList.add(excelHandler);
        return this;
    }

}
