package org.infrastructure.excel;

import lombok.Builder;
import lombok.Data;
import org.infrastructure.excel.annotation.Excel;


/**
 *  @author liuzw
 */

@Data
@Builder
public class TestExcelDto {

    @Excel(columnName = "年龄", order = 2)
    private Integer age;
    @Excel(columnName = "性别", order = 3)
    private String sex;
    @Excel(columnName = "姓名", order = 1)
    private String name;
    @Excel(columnName = "测试|测试1", order = 5)
    private String test1;
    @Excel(columnName = "测试|测试2", order = 6)
    private String test2;
    @Excel(columnName = "AA|BB", order = 7)
    private String test3;
    @Excel(columnName = "AA|CC", order = 8, format = "0.00", isSum = true)
    private Double test4;
    @Excel(columnName = "AA|DD|EE", order = 9, format = "0.00", isSum = true)
    private Double test5;
    @Excel(columnName = "AA|DD|FF", order = 10)
    private String test6;
}
