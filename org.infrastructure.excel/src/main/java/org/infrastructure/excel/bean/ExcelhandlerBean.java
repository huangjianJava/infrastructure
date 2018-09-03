package org.infrastructure.excel.bean;

import lombok.Builder;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.infrastructure.excel.handler.impl.ExcelhandlerChain;

import java.util.List;
import java.util.Map;

/**
 * 封装执行链所需数据
 *
 * @author liuzw
 */

@Data
@Builder
public class ExcelhandlerBean {

    /**
     * 使用@excel注解的实体类
     */
    private Class<?> clazz;

    /**
     * 存放表头信息
     * 数组中存放的格式为 List<ExcelBean>
     */
    private Object[] objects;

    /**
     * 执行链条
     */
    private ExcelhandlerChain excelhandlerChain;

    /**
     * 生成Excel 工作薄对象
     */
    private HSSFWorkbook workbook;

    /**
     * 生成Excel sheet对象
     */
    private HSSFSheet sheet;

    /**
     * 存放加了@excel注解的字段名称
     */
    private List<String> fieldList;

    /**
     * 存放字段的格式化
     */
    private Map<String, CellStyle> fieldFormatMap;

    /**
     * 存放求和的字段
     */
    private List<String> fieldSumList;

    /**
     * 表头所占行数
     */
    private Integer max;

    /**
     * 数据
     */
    private List<?> dataList;

    /**
     * excel表名
     */
    private String excelName;

    /**
     * 是否显示合计
     */
    private Boolean isShowTotal;
}
