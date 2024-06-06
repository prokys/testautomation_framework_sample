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


    @BeforeClass
    public void beforeClass(){
        PropertiesUtils.loadEnvironmentConfiguration();
        System.out.println("Before test");
    }

    @Test(groups = "createVetBeforeDeleteAfter")
    public void checkIfVeterinarianIsInDatabase() {
        response = when().get(vetsAPIUrl + "/" + vetID);
        if (response.then().extract().statusCode()==200) {
            Assert.assertEquals(response.then().extract().path("firstName"), firstName);
            Assert.assertEquals(response.then().extract().path("lastName"), lastName);
//            Assert.assertEquals(response.then().extract().path("specialties.name[0]"), specialty);
            System.out.println("Veterinarian "+firstName+" "+ lastName+" is in database");
        }
    }

    @Test(groups = "deleteVetAfter")
    public void addVeterinarian() {
        String requestBody = "{\"firstName\": \""+firstName+"\", \"lastName\": \""+lastName+"\", \"specialties\": [{\"name\": \" \"}]}";
        response = given().contentType(ContentType.JSON).body(requestBody).when().post(vetsAPIUrl);
        Assert.assertEquals(response.then().extract().statusCode(), 201);
        System.out.println("Veterinarian "+firstName+" "+lastName+" added to database");
    }

    @Test(groups = "createVetBeforeDeleteAfter")
    public void editVeterinarian() {
        String requestBody = "{\"firstName\": \""+firstNameEdited+"\", \"lastName\": \""+lastNameEdited+"\", \"specialties\": [{\"name\": \" \"}]}";
        response = given().contentType(ContentType.JSON).body(requestBody).when().put(vetsAPIUrl+"/"+vetID);
        Assert.assertEquals(response.then().extract().statusCode(), 204);
        System.out.println("Veterinarian edited to "+firstNameEdited+" "+lastNameEdited);
    }
    @Test(groups = "createVetBeforeDeleteAfter")
    public void deleteVeterinarian() {
        response = when().delete(vetsAPIUrl+"/"+vetID);
        Assert.assertEquals(response.then().extract().statusCode(), 204);
        System.out.println("Veterinarian "+firstName+" "+lastName+" deleted from database");
    }

    @BeforeMethod(onlyForGroups = "createVetBeforeDeleteAfter", inheritGroups = false)
    public void beforeMethod(){
        PostgresUtils.connectToDb();
        PostgresUtils.executeStatement("INSERT INTO vets (first_name, last_name) VALUES ('"+firstName+"', '"+lastName+"')");
        vetID = Integer.parseInt(PostgresUtils.executeQuery("SELECT id FROM vets WHERE (first_name='"+firstName+"') AND (last_name='"+lastName+"')").get(0).get("id"));
        PostgresUtils.disconnectFromDb();
        System.out.println("Before method");
    }

    @AfterMethod(onlyForGroups = {"createVetBeforeDeleteAfter", "deleteVetAfter"}, inheritGroups = false)
    public void afterMethod(){
        PostgresUtils.connectToDb();
        PostgresUtils.executeStatement("DELETE FROM vets WHERE (first_name='"+firstName+"') AND (last_name='"+lastName+"')");
        PostgresUtils.executeStatement("DELETE FROM vets WHERE (first_name='"+firstNameEdited+"') AND (last_name='"+lastNameEdited+"')");
        PostgresUtils.disconnectFromDb();
        System.out.println("After method");
    }
}
