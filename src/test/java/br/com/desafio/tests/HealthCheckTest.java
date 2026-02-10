package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.factory.BookingDataFactory;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Infraestrutura")
@Feature("Health Check")
@DisplayName("Testes de Disponibilidade")
public class HealthCheckTest extends BaseTest {

    @Test
    @Story("Verificar se a API está online")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Validar Health Check da API (Ping)")
    void deveVerificarSaudeDaApi() {
        Allure.addAttachment("Tester Responsável", BookingDataFactory.novoNome());

        Response response = given()
                .when()
                .get("/ping")
                .then()
                .extract()
                .response();

        assertThat(response.statusCode())
                .as("O endpoint /ping deve retornar status 201 quando a API está ativa")
                .isEqualTo(201);

        assertThat(response.body().asString())
                .as("O corpo da resposta do health check deve ser 'Created'")
                .isEqualTo("Created");
    }
}