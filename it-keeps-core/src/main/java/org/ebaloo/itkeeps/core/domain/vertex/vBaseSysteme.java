

package org.ebaloo.itkeeps.core.domain.vertex;

import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;

@DatabaseVertrex()
public abstract class vBaseSysteme extends vBase {

	public vBaseSysteme() {
		super();
	}
	
	/*
	public BaseSysteme(final BaseAbstract abase) {
		super(abase);
	}
	*/

	public vBaseSysteme(
			final String name
			) {
		super(name);
		
	}
	
	protected vBaseSysteme(final jBase j, final boolean f) {
		super(j, f);
		
		if(f)
			this.setEnable(Boolean.TRUE);
	}	
}



