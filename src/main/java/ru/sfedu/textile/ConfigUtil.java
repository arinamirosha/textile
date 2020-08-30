package ru.sfedu.textile;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static ru.sfedu.textile.Constants.CONFIG_JAR;
import static ru.sfedu.textile.Constants.CONFIG_PATH;

/**
 * Configuration utility. Allows to get configuration properties from the
 * default configuration file
 *
 * @author Boris Jmailov
 */
public class ConfigUtil {

//    private static final String DEFAULT_CONFIG_PATH = "configs/config.properties";
    private static final Properties configuration = new Properties();

    /**
     * Hides default constructor
     *
     *
     */
    private ConfigUtil() {
    }


    private static Properties getConfiguration() throws IOException {
        if(configuration.isEmpty()){
            loadConfiguration();
        }
        return configuration;
    }

    /**
     * Loads configuration from <code>DEFAULT_CONFIG_PATH</code>
     * @throws IOException In case of the configuration file read failure
     */
    private static void loadConfiguration() throws IOException{
//        InputStream in = DEFAULT_CONFIG_PATH.getClass().getResourceAsStream(DEFAULT_CONFIG_PATH);
        FileInputStream in = new FileInputStream(CONFIG_PATH);

//        FileInputStream in;
//        try {
//            in = new FileInputStream(CONFIG_JAR);
//        } catch (FileNotFoundException e) {
//            in = new FileInputStream(CONFIG_PATH);
//        }

        try {
            configuration.load(in);
        } catch (IOException ex) {
            throw new IOException(ex);
        } finally{
            in.close();
        }
    }
    /**
     * Gets configuration entry value
     * @param key Entry key
     * @return Entry value by key
     * @throws IOException In case of the configuration file read failure
     */
    public static String getConfigurationEntry(String key) throws IOException{
        return getConfiguration().getProperty(key);
    }

}