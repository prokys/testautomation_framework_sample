package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
    private static String browserName;
    private static String explicitWait;
    private static String applicationUrl;
    private static String databaseConnectionString;

    public static String getBrowserName(){
        return browserName;
    }
    public static String getExplicitWait(){
        return explicitWait;
    }
    public static String getApplicationUrl(){
        return applicationUrl;
    }
    public static String getDatabaseConnectionString(){
        return databaseConnectionString;
    }

    public static void loadEnvironmentConfiguration(){
        String environment = System.getProperty("environment");
        String environmentPropertiesFilePath = "src/test/resources/environments/" + environment.toLowerCase() + ".properties";

        try{
            Properties environmentProperties = new Properties();
            environmentProperties.load(new FileInputStream(environmentPropertiesFilePath));

            browserName = environmentProperties.getProperty("browser.name");
            explicitWait = environmentProperties.getProperty("explicit.wait");
            applicationUrl = environmentProperties.getProperty("application.url");
            databaseConnectionString = environmentProperties.getProperty("database.connection.string");
        }
        catch (IOException e){
            System.out.println("Loading of properties failed with: "+e.getMessage());
        }
    }
}
