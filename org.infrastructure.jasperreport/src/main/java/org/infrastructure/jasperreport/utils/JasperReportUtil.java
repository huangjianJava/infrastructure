package org.infrastructure.jasperreport.utils;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @author liuzw
 */
public class JasperReportUtil {


    private JasperReportUtil() {
    }



    /**
     *
     */
    public static void pdf(JasperPrint jasperPrint) throws Exception {
        HttpServletResponse response = getResponse();
        JRPdfExporter exporter = new JRPdfExporter();
        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
        exporter.setExporterInput(exporterInput);
        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(response.getOutputStream());
        exporter.setExporterOutput(exporterOutput);
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }


    /**
     *
     */
    public static void xml(JasperPrint jasperPrint) throws Exception {
        HttpServletResponse response = getResponse();
        JRXmlExporter xml = new JRXmlExporter();
        xml.setExporterInput(new SimpleExporterInput(jasperPrint));
        xml.setExporterOutput(new SimpleXmlExporterOutput(response.getOutputStream()));
        xml.exportReport();
    }


    /**
     *
     */
    public static void html(JasperPrint jasperPrint) throws Exception {
        HttpServletResponse response = getResponse();
        HtmlExporter html = new HtmlExporter();
        html.setExporterInput(new SimpleExporterInput(jasperPrint));
        html.setExporterOutput(new SimpleHtmlExporterOutput(response.getOutputStream()));
        html.exportReport();
    }


    /**
     *  docx
     */
    public static void docx(JasperPrint jasperPrint) throws Exception{
            String tmpFilename = System.currentTimeMillis() + ".docx";
            HttpServletResponse response = getResponse();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="
                    + URLEncoder.encode(tmpFilename,"utf8"));
            response.addHeader("Content-Type", "application/msword");
            JRDocxExporter docx = new JRDocxExporter();
            docx.setExporterInput(new SimpleExporterInput(jasperPrint));
            docx.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            docx.exportReport();
    }


    /**
     * xlsx
     */
    public static void xlsx(JasperPrint jasperPrint) throws Exception {
            String tmpFilename = System.currentTimeMillis() + ".xlsx";
            HttpServletResponse response = getResponse();
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename="
                    + URLEncoder.encode(tmpFilename,"utf8"));
            JRXlsxExporter xlsx = new JRXlsxExporter();
            xlsx.setExporterInput(new SimpleExporterInput(jasperPrint));
            xlsx.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(true);
            xlsx.setConfiguration(configuration);
            xlsx.exportReport();
    }


    /**
     * pptx
     */
    public static void pptx(JasperPrint jasperPrint) throws Exception {
            String tmpFilename = System.currentTimeMillis() + ".pptx";
            HttpServletResponse response = getResponse();
            response.reset();
            response.setContentType("application/msword;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + tmpFilename);
            JRPptxExporter pptx = new JRPptxExporter();
            pptx.setExporterInput(new SimpleExporterInput(jasperPrint));
            pptx.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            pptx.exportReport();
    }

    private static HttpServletResponse getResponse(){
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }

}