
package org.ebaloo.itkeeps.core.domain.vertex;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jBaseStandard;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;


@DatabaseVertrex()
public abstract class vBaseStandard extends vBase {

	private static Logger logger = LoggerFactory.getLogger(vBaseStandard.class);

	
	public vBaseStandard() {
		super();
	}
	
	public vBaseStandard(
			final String name
			) {
		super(name);
		
	}


	protected vBaseStandard(final jBaseStandard j) {
		super(j);

		try {
		this.updateBaseStandard(j);
	} catch (Exception e) {
		this.delete();
		throw e;
	}

	}

	
	
	
	/*
	 *   EXTERNAL_REF
	 */
	

	@DatabaseProperty(name = jBaseStandard.EXTERNAL_REF, type = OType.EMBEDDEDMAP)
	protected Map<String, String> getExternalRef()  {
		return this.getMapString(jBaseStandard.EXTERNAL_REF);
	}

	protected String getExternalRefValue(String key) {
		return getExternalRef().get(key);
	}
	
	
	protected void setExternalRef( Map<String, String> map) {
		this.setMapString(jBaseStandard.EXTERNAL_REF, map);
	}

	
	
	/*
	public static <T extends BaseAbstract> T getByExternalRef(final ModelClass<T> target,
			final String key, final String value) {
		return getByExternalRef(target, key, value, false);
	}
	*/
	
	
	
	protected static <T extends vBaseAbstract> T getByExternalRef(OrientBaseGraph graph, final Class<T> target,
			final String key, final String value, final boolean instanceOf) {

		
		String cmdSql = "SELECT FROM " + target.getSimpleName() + " WHERE "
				+ WhereClause.IsntanceOf(target, instanceOf) + " AND "
				+ jBaseStandard.EXTERNAL_REF + "['"+ key + "'] = ?";

		List<OrientVertex> list = vCommon.command(graph, cmdSql, value);

		if (list.isEmpty()) {
			if (logger.isTraceEnabled())
				logger.trace("OrientVertex not found for [k,v]: " + key + "," + value);
			return null;
		}

		if (list.size() > 1) {
			throw new RuntimeException("To many obejct for [k,v]: " + key + "," + value + " [" + cmdSql + "]");
		}

		
		OrientVertex ov = list.get(0);
		T baseAbstract = vBaseAbstract.getInstance(target, ov);

		if (logger.isTraceEnabled())
			logger.trace("OrientVertex found for [k,v]: " + key + "," + value + " @" + ov.getType().getName() + " #"
					+ ov.getIdentity().toString());

		return baseAbstract;
	}
	
	
	/*
	 * OTHER_NAME 
	 */


	protected static String stripOtherName(String value) {
		
		value = StringUtils.strip(value);
		value = value.toLowerCase();
		value = StringUtils.stripAccents(value);
		
		return value;
	}


	@DatabaseProperty(
			name = jBaseStandard.OTHER_NAME, 
			type = OType.EMBEDDEDLIST
			)
	protected final List<String> getOtherName() {
		return this.getListString(jBaseStandard.OTHER_NAME);
	}

	protected final void setOtherName(List<String> list) {
		
		if(list == null)
			list = new ArrayList<>();
		
		list = list.stream().map(vBaseStandard::stripOtherName).collect(Collectors.toList());
		
		this.setListString(jBaseStandard.OTHER_NAME, list);
	}

	
	
	
	protected static <T extends vBaseAbstract> T getByOtherName(OrientBaseGraph graph, Class<T> target, final String iValue) {
		return getByOtherName(graph, target, iValue, false);
	}

	protected static <T extends vBaseAbstract> T getByOtherName(OrientBaseGraph graph, Class<T> target, final String iValue, final boolean isInstanceOf) {
		
		if(StringUtils.isBlank(iValue)) {
			return null;
		}
		
		String value = stripOtherName(iValue);
		
		
		// TODO Injection SQL
		
		String cmdSQL = "";
		cmdSQL += "SELECT FROM " + target.getSimpleName() + " WHERE @rid in ";
		cmdSQL += "  (SELECT myRid FROM ";
		cmdSQL += "    (SELECT @rid as myRid, " + jBaseStandard.OTHER_NAME + " FROM " + target.getSimpleName() + "";
		cmdSQL += "    WHERE " + jBaseStandard.OTHER_NAME + " IS NOT NULL ";
		cmdSQL +=        "AND "; 
		cmdSQL +=      WhereClause.IsntanceOf(target, isInstanceOf);
		cmdSQL += "    UNWIND " + jBaseStandard.OTHER_NAME + " ) ";
		cmdSQL += "  WHERE " + jBaseStandard.OTHER_NAME + ".toLowerCase() = '" + value + "') ";
		
		//BaseQuery bq = new BaseQuery();
		List<T> list = vBaseAbstract.commandBaseAbstract(graph, target, cmdSQL);
        
		if(list.isEmpty()) {
			return null;
		}
		
		if(list.size() > 1) {
			logger.error("To many obejct for find version in : " +  iValue + " / ["+ cmdSQL +"]");
			return null;
		}

		return list.get(0);

	}
	
	/*
	 * Find
	 */

	/*
	public static <T extends BaseAbstract> T findByGuidOrName(OrientBaseGraph graph, final ModelClass<T> target, final String tofinde) {
		return findByGuidOrName(graph, target, tofinde, false);
	}

	
	@SuppressWarnings({ "unchecked"})
	public static <T extends BaseAbstract> T findByGuidOrName(OrientBaseGraph graph, final ModelClass<T> target, final String tofinde, final boolean isntanceof) {
		
		T retTarget = null;
		
		if(Guid.isGuid(tofinde)) {
			
			BaseAbstract ab = BaseAbstract.get(graph, target, new Guid(tofinde), isntanceof);

			if(ab == null) {
				return null;
			}

			if(!(isntanceof && target.isInstance(ab))) {
				throw new RuntimeException("the '" + JBase.GUID + "' ("+tofinde+") is not instance @" + target.getSimpleName());
			}

			if(!isntanceof && !ab.getType().equals(target.getSimpleName())) {
				throw new RuntimeException("the '" + JBase.GUID + "' ("+tofinde+") is not @" + target.getSimpleName());
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
	*/


	
	@DatabaseProperty(name = jBaseStandard.ICON)
	protected String getIcon() {
		
		String value = this.getProperty(jBaseStandard.ICON);
		
		if(Rid.is(value)) {
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
	
	protected void setIcon(String value) {
		this.setProperty(jBaseStandard.ICON, value);
	}



	// API
	
	protected <T extends jBaseStandard> void readBaseStandard(jBaseStandard j) {

		this.readBase(j);
		
		j.setIcon(this.getIcon());
		j.setOtherName(this.getOtherName());
		j.setExternalRef(this.getExternalRef());
		
	}

	/*
	public static <T extends jBase> T create(T j, Guid requesteurGuid) {
		throw new RuntimeException("Base is Abstract");
	}
	*/

	
	protected void updateBaseStandard(jBaseStandard j) {
		
		this.updateBase(j);

		if(j.isPresentExternalRef())
			this.setExternalRef(j.getExternalRef());
		
		if(j.isPresentOtherName())
			this.setOtherName(j.getOtherName());

		if(j.isPresentIcon()) {
			this.setIcon(j.getIcon());
		}
		
	}


}



