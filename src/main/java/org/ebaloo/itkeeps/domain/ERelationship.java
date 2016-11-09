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

package org.ebaloo.itkeeps.domain;

import com.tinkerpop.blueprints.Direction;

public enum ERelationship {

	//TODO: A voir pour inverser le terme IN/OUT -> PARENT/ENFANT
	PARENT(Direction.OUT),
	CHILD(Direction.IN),
	BOTH(Direction.BOTH);
	
	private final Direction direction;
	
	private ERelationship(Direction direction)
	{
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

}
