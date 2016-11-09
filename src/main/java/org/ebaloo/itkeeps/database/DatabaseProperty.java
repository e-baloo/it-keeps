
package org.ebaloo.itkeeps.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.orientechnologies.orient.core.metadata.schema.OType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface DatabaseProperty {

	String name();

	boolean isNotNull() default false; 
	
	boolean isReadOnly() default false;
	
	OType type() default OType.STRING;

// ITA-198
//	String defaultValue() default "";

	boolean isIndex() default false;

}
