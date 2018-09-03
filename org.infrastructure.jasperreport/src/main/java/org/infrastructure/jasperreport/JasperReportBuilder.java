package org.infrastructure.jasperreport;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.infrastructure.jasperreport.enums.JasperReportTypeEnum;
import org.infrastructure.jasperreport.utils.JasperReportUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author liuzw
 * @date 2018/5/15 16:36
 **/
public class JasperReportBuilder {

    private JasperReportBuilder(){}

    /**
     * 根据已经编辑好的模板 生成打印报表
     *
     * @param dataList  数据
     * @param path      路径
     */
    public static void createByTemplate(List<?> dataList, String path) throws Exception {
        createByTemplate(dataList, path, JasperReportTypeEnum.PDF);
    }

    /**
     * 根据已经编辑好的模板 生成打印报表
     *
     * @param dataList           数据
     * @param type              打印类型
     */
    public static void createByTemplate(List<?> dataList, String path, JasperReportTypeEnum type) throws Exception {
        create(dataList, path, type);
    }

    /**
     * 根据已经编辑好的模板 生成打印报表
     *
     * @param dataList    数据
     * @param inputStream inputStream
     */
    public static void createByTemplateStream(List<?> dataList, InputStream inputStream) throws Exception {
        createByTemplateStream(dataList, inputStream, JasperReportTypeEnum.PDF);
    }

    /**
     * 根据已经编辑好的模板 生成打印报表
     *
     * @param dataList           数据
     * @param type              打印类型
     */
    public static void createByTemplateStream(List<?> dataList, InputStream inputStream, JasperReportTypeEnum type) throws Exception {
        createByStream(dataList, inputStream, type);
    }


    /**
     * 生成打印报表
     *
     * @param dataList   数据
     * @param path       模板路径
     * @param type       类型
     */
    private static void create(List<?> dataList, String path, JasperReportTypeEnum type) throws Exception {
         JasperDesign jasperDesign = JRXmlLoader.load(path);
         create(jasperDesign, dataList, type);
    }

    /**
     * 生成打印报表
     *
     * @param dataList     数据
     * @param inputStream  inputStream
     * @param type         类型
     */
    private static void createByStream(List<?> dataList, InputStream inputStream, JasperReportTypeEnum type) throws Exception {
        JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
        create(jasperDesign, dataList, type);
    }

    /**
     * 生成打印报表
     *
     * @param dataList      数据
     * @param jasperDesign  JasperDesign
     * @param type          类型
     */
    @SuppressWarnings(value={"unchecked"})
    private static void create(JasperDesign jasperDesign, List<?> dataList, JasperReportTypeEnum type) throws Exception {
        JasperReport jasperReport = JasperCompileManager.getInstance(DefaultJasperReportsContext.getInstance()).compile(jasperDesign);
        //STEP 2 : 指定数据源
        JRDataSource dataSource;
        if (dataList != null && dataList.size() > 0) {
            Object obj = dataList.get(0);
            if (obj instanceof Map) {
                dataSource = new JRMapCollectionDataSource((List<Map<String, ?>>) dataList);
            } else {
                dataSource = new JRBeanCollectionDataSource(dataList);
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,  new HashMap<>(1), dataSource);

            switch (type) {
                case PDF:
                    JasperReportUtil.pdf(jasperPrint);
                    break;
                case HTML:
                    JasperReportUtil.html(jasperPrint);
                    break;
                case XML:
                    JasperReportUtil.xml(jasperPrint);
                    break;
                case DOCX:
                    JasperReportUtil.docx(jasperPrint);
                    break;
                case XLSX:
                    JasperReportUtil.xlsx(jasperPrint);
                    break;
                case PPTX:
                    JasperReportUtil.pptx(jasperPrint);
                    break;
                default:
                    JasperReportUtil.pdf(jasperPrint);
                    break;
            }
        }
    }


}
