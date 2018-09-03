
package org.infrastructure.common.valid;

import org.infrastructure.common.valid.interfaces.annotation.Email;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
*
* @Title: TODO
* @Description: TODO(用一句话描述该文件做什么) 
* @author ben
* @date 2018年4月26日 上午10:42:34
* @version V1.0  
* 
**/

public class EmailValidator implements ConstraintValidator<Email,String> {

    private String regexp;

    @Override
    public void initialize(Email constraintAnnotation) {
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