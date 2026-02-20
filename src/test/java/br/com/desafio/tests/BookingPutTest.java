package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.client.BookingClient;
import br.com.desafio.factory.BookingDataFactory;
import br.com.desafio.model.request.BookingRequest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Booking")
@Feature("Update Booking")
@DisplayName("Testes de Atualização de Reserva")
public class BookingPutTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Story("Atualizar reserva existente")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Deve atualizar todos os campos de uma reserva e validar o contrato")
    void deveAtualizarReservaComSucesso() {
        BookingRequest createRequest = BookingDataFactory.criarReservaValida();
        Integer bookingId = bookingClient.createBooking(createRequest)
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        BookingRequest updateRequest = BookingDataFactory.criarReservaValida();

        Response response = bookingClient.updateBooking(bookingId, updateRequest, getToken());

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-put-schema.json"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.path("firstname").toString())
                    .as("Nome")
                    .isEqualTo(updateRequest.getFirstname());

            softly.assertThat(response.path("lastname").toString())
                    .as("Sobrenome")
                    .isEqualTo(updateRequest.getLastname());

            softly.assertThat((Integer) response.path("totalprice"))
                    .as("Preço")
                    .isEqualTo(updateRequest.getTotalprice());

            softly.assertThat((Boolean) response.path("depositpaid"))
                    .as("Status Depósito")
                    .isEqualTo(updateRequest.getDepositpaid());

            softly.assertThat(response.path("bookingdates.checkin").toString())
                    .as("Check-in")
                    .isEqualTo(updateRequest.getBookingdates().getCheckin());

            softly.assertThat(response.path("bookingdates.checkout").toString())
                    .as("Check-out")
                    .isEqualTo(updateRequest.getBookingdates().getCheckout());
        });
    }

    @Test
    @Story("Atualizar reserva sem token")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve retornar 403 (Forbidden) ao tentar atualizar com token inválido")
    void deveRetornarErroAoAtualizarSemToken() {
        BookingRequest novaReserva = BookingDataFactory.criarReservaValida();
        int bookingId = bookingClient.createBooking(novaReserva)
                .then().extract().path("bookingid");

        BookingRequest requestAtualizacao = BookingDataFactory.criarReservaValida();
        Response response = bookingClient.updateBooking(bookingId, requestAtualizacao, "token_invalido");

        assertThat(response.statusCode())
                .as("A API deve proibir a atualização (403 Forbidden)")
                .isEqualTo(403);
    }
}