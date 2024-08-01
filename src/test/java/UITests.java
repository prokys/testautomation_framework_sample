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

    /**
     * Setup method for class UITests.
     * Load .properties file based on environment
     * Initialize driver based on name in .properties file
     */
    @BeforeClass
    public void beforeClass(){
        PropertiesUtils.loadEnvironmentConfiguration();
        driver = DriverUtils.initDriver(PropertiesUtils.getBrowserName());
        System.out.println("Before class");
    }

    /**
     * Teardown method for class UITests.
     * Quit driver after class has been run
     */
    @AfterClass
    public void afterClass() {
        DriverUtils.quitDriver();
        System.out.println("After class");
    }

    /**
     * Test to search for owner.
     * Owner is created in @BeforeMethod
     * Test navigates on ownersSearchPage and search for owner with firstName and lastName
     * Then assert fullName from search with firstName and lastName
     * Owner is deleted in @AfterMethod
     */
    @Test(groups = "createBeforeDeleteAfter")
    public void searchForOwner(){
        ownersSearchPage = mainPage.navigateToOwnersSearchPage();
        String fullName = ownersSearchPage.searchForOwnerAndReturnFullName(firstName, lastName);
        Assert.assertEquals(fullName, firstName+" "+lastName);
        System.out.println("Owner named "+firstName+" "+lastName+" is in database");
    }

    /**
     * Test to create new owner.
     * Test navigates to ownersAddNewPage and creates new owner with firstName, lastName, address, city and telephone
     * Then searches for owner by firstName and lastName and asserts if fullName from search with firstName and lastName of created owner.
     * Owner is deleted in @AfterMethod
     */
    @Test(groups = "deleteOwnerAfter")
    public void createNewOwnerWithCompleteInformation(){
        ownersAddNewPage = mainPage.navigateToOwnersAddNewPage();
        ownersSearchPage = ownersAddNewPage.addNewOwner(firstName, lastName, address, city, telephone);
        String fullName = ownersSearchPage.searchForOwnerAndReturnFullName(firstName, lastName);
        Assert.assertEquals(fullName, firstName+" "+lastName);
        System.out.println("Owner named "+firstName+" "+lastName+" created");
    }

    /**
     * Test to edit existing owner.
     * Owner is created in @BeforeMethod
     * Test searches for created owner and navigates to ownersInformationPage and clicks button to get to ownersEditPage
     * Then it updates information and updates owner and asserts owners information with firstNameEdited and lastNameEdited
     * Owner is deleted in @AfterMethod
     */
    @Test(groups = "createBeforeDeleteAfter")
    public void editExistingOwnerWithCompleteInformation(){
        ownersSearchPage = mainPage.navigateToOwnersSearchPage();
        String fullName = ownersSearchPage.searchForOwnerAndReturnFullName(firstName, lastName);
        Assert.assertEquals(fullName, firstName+" "+lastName);

        ownersInformationPage = ownersSearchPage.navigateToOwnersInformationPage(firstName, lastName);
        ownersEditPage = ownersInformationPage.navigateToOwnersEditPage();
        ownersEditPage.editOwner(firstNameEdited, lastNameEdited, addressEdited, cityEdited, telephoneEdited);

        fullName = ownersInformationPage.checkOwnersInformation(firstNameEdited, lastNameEdited);
        Assert.assertEquals(fullName, firstNameEdited+" "+lastNameEdited);
        System.out.println("User edited to "+firstNameEdited+" "+lastNameEdited);
    }

    /**
     * Setup method to navigate to application URL and initialize mainPage
     */
    @BeforeMethod
    public void beforeAllMethodsSetup(){
        driver.get(PropertiesUtils.getApplicationUrl());
        Assert.assertEquals(driver.findElement(By.xpath("//h1[@class='title']")).getText(), "Welcome to Petclinic");
        mainPage = new MainPage(driver);
    }

    /**
     * Setup method for group createBeforeDeleteAfter to connect to db, create owner and disconnect from db
     */
    @BeforeMethod(onlyForGroups = "createBeforeDeleteAfter", inheritGroups = false)
    public void createOwnerBeforeMethod(){
        PostgresUtils.connectToDb();
        PostgresUtils.executeStatement("INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES ('"+firstName+"', '"+lastName+"', '"+address+"', '"+city+"', '"+telephone+"')");
        PostgresUtils.disconnectFromDb();
        System.out.println("Before method");
    }
    /**
     * Teardown method for groups createBeforeDeleteAfter and deleteOwnerAfter to connect to db, delete owner and disconnect from db
     */
    @AfterMethod(onlyForGroups = {"createBeforeDeleteAfter", "deleteOwnerAfter"}, inheritGroups = false)
    public void deleteOwnerAfterMethod(){
        PostgresUtils.connectToDb();
        PostgresUtils.executeStatement("DELETE FROM owners WHERE first_name='"+firstName+"'");
        PostgresUtils.executeStatement("DELETE FROM owners WHERE first_name='"+firstNameEdited+"'");
        PostgresUtils.disconnectFromDb();
        System.out.println("After method");
    }
}
