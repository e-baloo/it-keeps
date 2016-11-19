

package org.ebaloo.itkeeps.core.domain.vertex;

import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;

@DatabaseVertrex()
public abstract class BaseSysteme extends Base {

	public BaseSysteme() {
		super();
	}
	
	/*
	public BaseSysteme(final BaseAbstract abase) {
		super(abase);
	}
	*/

	public BaseSysteme(
			final String name
			) {
		super(name);
		
	}
	
	protected BaseSysteme(final JBase j, final boolean f) {
		super(j, f);
		
		if(f)
			this.setEnable(Boolean.TRUE);
	}	
}



