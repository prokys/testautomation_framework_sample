import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.PostgresUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseTests {
    int affectedRows;

    @BeforeTest
    public void connectToDb(){
        PostgresUtils.connectToDb();
    }
    @AfterTest
    public void disconnectFromDb(){
        PostgresUtils.disconnectFromDb();
    }
    @DataProvider(name = "existingPetType")
    public Object[][] existingPetType() {
        return new Object[][] {
                {"dog"},
        };
    }
    @Test(dataProvider = "existingPetType")
    public void checkIfPetTypeIsInDatabase(String petType) {
        ArrayList<HashMap<String,String>> petTypes = PostgresUtils.executeQuery("SELECT * FROM types WHERE name='"+petType+"'");

        if (petTypes.size()==0){
            System.out.println("Pet type "+petType+ " is not in database");
        } else if (petTypes.get(0).containsValue(petType)) {
            System.out.println("Pet type "+petType+ " is in database");
        } else  {
            System.out.println("Something went wrong");
        }
    }
    @DataProvider(name = "newPetType")
    public Object[][] newPetType() {
        return new Object[][] {
                {"monkey"},
        };
    }
    @Test(dataProvider = "newPetType")
    public void createNewPetType(String petType) {
        affectedRows = PostgresUtils.executeStatement("INSERT INTO types (name) VALUES ('"+petType+"')");
        Assert.assertEquals(affectedRows,1);
        System.out.println("Pet type "+petType+ " added into database");
    }
}