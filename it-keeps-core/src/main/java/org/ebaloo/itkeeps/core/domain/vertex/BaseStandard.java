
package org.ebaloo.itkeeps.core.domain.vertex;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.api.model.JBaseStandard;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.BaseUtils;
import org.ebaloo.itkeeps.core.domain.ModelFactory.ModelClass;
import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;


@ModelClassAnnotation(
		isAbstract = true)
@DatabaseVertrex()
public abstract class BaseStandard extends Base {

	private static Logger logger = LoggerFactory.getLogger(BaseStandard.class);

	
	public BaseStandard() {
		super();
	}
	
	public BaseStandard(final BaseAbstract abase) {
		super(abase);
	}

	public BaseStandard(
			final String name
			) {
		super(name);
		
	}


	protected BaseStandard(final JBaseStandard j, final boolean f) {
		super(j, f);
		
		if(j.isPresentExternalRef())
			this.setExternalRef(j.getExternalRef());
		
		if(j.isPresentOtherName())
			this.setOtherName(j.getOtherName());

		if(j.isPresentIcon()) {
			this.setIcon(j.getIcon());
		}

		if(f)
			this.setEnable(Boolean.TRUE);
	}

	
	/*
	 *   EXTERNAL_REF
	 */
	

	@DatabaseProperty(name = JBaseStandard.EXTERNAL_REF, type = OType.EMBEDDEDMAP)
	public Map<String, String> getExternalRef()  {
		return this.getMapString(JBaseStandard.EXTERNAL_REF);
	}

	public String getExternalRefValue(String key) {
		return getExternalRef().get(key);
	}
	
	
	public void setExternalRef( Map<String, String> map) {
		this.setMapString(JBaseStandard.EXTERNAL_REF, map);
	}

	
	
	/*
	public static <T extends BaseAbstract> T getByExternalRef(final ModelClass<T> target,
			final String key, final String value) {
		return getByExternalRef(target, key, value, false);
	}
	*/
	
	public static <T extends BaseAbstract> T getByExternalRef(OrientBaseGraph graph, final ModelClass<T> target,
			final String key, final String value, final boolean instanceOf) {

		
		String cmdSql = "SELECT FROM " + target.getClassName() + " WHERE "
				+ BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE + " AND "
				+ BaseUtils.WhereClause.classIsntanceOf(target.getClassName(), instanceOf) + " AND "
				+ JBaseStandard.EXTERNAL_REF + "['"+ key + "'] = ?";

		List<OrientVertex> list = CommonOrientVertex.command(graph, cmdSql, value);

		if (list.isEmpty()) {
			if (logger.isTraceEnabled())
				logger.trace("OrientVertex not found for [k,v]: " + key + "," + value);
			return null;
		}

		if (list.size() > 1) {
			throw new RuntimeException("To many obejct for [k,v]: " + key + "," + value + " [" + cmdSql + "]");
		}

		
		OrientVertex ov = list.get(0);
		T baseAbstract = BaseAbstract.newInstance(target, ov);

		if (logger.isTraceEnabled())
			logger.trace("OrientVertex found for [k,v]: " + key + "," + value + " @" + ov.getType().getName() + " #"
					+ ov.getIdentity().toString());

		return baseAbstract;
	}
	
	
	/*
	 * OTHER_NAME 
	 */


	public static final String stripOtherName(String value) {
		
		value = StringUtils.strip(value);
		value = value.toLowerCase();
		value = StringUtils.stripAccents(value);
		
		return value;
	}


	@DatabaseProperty(
			name = JBaseStandard.OTHER_NAME, 
			type = OType.EMBEDDEDLIST
			)
	public final List<String> getOtherName() {
		return this.getListString(JBaseStandard.OTHER_NAME);
	}

	public final void setOtherName(List<String> list) {
		
		if(list == null)
			list = new ArrayList<String>();
		
		list = list.stream().map(e -> stripOtherName(e)).collect(Collectors.toList());
		
		this.setListString(JBaseStandard.OTHER_NAME, list);
	}

	
	
	
	public static final <T extends BaseAbstract> T getByOtherName(OrientBaseGraph graph, ModelClass<T> target, final String iValue) {
		return getByOtherName(graph, target, iValue, false);
	}

