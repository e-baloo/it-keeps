
package org.ebaloo.itkeeps.domain.vertex;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.domain.BaseUtils;
import org.ebaloo.itkeeps.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.domain.pojo.JCredential;
import org.ebaloo.itkeeps.tools.SecurityFactory;
import org.ebaloo.itkeeps.tools.SecurityFactory.SecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;


@ModelClassAnnotation()
@DatabaseVertrex()
public class User extends BaseStandard {

	private static Logger logger = LoggerFactory.getLogger(User.class);

	
	public User() {
		super();
	}
	
	public User(final BaseAbstract abase) {
		super(abase);
	}


	
	/*
	 * 
	 * 
	 */
	
	
	
	
	/*
	 *   ID
	 */
	
	public static final String ID = "id";

	@DatabaseProperty(name = ID, isNotNull = true)
	public String getId()  {
		return this.getProperty(ID);
	}

	public void setId(final String id) {
		this.setProperty(ID, id);
	}

	
	
	public static User getByCredentials(final JCredential credentials) {

		if(credentials == null) 
			throw new RuntimeException("TODO"); //TODO
		
		if(credentials.getId() == null)
			throw new RuntimeException("TODO"); //TODO
		
		
		String cmdSql = "SELECT FROM " + User.class.getSimpleName() + " WHERE " + BaseUtils.WhereClause.enable() + " AND " + User.ID
				+ ".toLowerCase() = ?";

		List<OrientVertex> list = CommonOrientVertex.command(cmdSql, credentials.getId().toLowerCase());

		if (list.isEmpty()) {
			if (logger.isTraceEnabled())
				logger.trace("OrientVertex not found for 'id':" + credentials.getId());
			return null;
		}

		if (list.size() > 1) {
			throw new RuntimeException("To many obejct for 'id': " + credentials.getId());
		}

		OrientVertex ov = list.get(0);

		User user = new User();
		user.setOrientVertex(ov);
		
		if (logger.isTraceEnabled())
			logger.trace("User found for 'id':" +  credentials.getId() + " @" + ov.getType().getName() + " "
					+ ov.getIdentity().toString());

		
		SecurityFactory.validateCredential(user, credentials);

		return user;
	}

	
	/*
	 * ROLES
	 */
	
	public static final String ROLES = "roles";
	
	
	@DatabaseProperty(name = ROLES, type = OType.EMBEDDEDLIST)
	public List<SecurityRole> getRoles() {
		return this.getEmbeddedListString(ROLES).stream().map(e -> SecurityRole.valueOf(e)).collect(Collectors.toList());
	}
	
	
	public void setRoles(List<SecurityRole> roles) {
		
		if(roles == null)
			roles = new ArrayList<>();
		
		this.setProperty(ROLES, roles.stream().map(e -> e.toString()).collect(Collectors.toList()));
	}

	
	
}



