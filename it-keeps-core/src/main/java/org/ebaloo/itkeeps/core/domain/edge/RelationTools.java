/*
 * Copyright (c) 2001-2016 Group JCDecaux. 
 * 17 rue Soyer, 92523 Neuilly Cedex, France.
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information
 * of Group JCDecaux ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you
 * entered into with Group JCDecaux.
 */

package org.ebaloo.itkeeps.core.domain.edge;

import com.tinkerpop.blueprints.Direction;

public class RelationTools {

	public static String toLogger(Direction dir)
	{
		switch(dir) {
		
		case IN:
			return "<--";
			
		case OUT:
			return "-->";
			
		case BOTH:
			return "<->";
			
		}
		
		return "?-?";
	}

	
	
}
