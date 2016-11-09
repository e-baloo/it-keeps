
package org.ebaloo.itkeeps.database;



import java.util.List;

import com.orientechnologies.common.log.OLogManager;
import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.cache.*;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import org.apache.commons.collections4.IteratorUtils;
import org.ebaloo.itkeeps.ConfigFactory;
import org.ebaloo.itkeeps.tools.MetricsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Timer;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public final class GraphFactory {
	
	
	private static Logger logger = LoggerFactory.getLogger(GraphFactory.class);

	static {
		
		System.setProperty("orientdb.installCustomFormatter", "true");
		
		OLogManager.instance().installCustomFormatter();
	}
	

	public static final String DATABASE_URI = "database.uri";
	public static final String DATABASE_USER = "database.user";
	public static final String DATABASE_PASSWORD = "database.password";
	public static final String DATABASE_POOL_MIN = "database.pool.min";
	public static final String DATABASE_POOL_MAX = "database.pool.max";
	//public static final String DATABASE_DEFAULT_TYPE = "database.defaultType";
	
	private static final String DEFAULT_DATABASE_URI = "memory:itkeeps";
	private static final String DEFAULT_DATABASE_USER = "admin";
	private static final String DEFAULT_DATABASE_PASSWORD = "admin"; 
	private static final int DEFAULT_DATABASE_POOL_MIN = 0;
	private static final int DEFAULT_DATABASE_POOL_MAX = 24;
	
	private static OrientGraphFactory singleton = null;

	
	public synchronized static OrientBaseGraph getOrientBaseGraph() {

        if (singleton == null) {
            singleton = getOrientGraphFactory();
        }

		return getOrientGraphNoTx();
	}
	
    public synchronized static OrientGraphNoTx getOrientGraphNoTx() {
    	
        if (singleton == null) {
            singleton = getOrientGraphFactory();
        }

        return singleton.getNoTx();
    }

    //private static String orientGraphType = OrientGraphNoTx.class.getSimpleName();

    
    private static OrientGraphFactory getOrientGraphFactory()  {
    	
		try {
			
			ConfigFactory.getString(DATABASE_URI, DEFAULT_DATABASE_URI);
			
    	
	    	String databaseUri = ConfigFactory.getString(DATABASE_URI, DEFAULT_DATABASE_URI); 
	    	String databaseUser = ConfigFactory.getString(DATABASE_USER, DEFAULT_DATABASE_USER);
	    	String databasePassword = ConfigFactory.getString(DATABASE_PASSWORD, DEFAULT_DATABASE_PASSWORD);

	    	int poolMin = ConfigFactory.getInt(DATABASE_POOL_MIN, DEFAULT_DATABASE_POOL_MIN);
	    	int poolMax = ConfigFactory.getInt(DATABASE_POOL_MAX, DEFAULT_DATABASE_POOL_MAX);

	    	
	//    	orientGraphType = config.getString(DATABASE_DEFAULT_TYPE, orientGraphType);
			if(Orient.instance().getLocalRecordCache() instanceof OLocalRecordCacheFactoryImpl) {
				((OLocalRecordCacheFactoryImpl) Orient.instance().getLocalRecordCache()).register(RecordCache.class.getName(), RecordCache.class);
				OGlobalConfiguration.CACHE_LOCAL_IMPL.setValue(RecordCache.class.getName());
			}
			return new OrientGraphFactory(databaseUri, databaseUser, databasePassword).setupPool(poolMin, poolMax);

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
    }
    
	public static final Timer TIMER_DATABASE_REQUEST_SQL = MetricsFactory.getMetricRegistry().timer("database.request.sql");

    
    
	public static List<OrientVertex> command(String cmdSQL, Object... args) {
		
		final Timer.Context timerContext = TIMER_DATABASE_REQUEST_SQL.time();
		
		try {
			long tStart = System.nanoTime();
			
			OrientBaseGraph graph = GraphFactory.getOrientBaseGraph();
			
			Iterable<OrientVertex>  iterable = graph.command(new OCommandSQL(cmdSQL)).execute(args);
			List<OrientVertex> list = IteratorUtils.toList(iterable.iterator());
	
	
			long elapsedSeconds = (System.nanoTime() - tStart);
			
			if(logger.isDebugEnabled()) {
				int size = list.size();
				StringBuilder message = new StringBuilder();
				
				message.append(String.format("Query executed in %d ms Returned %d record%s", elapsedSeconds, size, size > 1 ? "s" : ""));
				
				/*
				 try{
					throw new Exception();
				} catch(Exception e) {
					e.printStackTrace();
				}
				 */
				message.append(String.format(" [%s]", cmdSQL));
				if(args != null) {
					message.append(" {");
					for(Object arg : args) {
						message.append(arg.toString());
						message.append(", ");
					}
					message.delete(message.length() - 2, message.length());
					message.append("}");
				}
				logger.debug(message.toString());
			}
			
			
			return list;
		} finally {
			timerContext.stop();
		}
		
	}
    
	
	
	
	
	public static int execute(String cmdSQL, Object... args) {
		
		final Timer.Context timerContext = TIMER_DATABASE_REQUEST_SQL.time();
		
		
		try {
			long tStart = System.currentTimeMillis();
			
			OrientBaseGraph graph = GraphFactory.getOrientBaseGraph();
			
			Integer value = (Integer) graph.command(new OCommandSQL(cmdSQL)).execute(args);
	
			graph.commit();
			
			long elapsedSeconds = (System.currentTimeMillis() - tStart);
			
			if(logger.isDebugEnabled()) {
				StringBuilder message = new StringBuilder();
				
				message.append(String.format("Query executed in %d ms for %d record%s", elapsedSeconds, value, value > 1 ? "s" : ""));
				
				/*
				 try{
					throw new Exception();
				} catch(Exception e) {
					e.printStackTrace();
				}
				 */
				message.append(String.format(" [%s]", cmdSQL));
				if(args != null) {
					message.append(" {");
					for(Object arg : args) {
						message.append(arg.toString());
						message.append(", ");
					}
					message.delete(message.length() - 2, message.length());
					message.append("}");
				}
				logger.debug(message.toString());
			}
		
			return value;
		} finally {
			timerContext.stop();
		}
	}
    
	public static void executeNoReturn(String cmdSQL, Object... args) {
		
		final Timer.Context timerContext = TIMER_DATABASE_REQUEST_SQL.time();
		
		
		try {
	
			long tStart = System.currentTimeMillis();
			
			OrientBaseGraph graph = GraphFactory.getOrientBaseGraph();
			
			graph.command(new OCommandSQL(cmdSQL)).execute(args);
	
			graph.commit();
			
			long elapsedSeconds = (System.currentTimeMillis() - tStart);
			
			if(logger.isDebugEnabled()) {
				StringBuilder message = new StringBuilder();
				
				message.append(String.format("Query executed in %d ms", elapsedSeconds));
				
				/*
				 try{
					throw new Exception();
				} catch(Exception e) {
					e.printStackTrace();
				}
				 */
				message.append(String.format(" [%s]", cmdSQL));
				if(args != null) {
					message.append(" {");
					for(Object arg : args) {
						message.append(arg.toString());
						message.append(", ");
					}
					message.delete(message.length() - 2, message.length());
					message.append("}");
				}
				logger.debug(message.toString());
			}
		} finally {
			timerContext.stop();
			
		}
	
	}
    
}