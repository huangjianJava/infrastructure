
package org.infrastructure.common.valid;


import org.infrastructure.common.valid.interfaces.annotation.Mobile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @Title: TODO
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ben
 * @date 2018年4月26日 下午2:28:38
 * @version V1.0  
 * 
 **/

public class MobileValidator implements ConstraintValidator<Mobile,String> {

    private String regexp;

    @Override
    public void initialize(Mobile constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null){
            return true;
        } else if (value.matches(regexp)){
            return true;
        }
        return false;
    }
}