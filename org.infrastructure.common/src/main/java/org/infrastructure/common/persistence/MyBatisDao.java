package org.infrastructure.common.persistence;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * 标识MyBatis的DAO,方便{@link org.mybatis.spring.mapper.MapperScannerConfigurer}的扫描.
 * 
 * annotationClass：配置了该注解的dao才会被扫描器扫描，与basePackage是与的作用.
 * <p>
 * <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">  
 *   <property name="basePackage" value="com.xxx.dao" />  
 *   <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />  
 *   <property name="annotationClass" value="com.xxx.dao.BatchAnnotation" />  
 *  </bean>
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface MyBatisDao {
    
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any
     */
    String value() default "";

}
