

package org.ebaloo.itkeeps.core.domain.vertex;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;

@DatabaseVertrex()
public abstract class BaseSysteme extends Base {

	public BaseSysteme() {
		super();
	}
	
	public BaseSysteme(final BaseAbstract abase) {
		super(abase);
	}

	public BaseSysteme(
			final String name
			) {
		super(name);
		
	}
	
}



