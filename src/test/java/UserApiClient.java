import io.restassured.response.*;

import static io.restassured.RestAssured.*;

public class UserApiClient {
    private static UserApiClient instance;
    private final String baseUrl;
    private int timeout = 5000;

    private UserApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static UserApiClient getInstance(String baseUrl) {
        if (instance == null) {
            instance = new UserApiClient(baseUrl);
        }
        return instance;
    }

    public Response createUser(java.util.Map<String, Object> payload) {
        return given()
                .baseUri(baseUrl)
                .contentType(io.restassured.http.ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/users");
    }

    public Response getUserById(String userId) {
        return given()
                .baseUri(baseUrl)
                .pathParam("id", userId)
                .when()
                .get("/api/v1/users/{id}");
    }

    public void deleteUser(String userId) {
        given()
                .baseUri(baseUrl)
                .when()
                .delete("/api/v1/users/" + userId);
    }
}
