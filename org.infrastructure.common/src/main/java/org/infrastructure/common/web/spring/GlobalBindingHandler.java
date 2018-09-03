package org.infrastructure.common.web.spring;

import org.apache.commons.lang3.StringEscapeUtils;
import org.infrastructure.common.utils.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

@ControllerAdvice
public class GlobalBindingHandler {
  //private static Logger log = LoggerFactory 
  //    .getLogger(GlobalBindingHandler.class);

  /**
   * 初始化数据绑定.
   *  1. 将所有传递进来的String进行HTML编码，防止XSS攻击 
   *  2. 将字段中Date类型转换为String类型
   *  @param binder WebDataBinder
   */
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
    binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
      }

      @Override
      public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
      }
    });
    // Date 类型转换
    binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(DateUtil.parseDate(text));
      }
      // @Override
      // public String getAsText() {
      // Object value = getValue();
      // return value != null ? DateUtils.formatDateTime((Date)value) : "";
      // }
    });
  }
}
