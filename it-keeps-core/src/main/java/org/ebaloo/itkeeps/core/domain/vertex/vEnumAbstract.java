
package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.metadata.schema.OType;
import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.api.enumeration.enAbstract;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 * 
 *
 */


@SuppressWarnings({ "rawtypes", "unchecked" })
@DatabaseVertrex()
abstract class vEnumAbstract<K extends enAbstract> extends vBaseAbstract {

	private static Logger logger = LoggerFactory.getLogger(vEnumAbstract.class);
	
	
	private static final HashMap<String, vEnumAbstract> map = new HashMap<String, vEnumAbstract>();
	
   

	public static final <T extends vEnumAbstract> T get(final OrientBaseGraph graph, final Class<T> target, final String name)  
	{
    	if(StringUtils.isEmpty(name))
    		return null;
		
    	
    	if(!map.containsKey(name)) {
    	
	    	
			String cmdSQL = "SELECT FROM " + target.getSimpleName() + " WHERE (" + NAME + "= ?)";
			
			List<OrientVertex> list = GraphFactory.command(graph, cmdSQL, name);
					
			if(list.size() == 0) {
				return null;
			}
			
			if(list.size() > 1) {
				throw new RuntimeException(String.format("name[%s] is not unique in class @%s", name, target.getSimpleName()));
			}
			
			T value = vEnumAbstract.getInstance(target, list.get(0));
			
			map.put(name, value);
			
    	}
		
		return (T) map.get(name);
	}
	
	
	
	protected vEnumAbstract() {
		;
	}
	
	


	
	


		/*
		 * 
		 */
		



	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(this.getType());
			sb.append("/");
			sb.append(this.getName());
		} catch ( Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}


	/*
	 * NAME
	 */
	
	public static final String NAME = "name";
	
	private String name = null;
	
	@DatabaseProperty(name = NAME, isNotNull = true)
	public String getName() {
		
		if(this.name == null) {
			this.name = this.getProperty(NAME);
		}
		
		return this.name;
	}

	protected void setName(String value) {
		
		this.name = value;
		this.setProperty(NAME, this.name);
		
	}

	
	/*
	 * ORDINAL 
	 */
	
	public static final String ORDINAL = "ordinal";
	
	@DatabaseProperty(name=ORDINAL, isNotNull = true, type = OType.INTEGER)
	public Integer getOrdinale()  {
		return this.getProperty(ORDINAL);
	}
	
	protected void setOrdinale(Integer value) {
		this.setProperty(ORDINAL, value);
	}
	

	/*
	 * VALUE 
	 */
	
	public static final String VALUE = "value";
	
	@DatabaseProperty(name=VALUE, isNotNull = true)
	public String getBalue()  {
		return this.getProperty(VALUE);
	}
	
	protected void setValue(String value) {
		this.setProperty(VALUE, value);
	}

	/*
	 * 
	 */
	
	
	/*
	public final static <T extends vEnumAbstract> T getInstance(final Class<T> target, final OrientVertex ov) {
		
		try {
			
			T enumAbstractClass = (T) target.newInstance();
			enumAbstractClass.setOrientVertex(ov);
			
			return enumAbstractClass;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
*/
	
	public static <T extends enAbstract, U extends vEnumAbstract> void init(Class<T> targetEnum, Class<U> targetClass) {

		logger.debug("init()");
		
		try {
		
		for(enAbstract value : (Collection<enAbstract<?>>) targetEnum.getMethod("values").invoke(null)) {
			
			logger.debug("init() value = " + value.toString());
			
			
			U eac = get((OrientBaseGraph) null, targetClass, value.name());
			
			if(eac == null) {
				eac = targetClass.newInstance();
				eac.newOrientVertex();
			}
			
			//eac.setOrientVertex(eac);
			eac.setName(value.name());
			eac.setOrdinale(value.ordinal());
			eac.setValue(String.format("%s", value.value()));
			eac.setEnable(true);
			
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}

