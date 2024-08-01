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

    /**
     * Setup method for class DatabaseTests.
     * Connect to database
     */
    @BeforeClass
    public void beforeClass(){
        PropertiesUtils.loadEnvironmentConfiguration();
        PostgresUtils.connectToDb();
        System.out.println("Before class");
    }

    /**
     * Teardown method for class DatabaseTests.
     * Disconnect to database
     */
    @AfterClass
    public void afterClass() {
        PostgresUtils.disconnectFromDb();
        System.out.println("After class");
    }

    /**
     * Test to check if pet is in database
     * Create pet type with @BeforeMethod
     * Use query executor to SELECT petType from database
     * Then assert returned pet type with initial
     * Delete pet type with @AfterMethod
     */
    @Test(groups = "createPetTypeBeforeDeleteAfter")
    public void checkIfPetTypeIsInDatabase() {
        ArrayList<HashMap<String,String>> petTypes = PostgresUtils.executeQuery("SELECT * FROM types WHERE name='"+petType+"'");
        Assert.assertTrue(petTypes.get(0).containsValue(petType));

        System.out.println("Pet type "+petType+" is in database");
    }
    /**
     * Test to add new pet type to database
     * Use query executor to INSERT petType to database
     * Then assert if affectedRows equals to 1 (one pet type added)
     * Delete pet type with @AfterMethod
     */
    @Test(groups = "deletePetTypeAfter")
    public void createNewPetType() {
        affectedRows = PostgresUtils.executeStatement("INSERT INTO types (name) VALUES ('"+petType+"')");
        Assert.assertEquals(affectedRows,1);
        System.out.println("Pet type "+petType+ " added into database");
    }

    /**
     * Test to update pet type in database
     * Create pet type with @BeforeMethod
     * Use query executor to UPDATE petType in database
     * Then assert if affectedRows equals to 1 (one pet type edited)
     * Delete pet type with @AfterMethod
     */
    @Test(groups = "createPetTypeBeforeDeleteAfter")
    public void updatePetTypeTo() {
        affectedRows = PostgresUtils.executeStatement("UPDATE types SET name='" + petTypeEdited + "' WHERE name='" + petType + "'");
        Assert.assertEquals(affectedRows, 1);
        System.out.println("Pet type " + petType + " updated to " + petTypeEdited);
    }

    /**
     * Test to update delete type from database
     * Create pet type with @BeforeMethod
     * Use query executor to DELETE petType from database
     * Then assert if affectedRows equals to 1 (one pet type deleted)
     * Delete pet type (if failed) with @AfterMethod
     */
    @Test(groups = "createPetTypeBeforeDeleteAfter")
    public void deletePetTypeFromDatabase() {
        affectedRows = PostgresUtils.executeStatement("DELETE FROM types WHERE name='"+petType+"'");
        Assert.assertEquals(affectedRows,1);
        System.out.println("Pet type "+petType+ " deleted from database");
    }

    /**
     * Setup method for group createPetTypeBeforeDeleteAfter to create petType with API call
     */
    @BeforeMethod(onlyForGroups = "createPetTypeBeforeDeleteAfter", inheritGroups = false)
    public void createPetTypeBeforeMethod(){
        String requestBody = "{\"name\": \""+petType+"\"}";
        response = given().contentType(ContentType.JSON).body(requestBody).when().post("http://localhost:9966/petclinic/api/pettypes");
        System.out.println("Before method");
    }

    /**
     * Teardown method for group createPetTypeBeforeDeleteAfter and deletePetTypeAfter to delete petType with API call
     */
    @AfterMethod(onlyForGroups = {"createPetTypeBeforeDeleteAfter", "deletePetTypeAfter"}, inheritGroups = false)
    public void deletePetTypeAfterMethod(){
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
