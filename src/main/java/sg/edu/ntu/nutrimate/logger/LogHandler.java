package sg.edu.ntu.nutrimate.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHandler {

   private static final Logger logger = LoggerFactory.getLogger(LogHandler.class.getSimpleName());
   private static final Logger authLogger = LoggerFactory.getLogger("sg.edu.ntu.nutrimate.logger.auth");
    
   //to check that slf4j Logger has their own defined levels
    public enum Level {
        INFO, DEBUG, WARN, ERROR
    }
    
    public static void handleLog(Level level, String message){
        switch (level) {
            case INFO -> logger.info(message);
            case DEBUG -> logger.debug(message);
            case WARN -> logger.warn(message);
            case ERROR -> logger.error(message);
        };
    }

    public static void handleAuthLog(Level level, String message){
        switch (level) {
            case INFO -> authLogger.info(message);
            case DEBUG -> authLogger.debug(message);
            case WARN -> authLogger.warn(message);
            case ERROR -> authLogger.error(message);
        };
    }
    
}
