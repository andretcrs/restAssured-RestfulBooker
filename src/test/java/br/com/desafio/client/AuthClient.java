package br.com.desafio.client;

import br.com.desafio.model.request.AuthRequest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private static final String LOGIN_ENDPOINT = "/auth";

    @Step("Gerar token de autenticação para o usuário: {username}")
    public String getToken(String username, String password) {
        AuthRequest authRequest = new AuthRequest(username, password);

        return given()
                .contentType(ContentType.JSON)
                .body(authRequest)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}