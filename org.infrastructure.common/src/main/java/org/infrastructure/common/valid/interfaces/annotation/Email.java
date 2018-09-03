
package org.infrastructure.common.valid.interfaces.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import org.infrastructure.common.valid.EmailValidator;

/**
 *
 * @Title: TODO
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ben
 * @date 2018年4月26日 上午10:34:12
 * @version V1.0  
 * 
 **/

@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})   
// 约束注解应用的目标元素类型(METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER等)
@Retention(RetentionPolicy.RUNTIME)   // 约束注解应用的时机
@Documented
@Constraint(validatedBy ={EmailValidator.class})  // 与约束注解关联的验证器
public @interface Email {

    String message() default "电子邮箱格式错误!";   // 约束注解验证时的输出消息

    Class<?>[] groups() default { };  // 约束注解在验证时所属的组别

    Class<? extends Payload>[] payload() default { }; // 约束注解的有效负载

    /**
     * 邮箱的验证规则
     * @return
     */
    String regexp() default "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    @Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})   
    // 约束注解应用的目标元素类型(METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER等)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Email[] value();
    }
}
