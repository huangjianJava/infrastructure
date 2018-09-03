
package org.infrastructure.common.valid;

import org.infrastructure.common.valid.interfaces.annotation.Password;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @Title: TODO
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ben
 * @date 2018年4月26日 下午3:50:50
 * @version V1.0  
 * 
 **/

public class PasswordValidator implements ConstraintValidator<Password,String> {

    private String regexp;
    
    private int minLen;
    private int maxLen;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
        this.minLen = constraintAnnotation.minLen();
        this.maxLen = constraintAnnotation.maxLen();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            if (minLen > 0) {
                if (value.length() < minLen) {
                    return false;
                }
            }
            if (maxLen > 0) {
                if (value.length() > maxLen) {
                    return false;
                }
            }
        }
        if (value == null) {
            return false;
        } else if (value.matches(regexp)){
            return true;
        }
        return false;
    }
}
