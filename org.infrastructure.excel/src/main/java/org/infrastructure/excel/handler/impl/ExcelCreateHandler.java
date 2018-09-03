package org.infrastructure.excel.handler.impl;


import org.apache.commons.lang3.StringUtils;
import org.infrastructure.excel.bean.ExcelhandlerBean;
import org.infrastructure.excel.handler.ExcelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 生成excel
 *
 * @author liuzw
 */
public class ExcelCreateHandler implements ExcelHandler {

    private static Logger logger = LoggerFactory.getLogger(ExcelCreateHandler.class);

    @Override
    public void handler(ExcelhandlerBean excelhandlerBean) {
        logger.info("-----------开始生成excel----------");
        try {

            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            HttpServletResponse response = attributes.getResponse();
            HttpServletRequest request = attributes.getRequest();

            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Access-Control-Allow-Origin", "*");
            // 设置文件头：最后一个参数是设置下载文件名
            String fileName = excelhandlerBean.getExcelName();
            final String userAgent = request.getHeader("USER-AGENT");

            if (StringUtils.contains(userAgent, "MSIE")
                    || StringUtils.contains(userAgent, "Trident")
                    || StringUtils.contains(userAgent, "Edge")) {
                //IE浏览器
                fileName = URLEncoder.encode(fileName,"UTF8");
            } else if (StringUtils.contains(userAgent, "Mozilla")) {
                //google,火狐浏览器
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }

            response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            excelhandlerBean.getWorkbook().write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            }
            logger.info("-----------导出成功!------------");
            out.close();
            excelhandlerBean.getWorkbook().close();
        } catch (Exception e) {
            logger.error("-------------导出失败, 失败原因：", e);
        }
    }
}
