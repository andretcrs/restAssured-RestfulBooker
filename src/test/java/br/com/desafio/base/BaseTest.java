package br.com.desafio.base;

import br.com.desafio.client.AuthClient;
import br.com.desafio.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.TestClassOrder;

import java.time.Instant;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public abstract class BaseTest {

    protected static String token;
    private static Instant tokenExpiration;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = ConfigManager.getConfiguration().baseUrl();

        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();

        validarSaudeDaApi();
    }

    protected String getToken() {
        if (token == null || isTokenExpired()) {
            forceGenerateToken();
        }
        return token;
    }

    private boolean isTokenExpired() {
        return tokenExpiration == null || Instant.now().isAfter(tokenExpiration);
    }

    protected void forceGenerateToken() {
        AuthClient authClient = new AuthClient();

        token = authClient.getToken(
                ConfigManager.getConfiguration().username(),
                ConfigManager.getConfiguration().password()
        );

        tokenExpiration = Instant.now().plusSeconds(540);
    }

    private static void validarSaudeDaApi() {
        RestAssured.given()
                .baseUri(RestAssured.baseURI)
                .when()
                .get("/ping")
                .then()
                .statusCode(201);
    }
}