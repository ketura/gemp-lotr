package com.gempukku.lotro.common;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.Properties;

public class ApplicationConfiguration {
    private static Logger LOGGER = Logger.getLogger(ApplicationConfiguration.class);
    private static Properties _properties;
    private static File _root;

    private synchronized static Properties getProperties() {
        if (_properties == null) {
            Properties props = new Properties();
            try {
                props.load(ApplicationConfiguration.class.getResourceAsStream("/gemp-lotr.properties"));
                _properties = props;
            } catch (Exception exp) {
                LOGGER.error("Can't load application configuration", exp);
                throw new RuntimeException("Unable to load application configuration", exp);
            }
        }
        return _properties;
    }

    public static String getProperty(String property) {
        return getProperties().getProperty(property);
    }
}
