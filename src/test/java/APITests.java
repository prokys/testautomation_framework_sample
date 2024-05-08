import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class APITests {
    private final String vetsAPIUrl = "http://localhost:9966/petclinic/api/vets";

    public int vetID;
    public Response response;

    @DataProvider(name = "existingVetData")
    public Object[][] existingVetData() {
        return new Object[][] {
                { "Helen", "Leary", "radiology"},
        };
    }
    @Test(dataProvider = "existingVetData")
    public void checkIfVetIsInDatabase(String firstName, String lastName, String specialty) {
        if(vetID==0) {
            ArrayList<String> vetsFirstNames = when().get(vetsAPIUrl).then().extract().path("firstName");
            ArrayList<String> vetsLastNames = when().get(vetsAPIUrl).then().extract().path("lastName");
            ArrayList<Integer> vetsIDs = when().get(vetsAPIUrl).then().extract().path("id");

            if (vetsFirstNames.indexOf(firstName) == vetsLastNames.indexOf(lastName)) {
                vetID = vetsIDs.get(vetsFirstNames.indexOf(firstName));
            }
        }
        response = when().get(vetsAPIUrl + "/" + vetID);
        if (response.then().extract().statusCode()==200) {
            Assert.assertEquals(response.then().extract().path("firstName"), firstName);
            Assert.assertEquals(response.then().extract().path("lastName"), lastName);
            Assert.assertEquals(response.then().extract().path("specialties.name[0]"), specialty);
            System.out.println("User "+firstName+" "+ lastName+" is in database");
        } else {
            System.out.println("User "+firstName+" "+ lastName+" is not in database");
        }
    }

    @DataProvider(name = "newVetData")
    public Object[][] newVetData() {
        return new Object[][] {
                { "firstName", "lastName", "surgery"},
        };
    }
    @Test(dataProvider = "newVetData")
    public void addVeterinarian(String firstName, String lastName, String specialty) {
        String requestBody = "{\"firstName\": \""+firstName+"\", \"lastName\": \""+lastName+"\", \"specialties\": [{\"name\": \""+specialty+"\"}]}";
        response = given().contentType(ContentType.JSON).body(requestBody).when().post(vetsAPIUrl);
        Assert.assertEquals(response.then().extract().statusCode(), 201);
        vetID = response.then().extract().path("id");
        System.out.println("User "+firstName+" "+lastName+" added to database");
        checkIfVetIsInDatabase(firstName, lastName, specialty);
    }

    @DataProvider(name = "editVetData")
    public Object[][] editVetData() {
        return new Object[][] {
                { "firstName", "lastName", "surgery", "firstNameEdited", "lastNameEdited", "surgeryEdited"},
        };
    }

    @Test(dataProvider = "editVetData")
    public void editVeterinarianToWithSpecialty(String firstName, String lastName, String specialty, String firstNameEdited, String lastNameEdited, String specialtyEdited) {
        checkIfVetIsInDatabase(firstName, lastName, specialty);
        String requestBody = "{\"firstName\": \""+firstNameEdited+"\", \"lastName\": \""+lastNameEdited+"\", \"specialties\": [{\"name\": \""+specialtyEdited+"\"}]}";
        response = given().contentType(ContentType.JSON).body(requestBody).when().put(vetsAPIUrl+"/"+vetID);
        Assert.assertEquals(response.then().extract().statusCode(), 204);
        System.out.println("User edited to: "+firstNameEdited+" "+lastNameEdited);
    }

}
