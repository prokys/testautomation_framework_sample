import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import utils.DriverUtils;
import utils.PostgresUtils;
import utils.PropertiesUtils;

public class UITests {
    public WebDriver driver;

    public MainPage mainPage;
    public OwnersSearchPage ownersSearchPage;
    public OwnersAddNewPage ownersAddNewPage;
    public OwnersInformationPage ownersInformationPage;
    public OwnersEditPage ownersEditPage;

    String firstName = "firstName";
    String lastName = "lastName";
    String city = "city";
    String address = "address";
    int telephone = 123456789;

    String firstNameEdited = "firstNameEdited";
    String lastNameEdited = "lastNameEdited";
    String cityEdited = "cityEdited";
    String addressEdited = "addressEdited";
    int telephoneEdited = 987654321;

    @BeforeTest
    public void beforeTest(){
        PropertiesUtils.loadEnvironmentConfiguration();
        driver = DriverUtils.initDriver(PropertiesUtils.getBrowserName());
        System.out.println("Before test");
    }
    @AfterTest
    public void afterTest() {
        DriverUtils.quitDriver();
        System.out.println("After test");
    }


    @Test(groups = "createBeforeDeleteAfter")
    public void searchForUser(){
        loadMainPage();
        ownersSearchPage = mainPage.navigateToOwnersSearchPage();
        ownersSearchPage.searchForOwnerAndCheckIfExists(firstName, lastName);
        System.out.println("Owner named "+firstName+" "+lastName+" is in database");
    }

    @Test(groups = "deleteOwnerAfter")
    public void createNewOwner(){
        loadMainPage();
        ownersAddNewPage = mainPage.navigateToOwnersAddNewPage();
        ownersSearchPage = ownersAddNewPage.addNewOwner(firstName, lastName, address, city, telephone);
        ownersSearchPage.searchForOwnerAndCheckIfExists(firstName, lastName);
        System.out.println("Owner named "+firstName+" "+lastName+" created");
    }

    @Test(groups = "createBeforeDeleteAfter")
    public void editExistingOwner(){
        loadMainPage();
        ownersSearchPage = mainPage.navigateToOwnersSearchPage();
        ownersSearchPage.searchForOwnerAndCheckIfExists(firstName, lastName);
        ownersInformationPage = ownersSearchPage.navigateToOwnersInformationPage(firstName, lastName);
        ownersEditPage = ownersInformationPage.navigateToOwnersEditPage();
        ownersEditPage.editOwner(firstNameEdited, lastNameEdited, addressEdited, cityEdited, telephoneEdited);
        ownersInformationPage.checkOwnersInformation(firstNameEdited, lastNameEdited);
        System.out.println("User edited to "+firstNameEdited+" "+lastNameEdited);
    }
    @BeforeMethod(onlyForGroups = "createBeforeDeleteAfter", inheritGroups = false)
    public void beforeMethod(){
        PostgresUtils.connectToDb();
        PostgresUtils.executeStatement("INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES ('"+firstName+"', '"+lastName+"', '"+address+"', '"+city+"', '"+telephone+"')");
        PostgresUtils.disconnectFromDb();
        System.out.println("Before method");
    }
    @AfterMethod(onlyForGroups = {"createBeforeDeleteAfter", "deleteOwnerAfter"}, inheritGroups = false)
    public void afterMethod(){
        PostgresUtils.connectToDb();
        PostgresUtils.executeStatement("DELETE FROM owners WHERE first_name='firstName'");
        PostgresUtils.executeStatement("DELETE FROM owners WHERE first_name='firstNameEdited'");
        PostgresUtils.disconnectFromDb();
        System.out.println("After method");
    }

    public void loadMainPage(){
        driver.get(PropertiesUtils.getApplicationUrl());
        Assert.assertEquals(driver.findElement(By.xpath("//h1[@class='title']")).getText(), "Welcome to Petclinic");
        mainPage = new MainPage(driver);
    }
}
