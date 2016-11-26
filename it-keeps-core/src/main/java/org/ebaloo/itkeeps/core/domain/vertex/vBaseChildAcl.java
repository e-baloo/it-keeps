
package org.ebaloo.itkeeps.core.domain.vertex;

import org.ebaloo.itkeeps.api.model.jBaseStandard;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;

@DatabaseVertrex()
public class vBaseChildAcl extends vBaseStandard {

	
	protected vBaseChildAcl() {
		super();
	}

	protected vBaseChildAcl(jBaseStandard j) {
		super(j);
	}


}



