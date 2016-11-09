
package org.ebaloo.itkeeps.domain;


import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.domain.annotation.ModelPropertyAnnotation;
import org.ebaloo.itkeeps.domain.annotation.ModelPropertyAnnotation.TypeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;


@ModelClassAnnotation(
		name = "BaseStandard",
		isAbstract = true)
@DatabaseVertrex()
public class BaseStandard extends Base {

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

	
	
	/*
	 *   EXTERNAL_REF
	 */
	
	public static final String EXTERNAL_REF = "externealRef";

	@DatabaseProperty(name = EXTERNAL_REF, type = OType.EMBEDDEDMAP)
	@ModelPropertyAnnotation(name = EXTERNAL_REF, type = TypeProperty.GET)
	public Map<String, String> getExternalRef()  {
		return this.getEmbeddedMapString(EXTERNAL_REF);
	}

	public String getExternalRefValue(String key) {
		return getExternalRef().get(key);
	}

	
	@ModelPropertyAnnotation(name = EXTERNAL_REF, type = TypeProperty.ADD)
	public void addExternalRef(final String key, final String value) {
		this.addEmbeddedMapString(EXTERNAL_REF, key, value);
	}

	@ModelPropertyAnnotation(name = EXTERNAL_REF, type = TypeProperty.REMOVE)
	public void removeExternalRef(final String key) {
		this.removeEmbeddedMapString(EXTERNAL_REF, key);
	}
	
	
	public static <T extends BaseAbstract> T getByExternalRef(final Class<T> target,
			final String key, final String value) {
		return getByExternalRef(target, key, value, false);
	}
	
