package recaf.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
 
public class Log4jTestWatcher extends TestWatcher {
 
    private final Logger logger;
 
    public Log4jTestWatcher() {
        logger = LogManager.getLogger();
    }
 
    public Log4jTestWatcher(String loggerName) {
        logger = LogManager.getLogger(loggerName);
    }
 
    @Override
    protected void failed(Throwable e, Description description) {
        logger.error(description, e);
    }
}

