

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.GuidFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;

import com.orientechnologies.orient.core.metadata.schema.OType;

/**
 * 
 *
 */
@DatabaseVertrex(isAbstract = true)
public class vBase extends vBaseAbstract {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(vBase.class);



	protected vBase() {
	}

	/*
	protected Base(final BaseAbstract abase) {
		super(abase);
	}
	*/

	protected vBase(final String name) {
		super(true);

		this.defaultSetting(GuidFactory.getGuid());

		if(StringUtils.isNoneBlank(name)) {
			this.setName(name);
		}

	}

	

	
	
	private void defaultSetting(Guid guid) {
		this.setProperty(jBase.GUID, guid.toString());
		this.setProperty(jBase.CREATION_DATE, DateTime.now(DateTimeZone.UTC).toDate());
		this.setProperty(jBase.NAME, guid.toString());		
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
	 * CREATION_DATE
	 */
	
	@DatabaseProperty(name = jBase.CREATION_DATE, isReadOnly = true, type = OType.DATETIME)
	public final DateTime getCreationDate() {
		
		Date value = (Date) this.getProperty(jBase.CREATION_DATE);

		if (value == null) {
			return null;
		}

		return new DateTime(value);
	}



	
	

	/*
	 * DESCRIPTION
	 */

	
	@DatabaseProperty(name = jBase.DESCRIPTION)
	public final String getDescription() {
		return this.getProperty(jBase.DESCRIPTION);
	}

	public final void setDescription(final String value) {
		this.setProperty(jBase.DESCRIPTION, value);
	}

	
	

	
	/*
	 * GetByName
	 */

	/*
	public static <T extends BaseAbstract> T getByName(OrientBaseGraph graph, final ModelClass<T> target,
			final String name) {
		return getByName(graph, target, name, false);
	}
	*/

	/*
	@SuppressWarnings("unchecked")
	public static <T extends BaseAbstract> T getByName(OrientBaseGraph graph, final ModelClass<T> target,
			final String name, boolean isInstanceof) {

		String cmdSql = "SELECT FROM " + target.getClassName() + " WHERE " + BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE + " AND " + BaseUtils.WhereClause.classIsntanceOf(target, isInstanceof) + " AND " + JBase.NAME
				+ ".toLowerCase() = ?";

		List<OrientVertex> list = CommonOrientVertex.command(graph, cmdSql, name.toLowerCase());

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
	*/





	
	
	




	
	
	/*
	 * 
	 */
	

	/*
	public final static <T extends BaseAbstract> List<T> getAllBase(OrientBaseGraph graph, final ModelClass<T> target) {
		return getAllBase(graph, target, true);
	}
	*/
	
	public final static <T extends vBaseAbstract> List<T> getAllBase(OrientBaseGraph graph, final Class<T> target, final boolean isInstanceof) {

		StringBuilder request = new StringBuilder();
		
		request.append("SELECT FROM ");
		request.append(target.getSimpleName());
		request.append(" WHERE ");
		request.append(WhereClause.IsntanceOf(target, isInstanceof));

		//BaseQuery bq = new BaseQuery();
        return vBaseAbstract.commandBaseAbstract(graph, target, request.toString());
        
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


	


	public static final String ICON_NAME_PREFIX = "icon:";


	
	
	/*
	public static <T extends BaseAbstract> T getByGuid(OrientBaseGraph graph, final ModelClass<T> target,
			final Guid guid) {

		
		String cmdSql = "SELECT FROM " + target.getClassName() + " WHERE "
				+ BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE + " AND " + JBase.GUID  + " = ?";

		List<OrientVertex> list = CommonOrientVertex.command(graph, cmdSql, guid.toString());

		if (list.isEmpty()) {
			if (logger.isTraceEnabled())
				logger.trace("OrientVertex not found for 'guid': " + guid);
			return null;
		}

		if (list.size() > 1) {
			// TODO
			throw new RuntimeException("To many obejct for for 'guid': " + guid + " [" + cmdSql + "]");
		}

		OrientVertex ov = list.get(0);

		@SuppressWarnings("unchecked")
		ModelClass<T> modelClass = (ModelClass<T>) ModelFactory.get(ov.getType().getName());

		T baseAbstract;
		try {
			baseAbstract = modelClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		baseAbstract.setOrientVertex(ov);

		if (logger.isTraceEnabled())
			logger.trace("OrientVertex found for for 'guid': " + guid + " @" + ov.getType().getName() + " "
					+ ov.getIdentity().toString());

		return baseAbstract;
	}
	*/
	
	
    public static jBaseLight getJBaseLight (final vBase base)  
    {
    	if(base == null)
    		return null;
    	
    	jBaseLight j = new jBaseLight();
    	
    	j.setGuid(base.getGuid());
    	j.setName(base.getName());
    	j.setType(base.getType());
    	j.setVersion(base.getObjectVersion());

    	return j;
	}

	
	

	// API Methose
    
	protected vBase(final jBase j) {
		super(true);


		try {
			this.setProperty(jBase.GUID, GuidFactory.getGuid());
			this.setProperty(jBase.CREATION_DATE, DateTime.now(DateTimeZone.UTC).toDate());
	
			this.updateBase(j);
		} catch (Exception e) {
			this.delete();
			throw e;
		}
	}
	
	/*
	public static <T extends jBase> T create(T j, Guid requesteurGuid) {
		throw new RuntimeException("Base is Abstract");
	}
    */

	protected <T extends jBase> void readBase(T j) {
		
		j.getJObject().setType(this.getType());
		j.getJObject().setVersion(this.getObjectVersion());
		j.getJObject().setCreationDate(this.getCreationDate());

		j.setGuid(this.getGuid());
		j.setName(this.getName());
		j.setDescription(this.getDescription());
		
	}
	
	
	protected final <T extends jBase> void checkVersion(T j) {
		if(!j.isPresentJObject())
			throw new RuntimeException("json not have '" + jBase._OBJECT + "' seting");
		if(!j.getJObject().isPresentVersion())
			throw new RuntimeException("json not have '" + jBase._OBJECT + "." + jBase.VERSION + "' seting");
		if(j.getJObject().getVersion() != this.getObjectVersion()) 
			throw new RuntimeException("json '" + jBase._OBJECT + "." + jBase.VERSION + " is differtne with 'this'");
	}
	
	
	protected <T extends jBase> void updateBase(T j) {
		
		if(j.isPresentName())
			this.setName(j.getName());

		if(j.isPresentDescription())
			this.setDescription(j.getDescription());
		
	}

	
	
	
	
	
	
	
	
	
	/*
	 * GUID 
	 */
	
	private Guid guid = null; 

	
	@DatabaseProperty(name = jBase.GUID, isNotNull = true, isReadOnly = true)
	public final Guid getGuid() {
		
		if(this.guid == null) {
			guid = new Guid(this.getProperty(jBase.GUID).toString());
		}
		
		return guid;
	}


	/*
	 * NAME
	 */
	
	private String name = null;
	
	@DatabaseProperty(name = jBase.NAME, isNotNull = true)
	public String getName() {
		
		if(this.name == null) {
			this.name = this.getProperty(jBase.NAME);
		}
		
		return this.name;
	}

	protected void setName(String value) {
		
		this.name = value;
		this.setProperty(jBase.NAME, this.name);
		
	}



	/*
	 * DISABLE
	 */


	

	
	public boolean hasGuid() {
		
		if(guid != null) {
			return true;
		}
		
		Object obj = this.getProperty(jBase.GUID);
		
		if(obj == null) {
			return false;
		}
		
		return Guid.isGuid(this.getProperty(jBase.GUID).toString());
		
	}
	

	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(this.getType());
			sb.append("/");
			sb.append(this.getGuid());
			sb.append("/");
			sb.append(this.getName());
		} catch ( Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
}
