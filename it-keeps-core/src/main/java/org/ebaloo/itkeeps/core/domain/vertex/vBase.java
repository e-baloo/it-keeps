

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

import com.tinkerpop.blueprints.Direction;
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

	
	protected vBase(final jBase j, final boolean f) {
		super(true);

		this.setProperty(jBase.GUID, GuidFactory.getGuid());

		if(j.isPresentName())
			this.setName(j.getName());

		if(j.isPresentDescription())
			this.setDescription(j.getDescription());

		this.setProperty(jBase.CREATION_DATE, DateTime.now(DateTimeZone.UTC).toDate());
		
		if(f)
			this.setEnable(Boolean.TRUE);
	}
	
	
	private void defaultSetting(Guid guid) {

		this.setProperty(jBase.GUID, guid.toString());

		this.setProperty(jBase.CREATION_DATE, DateTime.now(DateTimeZone.UTC).toDate());
		this.setProperty(jBase.ENABLE, true);
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
		
		request.append("SELECT FROM " + target.getSimpleName() + " ");
		request.append("WHERE ");
		request.append(WhereClause.ENABLE_IS_TRUE);
		request.append(" AND ");
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
	
	
    
    
    
    
	public <T extends jBase> T read(T j, Guid requesteurGuid) {
		
		j.getJObject().setType(this.getType());
		j.getJObject().setVersion(this.getObjectVersion());
		j.getJObject().setEnable(this.isEnable());
		j.getJObject().setCreationDate(this.getCreationDate());

		j.setGuid(this.getGuid());
		j.setName(this.getName());
		j.setDescription(this.getDescription());
		
		return null;
	}
	
	
	public <T extends jBase> T update(T obj, Guid requesteurGuid) {
		
		if(!obj.isPresentJObject())
			throw new RuntimeException(); //TODO

		if(!obj.getJObject().isPresentVersion())
			throw new RuntimeException(); //TODO

		if(obj.getJObject().getVersion() != this.getObjectVersion()) 
			throw new RuntimeException(); //TODO
		
		if(obj.isPresentName())
			this.setName(obj.getName());

		if(obj.isPresentDescription())
			this.setDescription(obj.getDescription());
		
		return null;

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
	 * ENABLE
	 */

	
	@DatabaseProperty(name = jBase.ENABLE, type = OType.BOOLEAN)
	protected void setEnable(Boolean enable) {
		
		this.reload();
		this.setProperty(jBase.ENABLE, enable);
	}

	public Boolean isEnable() {
		return (Boolean) this.getProperty(jBase.ENABLE);
	}

	/*
	 * DISABLE
	 */

	public boolean disable() {
		
		this.deleteAllEdges(Direction.IN);
		this.deleteAllEdges(Direction.OUT);
		this.setProperty(jBase.ENABLE, false);
		this.commit();
		return true;
	}

	public int compareTo(vBase obj) {
		if(obj == null) {
			return 0;
		}

		if(StringUtils.isBlank(obj.getName())) {
			return 0;
		}
		
		if(StringUtils.isBlank(this.getName())) {
			return 0;
		}

		// Tri en fonction du Name //
		return this.getName().compareTo(((vBase)obj).getName());
	}
	

	
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
