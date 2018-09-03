package org.infrastructure.excel;

import org.infrastructure.excel.handler.ExcelCreateFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzw
 * @Title 描述
 * @Description 描述
 * @date 2018/6/13 10:15
 **/
public class Test {
    public static void main(String[] args) {
        // 模拟从数据库查出的数据
        List<TestExcelDto> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(TestExcelDto.builder().age(i).name("测试" + i).sex(i % 2 == 0 ? "男" : "女")
                    .test1("-test1-" + i + "--")
                    .test2("-test2-" + i + "--").test3("-test3-" + i + "--").test4(Double.valueOf(i * 100 + ".6666"))
                    .test5(Double.valueOf(i * 100 + ".12345"))
                    .build());
        }
        //调用生成excel方法 注意实体类 和List<T> 泛型是同一类型
        // create 方法中 第一个参数为导出excel文件的名字，
        //              第二个参数为从数据库查出来的数据例如 List<TestExcelDto>,
        //              第三个参数为当前要导出的excel的实体类，
        ExcelCreateFactory.create("测试Demo", list, TestExcelDto.class);
    }
}
