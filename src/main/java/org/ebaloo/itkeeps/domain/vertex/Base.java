

package org.ebaloo.itkeeps.domain.vertex;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.domain.BaseUtils;
import org.ebaloo.itkeeps.domain.Guid;
import org.ebaloo.itkeeps.domain.GuidFactory;
import org.ebaloo.itkeeps.domain.ModelFactory;
import org.ebaloo.itkeeps.domain.ModelFactory.ModelClass;
import org.ebaloo.itkeeps.domain.annotation.ModelClassAnnotation;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import com.orientechnologies.orient.core.metadata.schema.OType;

/**
 * 
 *
 */
@ModelClassAnnotation()
@DatabaseVertrex(isAbstract = true)
public abstract class Base extends BaseAbstract {

	private static Logger logger = LoggerFactory.getLogger(Base.class);

	public static final String ENABLE = "enable";
	public static final String CREATION_DATE = "creationDate";
	public static final String DESCRIPTION = "description";


	public Base() {
	}

	public Base(final BaseAbstract abase) {
		super(abase);
	}

	public Base(final String name) {
		super(true);

		this.defaultSetting(GuidFactory.getGuid());

		if(StringUtils.isNoneBlank(name)) {
			this.setName(name);
			this.commit();
		}

	}

	
	private void defaultSetting(Guid guid) {

		this.setProperty(GUID, guid.toString());

		this.setProperty(CREATION_DATE, DateTime.now(DateTimeZone.UTC).toString());
		this.setProperty(ENABLE, true);
		this.setProperty(DESCRIPTION, StringUtils.EMPTY);
		this.setProperty(NAME, guid.toString());		
		this.commit();
	}
	
	
	public void defaultSetting(String name) {

		if(!this.hasGuid()) {
			defaultSetting(GuidFactory.getGuid());
		}

		this.setName(name);
		this.commit();
		
		
	}
	

	/*
	 * NAME
	 */
	
	public static final String NAME = "name";
	
	@DatabaseProperty(name = NAME, isNotNull = true)
	public final String getName() {
		return this.getProperty(NAME);
	}

	public void setName(String newValue) {
		this.setProperty(NAME, newValue);
	}

	/*
	 * ENABLE
	 */

	@DatabaseProperty(name = ENABLE, type = OType.BOOLEAN)
	public Boolean isEnable() {
		return (Boolean) this.getProperty(ENABLE);
	}

	/*
	 * DISABLE
	 */

	public boolean disable() {
		
		this.deleteAllEdges(Direction.IN);
		this.deleteAllEdges(Direction.OUT);
		this.setProperty(ENABLE, false);
		this.commit();
		return true;
	}

	



	

	/*
	 * CREATION_DATE
	 */
	
	@DatabaseProperty(name = CREATION_DATE, isReadOnly = true)
	public final DateTime getCreationDate() {
		String value = this.getProperty(CREATION_DATE);

		if (value == null) {
			return null;
		}

		return DateTime.parse(value);
	}



	
	

	/*
	 * DESCRIPTION
	 */
	@DatabaseProperty(name = DESCRIPTION)
	public final String getDescription() {
		return this.getProperty(DESCRIPTION);
	}

	public final void setDescription(final String value) {
		this.setProperty(DESCRIPTION, value);
	}

	
	

	
	/*
	 * GetByName
	 */

	public static <T extends BaseAbstract> T getByName(final ModelClass<T> target,
			final String name) {
		return getByName(target, name, false);
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseAbstract> T getByName(final ModelClass<T> target,
			final String name, boolean isInstanceof) {

		String cmdSql = "SELECT FROM " + target.getClassName() + " WHERE " + BaseUtils.WhereClause.enable() + " AND " + BaseUtils.WhereClause.classIsntanceOf(target, isInstanceof) + " AND " + Base.NAME
				+ ".toLowerCase() = ?";

		List<OrientVertex> list = CommonOrientVertex.command(cmdSql, name.toLowerCase());

		if (list.isEmpty()) {
			if (logger.isTraceEnabled())
				logger.trace("OrientVertex not found for name:" + name);
			return null;
		}

		if (list.size() > 1) {
			// TODO
			throw new RuntimeException(new Exception ("To many obejct for 'name': " + name));
		}

		OrientVertex ov = list.get(0);

		ModelClass<T> modelClass = (ModelClass<T>) ModelFactory.get(ov.getType().getName());

		T baseAbstract;
		try {
			baseAbstract = modelClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		baseAbstract.setOrientVertex(ov);

		if (logger.isTraceEnabled())
			logger.trace("OrientVertex found for name:" + name + " @" + ov.getType().getName() + " #"
					+ ov.getIdentity().toString());

		return baseAbstract;
	}





	
	
	




	
	
	/*
	 * 
	 */
	

	public final static List<BaseAbstract> getAllBase(final ModelClass<? extends BaseAbstract> target) {
		return getAllBase(target, true);
	}
	
	public final static List<BaseAbstract> getAllBase(final ModelClass<? extends BaseAbstract> target, final boolean isInstanceof) {

		StringBuilder request = new StringBuilder();
		
		request.append("SELECT FROM " + target.getClassName() + " ");
		request.append("WHERE ");
		BaseUtils.WhereClause.enable(request);
		request.append(" AND ");
		BaseUtils.WhereClause.classIsntanceOf(target, isInstanceof, request);

		BaseQuery bq = new BaseQuery();
        return bq.commandBaseAbstract(request.toString());
        
	}
	
	
	

	
	/*
	 * 
	 */

	/*
	public static final <T extends Version> T getVersionByOtherName(Class<T> target, final String iValue) {
		return getVersionByOtherName(target, iValue, true);
	}
	
	@SuppressWarnings("unchecked")
	public static final <T extends Version> T getVersionByOtherName(Class<T> target, final String iValue, final boolean instanceOf) {
		
		if(StringUtils.isBlank(iValue)) {
			return null;
		}
		
		String value = stripOtherName(iValue);
		
		// TODO Injection SQL
		
		String cmdSQL = "";
		cmdSQL += "SELECT FROM " + target.getSimpleName() + " ";
		cmdSQL += "WHERE ";
		cmdSQL +=    OTHER_NAME + " IN ['" + value + "'] ";
		cmdSQL +=   "AND "; 
		cmdSQL += BaseUtils.WhereClause.enable(context);
		cmdSQL +=   "AND "; 
		cmdSQL += BaseUtils.WhereClause.classIsntanceOf(target, instanceOf);
		
		
		BaseQuery bq = new BaseQuery(context);
		List<BaseAbstract> list = bq.commandBaseAbstract(cmdSQL);
        

		if(list.isEmpty()) {
			return null;
		}
		
		if(list.size() > 1) {
			logger.error("To many obejct for find version in : " +  iValue + " / ["+ cmdSQL +"]");
			return null;
		}

		return (T) list.get(0);

	}
	*/


	
	/*
	 * (non-Javadoc)
	 * @see com.jcdecaux.itasset.model.domain.BaseAbstract#delete()
	 */
	public boolean delete() {
		
		if(this.disable()) {
			this.reload();
			
			this.getOrientVertex().remove();
			this.commit();
			
			return true;
		}
		
		return false;
	}

	public static final String ICON_NAME_PREFIX = "icon:";



	


}
