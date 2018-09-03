package org.infrastructure.excel.handler.impl;

import org.apache.commons.collections4.MapUtils;
import org.infrastructure.excel.annotation.Excel;
import org.infrastructure.excel.bean.ExcelBean;
import org.infrastructure.excel.bean.ExcelhandlerBean;
import org.infrastructure.excel.handler.ExcelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 拼装excel表头 ExcelBean
 *
 * @author liuzw
 */
public class HeaderHandler implements ExcelHandler {

    private static Logger logger = LoggerFactory.getLogger(HeaderHandler.class);

    /**
     * 存放表头信息
     */
    private Map<String, Object[]> dataMap = new HashMap<>(10);

    private List<String> columnList = new ArrayList<>(20);

    private Map<String, Integer> headerMap = new HashMap<>(20);

    private List<String> fieldList = new ArrayList<>(20);

    private Integer max = 0;

    /**
     * 是否显示合计
     */
    private Boolean isShowTotal = false;

    @Override
    public void handler(ExcelhandlerBean excelhandlerBean) {
        logger.info("--------开始组装excel表头信息----------");
        excelhandlerBean.setObjects(getExcleHeader(excelhandlerBean.getClazz()));
        excelhandlerBean.setMax(max);
        excelhandlerBean.setFieldList(fieldList);
        excelhandlerBean.setIsShowTotal(isShowTotal);

        if (excelhandlerBean.getObjects() != null && excelhandlerBean.getObjects().length > 0) {
            excelhandlerBean.getExcelhandlerChain().handler(excelhandlerBean);
        }
    }

    /**
     * 设置表头bean
     */
    private Object[] getExcleHeader(Class<?> clazz) {
        //存放表头信息
        Object[] objects;
        if (!MapUtils.isEmpty(dataMap) && dataMap.containsKey(clazz.getName())) {
            objects = dataMap.get(clazz.getName());
        } else {
            //获取含有 Excel 注解的属性
            Field[] fields = clazz.getDeclaredFields();

            List<ExcelBean> excelBeans = new ArrayList<>(fields.length);

            for (Field field : fields) {
                // 获取注解中的属性 即 表头信息
                if (field.isAnnotationPresent(Excel.class)) {
                    getMax(field.getAnnotation(Excel.class));
                }
            }
            //设置父表头所占列
            setExcelHeaderCell();

            //先设置合计为不显示
            isShowTotal = false;
            for (Field field : fields) {
                // 获取注解中的属性 即 表头信息
                if (field.isAnnotationPresent(Excel.class)) {
                    Excel excel = field.getAnnotation(Excel.class);
                    if (excel.isSum()) {
                        isShowTotal = true;
                    }
                    //设置表头信息
                    getExcelBeanList(excel, field, excelBeans);
                }
            }
            Set<ExcelBean> excelBeanHashSet = new HashSet<>(excelBeans);
            excelBeans.clear();
            excelBeans.addAll(excelBeanHashSet);
            //根据order 排序表头要显示字段顺序
            excelBeans.sort((o1, o2) -> {
                if (o1.getOrder().equals(o2.getOrder())) {
                    return o1.getFirstRow() - o2.getFirstRow();
                }
                return o1.getOrder() - o2.getOrder();
            });
            // 获取字段属性
            getFieldList(excelBeans);
            //计算表头的位置
            accountExcelHeader(excelBeans);
            //对表头进行分层
            objects = layeredExcelHeader(excelBeans);

        }

        return objects;
    }


    private void getMax(Excel excel) {
        //获取列名
        String columnName = excel.columnName();
        columnList.add(columnName);
        String[] columnNames = columnName.split("\\|");
        max = max > columnNames.length ? max : columnNames.length;
    }


    /**
     * 设置表头的所占列数
     */
    private void setExcelHeaderCell() {
        for (String str : columnList) {
            String[] columnNames = str.split("\\|");
            for (int i = 0; i < columnNames.length - 1; i++) {
                if (headerMap.containsKey(columnNames[i])) {
                    Integer num = MapUtils.getInteger(headerMap, columnNames[i]) + 1;
                    headerMap.put(columnNames[i], num);
                } else {
                    headerMap.put(columnNames[i], 1);
                }
            }
        }
    }

    /**
     * 计算表头的位置
     */
    private void accountExcelHeader(List<ExcelBean> excelBeans) {
        Integer index = 0;
        Integer sum;
        for (ExcelBean excelBean : excelBeans) {
            excelBean.setFirstCol(index);
            if (headerMap.containsKey(excelBean.getColumnName())) {
                //excel 列和行是从 0 开始的
                sum = index + MapUtils.getInteger(headerMap, excelBean.getColumnName()) - 1;
                excelBean.setLastCol(sum);
            } else {
                excelBean.setLastCol(index);
                index++;
            }
            Boolean flag = (excelBean.getLastRow() - excelBean.getFirstRow()) > 0 ||
                    (excelBean.getLastCol() - excelBean.getFirstCol()) > 0;
            excelBean.setIsMerge(flag ? Boolean.TRUE : Boolean.FALSE);
        }
    }

    private Object[] layeredExcelHeader(List<ExcelBean> excelBeans) {
        List<ExcelBean> list;
        Object[] objects = new Object[max];
        for (int i = 0; i < max; i++) {
            list = new ArrayList<>();
            for (ExcelBean bean : excelBeans) {
                if (bean.getFirstRow() == i) {
                    list.add(bean);
                }
            }
            objects[i] = list;
        }
        return objects;
    }

    /**
     * 获取字段属性
     */
    private void getFieldList(List<ExcelBean> excelBeans) {
        for (ExcelBean excelBean : excelBeans) {
            fieldList.add(excelBean.getField());
        }
    }


    private void getExcelBeanList(Excel excel, Field field, List<ExcelBean> excelBeanList) {
        //获取列名
        String[] columnNames = excel.columnName().split("\\|");
        Integer length = columnNames.length;
        for (int i = 0; i < length; i++) {
            ExcelBean excelBean = ExcelBean.builder().columnName(columnNames[i])
                    .format(excel.format()).style(excel.style()).order(excel.order()).width(excel.width()).firstRow(i)
                    .isSum(excel.isSum())
                    .build();
            if (length == 1) {
                excelBean.setLastRow(max - 1);
            } else if (length < max) {
                if (i < length - 1) {
                    //默认第一个只占一行
                    excelBean.setLastRow(i);
                } else {
                    excelBean.setLastRow(max - length + i);
                }
            } else if (length.equals(max)) {
                excelBean.setLastRow(i);
            }
            if (i == length - 1) {
                excelBean.setField(field.getName());
            }
            excelBeanList.add(excelBean);
        }

    }
}
