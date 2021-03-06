
package org.ebaloo.itkeeps.api.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Documented
@Retention (RUNTIME)
@Target({TYPE, METHOD})
public @interface aApplicationRolesAllowed {
	enRole value();
	
	enum enRole {

		GUEST,
		USER,
		ADMIN,
		ROOT;


		public boolean isGuest() {
			return true;
		}
		
		public boolean isUser() {
			return this == USER || this == ADMIN || this == ROOT;
		}

		public boolean isAdmin() {
			return this == ADMIN || this == ROOT;
		}

		public boolean isRoot() {
			return this == ROOT;
		}
		
		public boolean isInRole(enRole role) {
			
			return this.ordinal() >= role.ordinal(); 
			
		}

	}
	
	
	
}