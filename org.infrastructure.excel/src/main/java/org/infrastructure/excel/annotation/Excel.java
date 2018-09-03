package org.infrastructure.excel.annotation;


import org.infrastructure.excel.bean.ExcelStyleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel导出字段的注解
 *
 * @author liuzw
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {


    /**
     * 列名
     */
    String columnName();

    /**
     * 行的高度
     */
    int height() default 0;

    /**
     * 列的宽度
     */
    int width() default 10;

    /**
     * 样式：居中，居左，居右
     * 默认居中
     */
    ExcelStyleEnum style() default ExcelStyleEnum.CENTER;

    /**
     * 输出的的格式
     */
    String format() default "";

    /**
     * 是否求和
     */
    boolean isSum() default false;

    /**
     * 列排列顺序
     */
    int order();

}
