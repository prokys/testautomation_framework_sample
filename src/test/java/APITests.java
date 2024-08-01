import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.PostgresUtils;
import utils.PropertiesUtils;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class APITests {
    private final String vetsAPIUrl = "http://localhost:9966/petclinic/api/vets";

    public int vetID;
    public Response response;

    String firstName = "firstName";
    String lastName = "lastName";
    String firstNameEdited = "firstNameEdited";
    String lastNameEdited = "lastNameEdited";

    /**
     * Setup method for class APITests.
     * Load .properties file based on environment
     */
    @BeforeClass
    public void beforeClass(){
        PropertiesUtils.loadEnvironmentConfiguration();
        System.out.println("Before class");
    }

    /**
     * Test to check for veterinarian.
     * Create veterinarian with @BeforeMethod and store vetID
     * Call API with GET method and vetID
     * Assert response code is 200 and firstName and lastName is same as initial
     * Delete veterinarian with @AfterMethod
     */
    @Test(groups = "createVetBeforeDeleteAfter")
    public void checkIfVeterinarianIsInDatabase() {
        response = when().get(vetsAPIUrl + "/" + vetID);
        Assert.assertEquals(response.then().extract().statusCode(),200);
        Assert.assertEquals(response.then().extract().path("firstName"), firstName);
        Assert.assertEquals(response.then().extract().path("lastName"), lastName);
        System.out.println("Veterinarian "+firstName+" "+ lastName+" is in database");
    }

    /**
     * Test to add new veterinarian.
     * Call API with POST method and body with firstName, lastName and specialty
     * Assert response code is 201 and firstName and lastName is same as initial
     * Delete veterinarian with @AfterMethod
     */
    @Test(groups = "deleteVetAfter")
    public void addVeterinarian() {
        String requestBody = "{\"firstName\": \""+firstName+"\", \"lastName\": \""+lastName+"\", \"specialties\": [{\"name\": \" \"}]}";
        response = given().contentType(ContentType.JSON).body(requestBody).when().post(vetsAPIUrl);
        Assert.assertEquals(response.then().extract().statusCode(), 201);
        Assert.assertEquals(response.then().extract().path("firstName"), firstName);
        Assert.assertEquals(response.then().extract().path("lastName"), lastName);
        System.out.println("Veterinarian "+firstName+" "+lastName+" added to database");
    }

    /**
     * Test to edit existing veterinarian.
     * Create veterinarian with @BeforeMethod and store vetID
     * Call API with PUT method with vetID and body with firstNameEdited, lastNameEdited and specialty (empty string)
     * Assert response code is 204
     * Delete veterinarian with @AfterMethod
     */
    @Test(groups = "createVetBeforeDeleteAfter")
    public void editVeterinarian() {
        String requestBody = "{\"firstName\": \""+firstNameEdited+"\", \"lastName\": \""+lastNameEdited+"\", \"specialties\": [{\"name\": \" \"}]}";
        response = given().contentType(ContentType.JSON).body(requestBody).when().put(vetsAPIUrl+"/"+vetID);
        Assert.assertEquals(response.then().extract().statusCode(), 204);
        System.out.println("Veterinarian edited to "+firstNameEdited+" "+lastNameEdited);
    }

    /**
     * Test to delete existing veterinarian.
     * Create veterinarian with @BeforeMethod and store vetID
     * Call API with DELETE method with vetID
     * Assert response code is 204
     * Delete veterinarian (if failed) with @AfterMethod
     */
    @Test(groups = "createVetBeforeDeleteAfter")
    public void deleteVeterinarian() {
        response = when().delete(vetsAPIUrl+"/"+vetID);
        Assert.assertEquals(response.then().extract().statusCode(), 204);
        System.out.println("Veterinarian "+firstName+" "+lastName+" deleted from database");
    }

    /**
     * Setup method for group createVetBeforeDeleteAfter to connect to db, create veterinarian and disconnect from db
     */
    @BeforeMethod(onlyForGroups = "createVetBeforeDeleteAfter", inheritGroups = false)
    public void beforeMethod(){
        PostgresUtils.connectToDb();
        PostgresUtils.executeStatement("INSERT INTO vets (first_name, last_name) VALUES ('"+firstName+"', '"+lastName+"')");
        vetID = Integer.parseInt(PostgresUtils.executeQuery("SELECT id FROM vets WHERE (first_name='"+firstName+"') AND (last_name='"+lastName+"')").get(0).get("id"));
        PostgresUtils.disconnectFromDb();
        System.out.println("Before method");
    }

    /**
     * Teardown method for group createVetBeforeDeleteAfter and deleteVetAfter to connect to db, delete veterinarian and disconnect from db
     */
    @AfterMethod(onlyForGroups = {"createVetBeforeDeleteAfter", "deleteVetAfter"}, inheritGroups = false)
    public void afterMethod(){
        PostgresUtils.connectToDb();
        PostgresUtils.executeStatement("DELETE FROM vets WHERE (first_name='"+firstName+"') AND (last_name='"+lastName+"')");
        PostgresUtils.executeStatement("DELETE FROM vets WHERE (first_name='"+firstNameEdited+"') AND (last_name='"+lastNameEdited+"')");
        PostgresUtils.disconnectFromDb();
        System.out.println("After method");
    }
}
