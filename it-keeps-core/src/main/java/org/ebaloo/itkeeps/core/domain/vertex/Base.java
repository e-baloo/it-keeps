

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.api.model.JBaseLight;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.BaseUtils;
import org.ebaloo.itkeeps.core.domain.GuidFactory;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
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



	protected Base() {
	}

	protected Base(final BaseAbstract abase) {
		super(abase);
	}

	protected Base(final String name) {
		super(true);

		this.defaultSetting(GuidFactory.getGuid());

		if(StringUtils.isNoneBlank(name)) {
			this.setName(name);
		}

	}

	
	protected Base(final JBase j, final boolean f) {
		super(true);

		this.setProperty(JBase.GUID, GuidFactory.getGuid());

		if(j.isPresentName())
			this.setName(j.getName());

		if(j.isPresentDescription())
			this.setDescription(j.getDescription());

		this.setProperty(JBase.CREATION_DATE, DateTime.now(DateTimeZone.UTC).toDate());
		
		if(f)
			this.setEnable(Boolean.TRUE);
	}
	
	
	private void defaultSetting(Guid guid) {

		this.setProperty(JBase.GUID, guid.toString());

		this.setProperty(JBase.CREATION_DATE, DateTime.now(DateTimeZone.UTC).toDate());
		this.setProperty(JBase.ENABLE, true);
		this.setProperty(JBase.NAME, guid.toString());		
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
	
	@DatabaseProperty(name = JBase.CREATION_DATE, isReadOnly = true, type = OType.DATETIME)
	public final DateTime getCreationDate() {
		
		Date value = (Date) this.getProperty(JBase.CREATION_DATE);

		if (value == null) {
			return null;
		}

		return new DateTime(value);
	}



	
	

	/*
	 * DESCRIPTION
	 */

	
	@DatabaseProperty(name = JBase.DESCRIPTION)
	public final String getDescription() {
		return this.getProperty(JBase.DESCRIPTION);
	}

	public final void setDescription(final String value) {
		this.setProperty(JBase.DESCRIPTION, value);
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
	
	public final static <T extends BaseAbstract> List<T> getAllBase(OrientBaseGraph graph, final ModelClass<T> target, final boolean isInstanceof) {

		StringBuilder request = new StringBuilder();
		
		request.append("SELECT FROM " + target.getClassName() + " ");
		request.append("WHERE ");
		request.append(BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE);
		request.append(" AND ");
		BaseUtils.WhereClause.classIsntanceOf(target, isInstanceof, request);

		//BaseQuery bq = new BaseQuery();
        return BaseAbstract.commandBaseAbstract(graph, target, request.toString());
        
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
	
	
    public static JBaseLight getJBaseLight (final BaseAbstract ba)  
    {
    	if(ba == null)
    		return null;
    	
    	JBaseLight j = new JBaseLight();
    	
    	j.setGuid(ba.getGuid());
    	j.setName(ba.getName());
    	j.setType(ba.getType());
    	j.setVersion(ba.getObjectVersion());

    	return j;
	}

	
	

	// API Methose
	
	
	public <T extends JBase> T apiFill(T j, Guid requesteurGuid) {
		
		j.getJObject().setType(this.getType());
		j.getJObject().setVersion(this.getObjectVersion());
		j.getJObject().setEnable(this.isEnable());
		j.getJObject().setCreationDate(this.getCreationDate());

		j.setGuid(this.getGuid());
		j.setName(this.getName());
		j.setDescription(this.getDescription());
		
		return j;
	}
	
	
	public <T extends JBase> void apiUpdate(T obj, Guid requesteurGuid) {
		
		if(!obj.isPresentJObject())
			throw new RuntimeException(); //TODO
		

		if(!obj.getJObject().isPresentVersion()) {
			throw new RuntimeException(); //TODO
		}

		if(obj.getJObject().getVersion() != this.getObjectVersion()) {
			throw new RuntimeException(); //TODO
		}
		
		if(obj.isPresentName())
			this.setName(obj.getName());

		if(obj.isPresentDescription())
			this.setDescription(obj.getDescription());

	}

	


}