	public static <T extends BaseAbstract> T getByExternalRef(final Class<T> target,
			final String key, final String value, final boolean instanceOf) {

		
		String cmdSql = "SELECT FROM " + target.getSimpleName() + " WHERE "
				+ BaseUtils.WhereClause.enable() + " AND "
				+ BaseUtils.WhereClause.classIsntanceOf(target.getSimpleName(), instanceOf) + " AND "
				+ BaseStandard.EXTERNAL_REF + "['"+ key + "'] = ?";

		List<OrientVertex> list = CommonOrientVertex.command(cmdSql, value);

		if (list.isEmpty()) {
			if (logger.isTraceEnabled())
				logger.trace("OrientVertex not found for [k,v]: " + key + "," + value);
			return null;
		}

		if (list.size() > 1) {
			// TODO
			throw new RuntimeException(new Exception ("To many obejct for [k,v]: " + key + "," + value + " [" + cmdSql + "]"));
		}

		OrientVertex ov = list.get(0);

//		ModelClass<T> modelClass = (ModelClass<T>) ModelFactory.get(ov.getType().getName());

		T baseAbstract;
		try {
			baseAbstract = target.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		baseAbstract.setOrientVertex(ov);

		if (logger.isTraceEnabled())
			logger.trace("OrientVertex found for [k,v]: " + key + "," + value + " @" + ov.getType().getName() + " #"
					+ ov.getIdentity().toString());

		return baseAbstract;
	}
	
	
	/*
	@Deprecated
	public static <T extends BaseAbstract> T getByExternalRef(final Class<T> target,
															  final String key, final String value) {
		return getByExternalRef(ModelFactory.get(target), key, value);
	}
	*/

	
	/*
	 * OTHER_NAME 
	 */


	public static final String stripOtherName(String value) {
		
		value = StringUtils.strip(value);
		value = value.toLowerCase();
		value = StringUtils.stripAccents(value);
		
		return value;
	}

	public static final String OTHER_NAME = "otherName";

	@DatabaseProperty(
			name = OTHER_NAME, 
			type = OType.EMBEDDEDLIST
			)
	@ModelPropertyAnnotation(name = OTHER_NAME, type = TypeProperty.GET) 
	public final List<String> getOtherName() {
		return this.getEmbeddedListString(OTHER_NAME);
	}

	@ModelPropertyAnnotation(name = OTHER_NAME, type = TypeProperty.ADD)
	public final void addOtherName(String value) {
		
		if(StringUtils.isBlank(value)) {
			return;
		}
		
		this.addEmbeddedListString(OTHER_NAME, stripOtherName(value));
	}

	@ModelPropertyAnnotation(name = OTHER_NAME, type = TypeProperty.REMOVE)
	public final void removeOtherName(String value) {

		if(StringUtils.isBlank(value)) {
			return;
		}

		this.removeEmbeddedListString(OTHER_NAME, value);
	}
	
	/*
	@Deprecated
	public static final <T extends BaseAbstract> T getByOtherName(Class<T> target, final String iValue) {
		return getByOtherName(target, iValue, false);
	}
	*/

	public static final <T extends BaseAbstract> T getByOtherName(Class<T> target, final String iValue) {
		return getByOtherName(target, iValue, false);
	}

	/*
	@Deprecated
	public static final <T extends BaseAbstract> T getByOtherName(Class<T> target, final String iValue, final boolean isntanceof) {
		return getByOtherName(ModelFactory.get(target), iValue, isntanceof);
	}
	*/

	@SuppressWarnings("unchecked")
	public static final <T extends BaseAbstract> T getByOtherName(Class<T> target, final String iValue, final boolean isInstanceOf) {
		
		if(StringUtils.isBlank(iValue)) {
			return null;
		}
		
		String value = stripOtherName(iValue);
		
		
		// TODO Injection SQL
		
		String cmdSQL = "";
		cmdSQL += "SELECT FROM " + target.getSimpleName()+ " WHERE @rid in ";
		cmdSQL += "  (SELECT myRid FROM ";
		cmdSQL += "    (SELECT @rid as myRid, " + BaseStandard.OTHER_NAME + " FROM " + target.getSimpleName() + "";
		cmdSQL += "    WHERE " + BaseStandard.OTHER_NAME + " IS NOT NULL ";
		cmdSQL +=        "AND "; 
		cmdSQL +=      BaseUtils.WhereClause.enable();
		cmdSQL +=        "AND "; 
		cmdSQL +=      BaseUtils.WhereClause.classIsntanceOf(target, isInstanceOf);
		cmdSQL += "    UNWIND " + BaseStandard.OTHER_NAME + " ) ";
		cmdSQL += "  WHERE " + BaseStandard.OTHER_NAME + ".toLowerCase() = '" + value + "') ";
		
		BaseQuery bq = new BaseQuery();
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
	
	/*
	 * Find
	 */

	public static <T extends BaseAbstract> T findByGuidOrName(final Class<T> target, final String tofinde) {
		return findByGuidOrName(target, tofinde, false);
	}

	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static <T extends BaseAbstract> T findByGuidOrName(final Class<T> target, final String tofinde, final boolean isntanceof) {
		
		T retTarget = null;
		
		if(Guid.isGuid(tofinde)) {
			
			BaseAbstract ab = BaseAbstract.getBaseAbstract(new Guid(tofinde));

			if(ab == null) {
				return null;
			}

			if(!(isntanceof && target.isInstance(ab))) {
				throw new RuntimeException("the 'guid' ("+tofinde+") is not instance @" + target.getSimpleName());
			}

			if(!isntanceof && !ab.getType().equals(target.getSimpleName())) {
				throw new RuntimeException("the 'guid' ("+tofinde+") is not @" + target.getSimpleName());
			}

			retTarget = (T) ab;
			
			return retTarget;
		}

		retTarget = Base.getByName(target, tofinde, isntanceof);
		if(retTarget != null) {
			return retTarget;
		}
		
		retTarget = BaseStandard.getByOtherName(target, tofinde, isntanceof);
		if(retTarget != null) {
			return retTarget;
		}
		
		return null;

	}
	


	
}



