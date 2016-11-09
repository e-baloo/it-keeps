
package org.ebaloo.itkeeps.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModelClassAnnotation{
	
	String name() default "";
	
	String[] disableProperty() default {};

	boolean isAbstract() default false;

}
