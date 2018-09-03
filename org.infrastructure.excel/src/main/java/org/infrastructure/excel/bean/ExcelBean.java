package org.infrastructure.excel.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * excel 导出辅助类
 *
 * @author liuzw
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelBean {

    /**
     * 属性名
     */
    private String field;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 行的高度
     */
    private Integer height;

    /**
     * 列的宽度
     */
    private Integer width;

    /**
     * 样式：居中，居左，居右
     */
    private ExcelStyleEnum style;

    /**
     * 输出的的格式
     */
    private String format;

    /**
     * 列排列顺序
     */
    private Integer order;

    /**
     * 所在行
     */
    private Integer firstRow;
    /**
     * 所在行
     */
    private Integer lastRow;

    /**
     * 所在列
     */
    private Integer firstCol;

    /**
     * 所在列
     */
    private Integer lastCol;

    /**
     * 是否合并
     */
    private Boolean isMerge;

    /**
     * 是否求和
     */
    private Boolean isSum;


    @Override
    public boolean equals(Object obj) {
        if (null == obj){
            return false;
        }
        ExcelBean excelBean = (ExcelBean) obj;
        if (field != null) {
            return field.equals(excelBean.field);
        }
        return columnName.equals(excelBean.columnName);
    }

    @Override
    public int hashCode() {
        return columnName.hashCode();
    }


}