	public static final <T extends BaseAbstract> T getByOtherName(OrientBaseGraph graph, ModelClass<T> target, final String iValue, final boolean isInstanceOf) {
		
		if(StringUtils.isBlank(iValue)) {
			return null;
		}
		
		String value = stripOtherName(iValue);
		
		
		// TODO Injection SQL
		
		String cmdSQL = "";
		cmdSQL += "SELECT FROM " + target.getClassName() + " WHERE @rid in ";
		cmdSQL += "  (SELECT myRid FROM ";
		cmdSQL += "    (SELECT @rid as myRid, " + JBaseStandard.OTHER_NAME + " FROM " + target.getClassName() + "";
		cmdSQL += "    WHERE " + JBaseStandard.OTHER_NAME + " IS NOT NULL ";
		cmdSQL +=        "AND "; 
		cmdSQL +=      BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE;
		cmdSQL +=        "AND "; 
		cmdSQL +=      BaseUtils.WhereClause.classIsntanceOf(target, isInstanceOf);
		cmdSQL += "    UNWIND " + JBaseStandard.OTHER_NAME + " ) ";
		cmdSQL += "  WHERE " + JBaseStandard.OTHER_NAME + ".toLowerCase() = '" + value + "') ";
		
		//BaseQuery bq = new BaseQuery();
		List<T> list = BaseAbstract.commandBaseAbstract(graph, target, cmdSQL);
        
		if(list.isEmpty()) {
			return null;
		}
		
		if(list.size() > 1) {
			logger.error("To many obejct for find version in : " +  iValue + " / ["+ cmdSQL +"]");
			return null;
		}

		return (T) list.get(0);

	}
	
	/*
	 * Find
	 */

	public static <T extends BaseAbstract> T findByGuidOrName(OrientBaseGraph graph, final ModelClass<T> target, final String tofinde) {
		return findByGuidOrName(graph, target, tofinde, false);
	}

	
	@SuppressWarnings({ "unchecked"})
	public static <T extends BaseAbstract> T findByGuidOrName(OrientBaseGraph graph, final ModelClass<T> target, final String tofinde, final boolean isntanceof) {
		
		T retTarget = null;
		
		if(Guid.isGuid(tofinde)) {
			
			BaseAbstract ab = BaseAbstract.getBaseAbstract(graph, target, new Guid(tofinde));

			if(ab == null) {
				return null;
			}

			if(!(isntanceof && target.isInstance(ab))) {
				throw new RuntimeException("the '" + JBase.GUID + "' ("+tofinde+") is not instance @" + target.getClassName());
			}

			if(!isntanceof && !ab.getType().equals(target.getClassName())) {
				throw new RuntimeException("the '" + JBase.GUID + "' ("+tofinde+") is not @" + target.getClassName());
			}

			retTarget = (T) ab;
			
			return retTarget;
		}

		retTarget = Base.getByName(graph, target, tofinde, isntanceof);
		if(retTarget != null) {
			return retTarget;
		}
		
		retTarget = BaseStandard.getByOtherName(graph, target, tofinde, isntanceof);
		if(retTarget != null) {
			return retTarget;
		}
		
		return null;

	}
	


	
	@DatabaseProperty(name = JBaseStandard.ICON)
	public String getIcon() {
		
		String value = this.getProperty(JBaseStandard.ICON);
		
		if(Guid.isGuid(value)) {
			return value;
		}
		
		if(StringUtils.isBlank(value)) {
			return ICON_NAME_PREFIX + this.getClass().getSimpleName();
		} else {
			
			if(value.startsWith(ICON_NAME_PREFIX)) {
				return value;
			} else {
				return ICON_NAME_PREFIX + value;
			}
			
		}
	}
	
	public void setIcon(String value) {
		this.setProperty(JBaseStandard.ICON, value);
	}



	// API
	
	@Override
	public <T extends JBase> void apiFill(T j, Guid requesteurGuid) {
		
		if(!(j instanceof JBaseStandard))
			throw new RuntimeException("TODO"); //TODO
		
		super.apiFill(j, requesteurGuid);
		
		JBaseStandard jBaseStandard = (JBaseStandard) j;
		
		jBaseStandard.setIcon(this.getIcon());
		jBaseStandard.setOtherName(this.getOtherName());
		jBaseStandard.setExternalRef(this.getExternalRef());
		
	}
	
	
	@Override
	public <T extends JBase> void apiUpdate(T j, Guid requesteurGuid) {
		
		if(!(j instanceof JBaseStandard))
			throw new RuntimeException("TODO"); //TODO

		super.apiUpdate(j, requesteurGuid);

		JBaseStandard jBaseStandard = (JBaseStandard) j;

		if(jBaseStandard.isPresentIcon())
			this.setIcon(jBaseStandard.getIcon());

		if(jBaseStandard.isPresentExternalRef())
			this.setExternalRef(jBaseStandard.getExternalRef());

		if(jBaseStandard.isPresentOtherName())
			this.setOtherName(jBaseStandard.getOtherName());


	}


}



