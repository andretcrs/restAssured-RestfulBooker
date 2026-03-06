package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.client.BookingClient;
import br.com.desafio.factory.BookingDataFactory;
import br.com.desafio.model.request.BookingRequest;
import io.qameta.allure.*;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

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

        bookingClient.updateBooking(bookingId, updateRequest, getToken())
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-put-schema.json"))
                .body("firstname", equalTo(updateRequest.getFirstname()))
                .body("lastname", equalTo(updateRequest.getLastname()))
                .body("totalprice", equalTo(updateRequest.getTotalprice()))
                .body("depositpaid", equalTo(updateRequest.getDepositpaid()))
                .body("bookingdates.checkin", equalTo(updateRequest.getBookingdates().getCheckin()))
                .body("bookingdates.checkout", equalTo(updateRequest.getBookingdates().getCheckout()));
    }

    @Test
    @Story("Atualizar reserva sem token")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve retornar 403 (Forbidden) ao tentar atualizar com token inválido")
    void deveRetornarErroAoAtualizarSemToken() {
        BookingRequest novaReserva = BookingDataFactory.criarReservaValida();
        int bookingId = bookingClient.createBooking(novaReserva)
                .then()
                .extract()
                .path("bookingid");

        BookingRequest requestAtualizacao = BookingDataFactory.criarReservaValida();

        bookingClient.updateBooking(bookingId, requestAtualizacao, "token_invalido")
                .then()
                .statusCode(403);
    }

    @Test
    @Story("Validar Idempotência na Atualização (PUT)")
    @Description("Deve garantir que múltiplas chamadas PUT com o mesmo payload resultem no mesmo estado")
    void deveGarantirIdempotenciaAoAtualizarReserva() {
        BookingRequest createRequest = BookingDataFactory.criarReservaValida();
        Integer bookingId = bookingClient.createBooking(createRequest)
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        BookingRequest updateRequest = BookingDataFactory.criarReservaValida();
        ResponseSpecification validacaoReserva = new ResponseSpecBuilder()
                .expectBody("firstname", equalTo(updateRequest.getFirstname()))
                .expectBody("lastname", equalTo(updateRequest.getLastname()))
                .expectBody("totalprice", equalTo(updateRequest.getTotalprice()))
                .expectBody("depositpaid", equalTo(updateRequest.getDepositpaid()))
                .build();

        bookingClient.updateBooking(bookingId, updateRequest, getToken())
                .then()
                .statusCode(200)
                .spec(validacaoReserva);

        bookingClient.updateBooking(bookingId, updateRequest, getToken())
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .spec(validacaoReserva);

        bookingClient.getBookingById(bookingId)
                .then()
                .body("firstname", equalTo(updateRequest.getFirstname()));

    }

}