package org.ebaloo.itkeeps.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class LogFactory {

	private static Logger _mainLogger = null;
	
	private static boolean _init = false;
	
	public static final String LOG_LEVEL = "log.level";
	public static final String DEFAULT_LOG_LEVEL = "INFO";
	
	   public static void init(Class<?> _class) {
		   
		   if(_init)
			   return;
			  
		   
		   String level = ConfigFactory.getString(LOG_LEVEL, DEFAULT_LOG_LEVEL);
		   
		   System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, level);
			
			SLF4JBridgeHandler.removeHandlersForRootLogger();
			SLF4JBridgeHandler.install();
			
			_mainLogger = LoggerFactory.getLogger("main");
			
			_mainLogger.info("mainlog start : " + level);
			_mainLogger.info("version       : " + ConfigFactory.getManifestInfoVersion(_class));
			
			_init = true;
	    }
	
	   
	   public static Logger getMain() {
		   if(!_init)
			   init(null);
		   
		   return _mainLogger;
	   }


	public static void trace(String msg) {
	   	if(_mainLogger == null || !_mainLogger.isTraceEnabled() || msg == null)
	   		return;

		  _mainLogger.trace(msg);
	}


	public static void trace(String format, Object... args) {
	    if(_mainLogger == null || !_mainLogger.isTraceEnabled())
            return;

            try {
                _mainLogger.trace(String.format(format, args));
            } catch(Exception e) {
                _mainLogger.trace("Exception : " + e.getMessage());
            }
	    }

	public static void trace(Throwable t) {
        if(_mainLogger == null || !_mainLogger.isTraceEnabled())
            return;

        trace("%s, %s", t.getMessage(), t.getStackTrace());
    }


}
