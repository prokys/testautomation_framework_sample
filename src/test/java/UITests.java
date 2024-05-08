import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.MainPage;
import pages.OwnersAddNewPage;
import pages.OwnersSearchPage;

public class UITests {
    public WebDriver driver;
    public final String petclinicURL = "http://localhost:8080";
    public MainPage mainPage;
    public OwnersSearchPage ownersSearchPage;
    public OwnersAddNewPage ownersAddNewPage;

    @BeforeTest
    public void beforeTest(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");

        driver = new ChromeDriver(options);
    }
    @AfterTest
    public void afterTest(){
        driver.quit();
    }
    @Test
    public void loadMainPage(){
        driver.get(petclinicURL);
        Assert.assertEquals(driver.findElement(By.xpath("//h1[@class='title']")).getText(), "Welcome to Petclinic");
        mainPage = new MainPage(driver);
    }
    @DataProvider(name = "Harold Davis")
    public Object[][] createData1() {
        return new Object[][] {
                { "Harold", "Davis" },
        };
    }
    @Test(dataProvider = "Harold Davis")
    public void searchForUser(String firstName, String lastName){
        loadMainPage();
        ownersSearchPage = mainPage.navigateToOwnersSearchPage();
        String fullName = ownersSearchPage.searchForOwnerAndCheckIfExists(firstName, lastName);
        Assert.assertEquals(fullName, firstName+" "+lastName);
        System.out.println("Owner named "+fullName+" is in database");
    }

    @DataProvider(name = "newUserToCreate")
    public Object[][] newUserToCreate() {
        return new Object[][] {
                { "firstName", "lastName", "address", "city", 123456789 },
        };
    }

    @Test(dataProvider = "newUserToCreate")
    public void createNewOwner(String firstName, String lastName, String address, String city, int telephone){
        loadMainPage();
        ownersAddNewPage = mainPage.navigateToOwnersAddNewPage();
        ownersAddNewPage.addNewOwner(firstName, lastName, address, city, telephone);
        searchForUser(firstName,lastName);

    }
}
