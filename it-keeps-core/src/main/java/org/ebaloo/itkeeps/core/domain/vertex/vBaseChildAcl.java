
package org.ebaloo.itkeeps.core.domain.vertex;

import org.ebaloo.itkeeps.api.model.jBaseStandard;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;

@DatabaseVertrex()
public class vBaseChildAcl extends vBaseStandard {

	
	public vBaseChildAcl() {
		super();
	}
	
	public vBaseChildAcl(
			final String name
			) {
		super(name);
		
	}

	public vBaseChildAcl(jBaseStandard j, boolean b) {
		super(j, b);
	}


}



