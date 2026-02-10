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

import static io.restassured.RestAssured.given;

// Essa anotação permite que você use @Order nas classes de teste para definir a sequência
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public abstract class BaseTest {

    protected static String token;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = ConfigManager.get("base.url");
        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();

        validarSaudeDaApi();
        gerarTokenDeAcesso();
    }

    private static void validarSaudeDaApi() {
        given()
                .baseUri(RestAssured.baseURI)
                .when()
                .get("/ping")
                .then()
                .statusCode(201);
    }

    private static void gerarTokenDeAcesso() {
        AuthClient authClient = new AuthClient();
        token = authClient.getToken(
                ConfigManager.getUsername(),
                ConfigManager.getPassword()
        );
    }
}