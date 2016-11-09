

package org.ebaloo.itkeeps.domain.vertex;

import org.ebaloo.itkeeps.database.annotation.DatabaseVertrex;

@DatabaseVertrex()
public class BaseSysteme extends Base {

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



