import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.MainPage;
import pages.OwnersSearchPage;

public class UITests {
    public WebDriver driver;
    public final String petclinicURL = "http://localhost:8080";
    public MainPage mainPage;
    public OwnersSearchPage ownersSearchPage;

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
}
