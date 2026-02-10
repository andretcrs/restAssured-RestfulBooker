package br.com.desafio.client;

import br.com.desafio.model.request.AuthRequest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthClient {

    @Step("Gerar token de autenticação para o usuário: {username}")
    public String getToken(String username, String password) {
        AuthRequest authRequest = new AuthRequest(username, password);

        return given()
                .contentType(ContentType.JSON)
                .body(authRequest)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}