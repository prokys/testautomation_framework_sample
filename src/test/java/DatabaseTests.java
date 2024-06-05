import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.PostgresUtils;
import utils.PropertiesUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class DatabaseTests {
    int affectedRows;
    Response response;

    String petType = "petType";
    String petTypeEdited = "petTypeEdited";

    @BeforeTest
    public void connectToDb(){
        PropertiesUtils.loadEnvironmentConfiguration();
        PostgresUtils.connectToDb();
        System.out.println("Before test");
    }
    @AfterTest
    public void disconnectFromDb(){
        PostgresUtils.disconnectFromDb();
        System.out.println("After test");
    }

    @Test(groups = "createPetTypeBeforeDeleteAfter")
    public void checkIfPetTypeIsInDatabase() {
        ArrayList<HashMap<String,String>> petTypes = PostgresUtils.executeQuery("SELECT * FROM types WHERE name='"+petType+"'");
        Assert.assertTrue(petTypes.get(0).containsValue(petType));

        System.out.println("Pet type "+petType+" is in database");
    }
    @Test(groups = "deletePetTypeAfter")
    public void createNewPetType() {
        affectedRows = PostgresUtils.executeStatement("INSERT INTO types (name) VALUES ('"+petType+"')");
        Assert.assertEquals(affectedRows,1);
        System.out.println("Pet type "+petType+ " added into database");
    }
    @Test(groups = "createPetTypeBeforeDeleteAfter")
    public void updatePetTypeTo() {
        affectedRows = PostgresUtils.executeStatement("UPDATE types SET name='" + petTypeEdited + "' WHERE name='" + petType + "'");
        Assert.assertEquals(affectedRows, 1);
        System.out.println("Pet type " + petType + " updated to " + petTypeEdited);
    }
    @Test(groups = "createPetTypeBeforeDeleteAfter")
    public void deletePetTypeFromDatabase() {
        affectedRows = PostgresUtils.executeStatement("DELETE FROM types WHERE name='"+petType+"'");
        Assert.assertEquals(affectedRows,1);
        System.out.println("Pet type "+petType+ " deleted from database");
    }

    @BeforeMethod(onlyForGroups = "createPetTypeBeforeDeleteAfter", inheritGroups = false)
    public void beforeMethod(){
        String requestBody = "{\"name\": \""+petType+"\"}";
        response = given().contentType(ContentType.JSON).body(requestBody).when().post("http://localhost:9966/petclinic/api/pettypes");
        System.out.println("Before method");
    }
    @AfterMethod(onlyForGroups = {"createPetTypeBeforeDeleteAfter", "deletePetTypeAfter"}, inheritGroups = false)
    public void afterMethod(){
        response = when().get("http://localhost:9966/petclinic/api/pettypes");
        ArrayList<String> petTypes = response.then().extract().body().path( "name");
        ArrayList<Integer> petTypesIDs = response.then().extract().body().path( "id");
        int petTypeID;
        if(petTypes.contains(petType)){
            petTypeID = (petTypesIDs.get(petTypes.indexOf(petType)));
            when().delete("http://localhost:9966/petclinic/api/pettypes/"+petTypeID);
        } else if (petTypes.contains(petTypeEdited)) {
            petTypeID = (petTypesIDs.get(petTypes.indexOf(petTypeEdited)));
            when().delete("http://localhost:9966/petclinic/api/pettypes/"+petTypeID);
        }
        System.out.println("After method");
    }
}
