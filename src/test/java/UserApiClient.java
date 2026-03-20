import io.restassured.response.*;

import static io.restassured.RestAssured.*;

import io.restassured.config.RestAssuredConfig;
import io.restassured.config.HttpClientConfig;

public class UserApiClient {
    private static UserApiClient instance;
    private final String baseUrl;
    private final RestAssuredConfig config;
    private int timeout = 5000;

    public UserApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", timeout)
                        .setParam("http.socket.timeout", timeout));
    }

    public Response createUser(models.UserPayload payload) {
        return given()
                .config(config)
                .baseUri(baseUrl)
                .contentType(io.restassured.http.ContentType.JSON)
                .body(payload)
                .post("/api/v1/users");
    }

    public Response getUserById(String userId) {
        return given()
                .config(config)
                .baseUri(baseUrl)
                .pathParam("id", userId)
                .get("/api/v1/users/{id}");
    }

    public Response deleteUser(String userId) {
        return given()
                .config(config)
                .baseUri(baseUrl)
                .when()
                .delete("/api/v1/users/", userId);
    }
}
