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
        if (environment == null){
            environment = "test";
            System.out.println("Environment not set, using test as default");
        }
        String environmentPropertiesFilePath = "src/test/resources/environments/" + environment.toLowerCase() + ".properties";

        try{
            Properties environmentProperties = new Properties();
            environmentProperties.load(new FileInputStream(environmentPropertiesFilePath));

            browserName = environmentProperties.getProperty("browser.name");
            if (browserName == null){
                browserName = "chrome";
                System.out.println("Browser name not provided, using chrome as default");
            }
            explicitWait = environmentProperties.getProperty("explicit.wait");
            if (explicitWait == null){
                explicitWait = "30";
                System.out.println("Explicit wait not provided, using 30 as default");
            }
            applicationUrl = environmentProperties.getProperty("application.url");
            databaseConnectionString = environmentProperties.getProperty("database.connection.string");
        }
        catch (IOException e){
            System.out.println("Loading of properties failed with: "+e.getMessage());
        }
    }
}
