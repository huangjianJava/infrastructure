
package org.infrastructure.common.valid.interfaces.annotation;


import org.infrastructure.common.valid.MobileValidator;
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
 * @date 2018年4月26日 下午2:28:02
 * @version V1.0  
 * 
 **/
@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})   
//约束注解应用的目标元素类型(METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER等)
@Retention(RetentionPolicy.RUNTIME)   // 约束注解应用的时机
@Documented
@Constraint(validatedBy ={MobileValidator.class})  // 与约束注解关联的验证器
public @interface Mobile {  
  
    String message() default "手机号码不正确";   // 约束注解验证时的输出消息

    Class<?>[] groups() default { };  // 约束注解在验证时所属的组别

    Class<? extends Payload>[] payload() default { }; // 约束注解的有效负载

    /**
     * 手机的验证规则
     * @return
     */
    String regexp() default "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9])|(19[0,9])|(166))[0-9]{8}$";

    @Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})   
    // 约束注解应用的目标元素类型(METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER等)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Email[] value();
    }
}
