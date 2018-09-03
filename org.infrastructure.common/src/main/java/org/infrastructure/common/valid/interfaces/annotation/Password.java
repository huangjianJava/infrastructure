
package org.infrastructure.common.valid.interfaces.annotation;

import org.infrastructure.common.valid.PasswordValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @Title: TODO
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ben
 * @date 2018年4月26日 下午3:49:40
 * @version V1.0  
 * 
 **/

@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})   
//约束注解应用的目标元素类型(METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER等)
@Retention(RetentionPolicy.RUNTIME)   // 约束注解应用的时机
@Documented
@Constraint(validatedBy ={PasswordValidator.class})  // 与约束注解关联的验证器
public @interface Password {  

  String message() default "密码格式不正确!";   // 约束注解验证时的输出消息
  
  int maxLen() default 0; //最小长度,为0不验证长度
  int minLen() default 0; //最大长度,为0不验证长度

  Class<?>[] groups() default { };  // 约束注解在验证时所属的组别

  Class<? extends Payload>[] payload() default { }; // 约束注解的有效负载

  /**
   * 密码的验证规则(必须字母、数字、特殊字符组成)
   * @return
   */
  String regexp() default "^^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*_-]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*_-]+$)(?![\\d!@#$%^&*_-]+$)[a-zA-Z\\d!@#$%^&*_-]+$";

  @Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})   
  // 约束注解应用的目标元素类型(METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER等)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
      Email[] value();
  }
}
