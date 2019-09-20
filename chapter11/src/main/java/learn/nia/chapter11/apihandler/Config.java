package learn.nia.chapter11.apihandler;

import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class Config {
    private static Properties properties = new Properties();
    private static String     configName = "/project.properties";
    private static Config     instance;

    public Config() {
        try {
            properties.load(new InputStreamReader(getClass().getResourceAsStream(configName), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public static int getInt( String str){
        try {
            if (null == instance) {
                getInstance();
            }
            return Integer.parseInt(properties.getProperty( str ));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getLong( String str){
        try {
            if (null == instance) {
                getInstance();
            }
            return Long.parseLong( properties.getProperty( str ) );
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double getDouble( String str){
        try {
            if (null == instance) {
                getInstance();
            }
            return Double.parseDouble(properties.getProperty( str ));

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getString( String str){
        try {
            if (null == instance) {
                getInstance();
            }
            return properties.getProperty( str );
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean getBoolean( String str){
        try {
            if (null == instance) {
                getInstance();
            }
            return Boolean.parseBoolean( properties.getProperty( str ));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
