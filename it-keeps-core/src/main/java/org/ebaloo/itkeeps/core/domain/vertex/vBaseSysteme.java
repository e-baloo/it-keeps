

package org.ebaloo.itkeeps.core.domain.vertex;

import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;

@DatabaseVertrex()
public abstract class vBaseSysteme extends vBase {

	protected vBaseSysteme() {
		super();
	}
	
	protected vBaseSysteme(final jBase j) {
		super(j);
	}

	protected vBaseSysteme(String name) {
		super(name);
	}	
}



