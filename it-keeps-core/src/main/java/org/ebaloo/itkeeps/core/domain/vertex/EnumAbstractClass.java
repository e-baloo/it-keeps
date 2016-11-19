
package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.metadata.schema.OType;
import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.api.annotation.EnumAbstract;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

/**
 * 
 *
 */


@SuppressWarnings({ "rawtypes", "unchecked" })
@DatabaseVertrex()
public abstract class EnumAbstractClass<K extends EnumAbstract> extends CommonOrientVertex {

	private static Logger logger = LoggerFactory.getLogger(EnumAbstractClass.class);
	
	
	private static final HashMap<String, EnumAbstractClass> map = new HashMap<String, EnumAbstractClass>();
	
   

	public static final <T extends EnumAbstractClass> T get(final OrientBaseGraph graph, final Class<T> target, final String name)  
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
			
			T value = EnumAbstractClass.getInstance(target, list.get(0));
			
			map.put(name, value);
			
    	}
		
		return (T) map.get(name);
	}
	
	
	
	protected EnumAbstractClass() {
		;
	}
	
	
	@Override
    public final boolean equals(Object obj) {

		if(obj == null)
			return false;
			
		if(!(obj instanceof EnumAbstractClass))
			return false;
		
		return this.getORID().equals(((EnumAbstractClass)obj).getORID());
    }
		
	protected final String getType() {
		
		OrientVertexType type;
		try {
			type = this.getOrientVertex().getType();
		} catch (Exception e) {
			logger.trace("getType() is null -> 2nd chance! [" + e.getMessage() + "]");
			this.reload();
			type = this.getOrientVertex().getType();
		}
		
		
		return type.getName();
	}

	
	


		protected boolean isInstanceOf(Class<?> clazz) {
			return clazz.isInstance(this);
		}



		/*
		 * 
		 */
		

		protected void newOrientVertex() {

		if(!this.hasOrientVertex()) {
			OrientVertexType ovt = this.getVertexType();
	
			if (ovt == null) {
				throw new RuntimeException("The class \"" + this.getClass().getSimpleName() + "\" is note defined in the database");
			}
			
			this.setOrientVertex(this.getGraph().addVertex("class:" + ovt.getName()));
		}
			
	}



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
	
	
	public final static <T extends EnumAbstractClass> T getInstance(final Class<T> target, final OrientVertex ov) {
		
		try {
			
			T enumAbstractClass = (T) target.newInstance();
			enumAbstractClass.setOrientVertex(ov);
			
			return enumAbstractClass;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	
	public static <T extends EnumAbstract, U extends EnumAbstractClass> void init(Class<T> targetEnum, Class<U> targetClass) {

		logger.debug("init()");
		
		try {
		
		for(EnumAbstract value : (Collection<EnumAbstract<?>>) targetEnum.getMethod("values").invoke(null)) {
			
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
			
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}

