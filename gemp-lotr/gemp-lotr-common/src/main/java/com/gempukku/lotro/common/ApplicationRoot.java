package com.gempukku.lotro.common;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.Properties;

public class ApplicationRoot {
    private static Logger LOGGER = Logger.getLogger(ApplicationRoot.class);
    private static File _root;

    public synchronized static File getRoot() {
        if (_root == null) {
            Properties props = new Properties();
            try {
                props.load(ApplicationRoot.class.getResourceAsStream("/gemp-lotr.properties"));
                _root = new File(props.getProperty("application.root"));
                LOGGER.debug("Application root is: "+_root.getAbsolutePath());
            } catch (Exception exp) {
                LOGGER.error("Can't find application root", exp);
                throw new RuntimeException("Unable to load gemp-lotr.properties");
            }
        }
        return _root;
    }
}
