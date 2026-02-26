package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.client.BookingClient;
import br.com.desafio.factory.BookingDataFactory;
import br.com.desafio.model.request.BookingRequest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("Booking")
@Feature("Create Booking")
@DisplayName("Testes de Criação de Reserva")
public class BookingPostTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Story("Criar reserva com sucesso e validar contrato")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Valida contrato e dados da reserva com validações encadeadas")
    void deveCriarReservaComSucesso() {
        BookingRequest requestBody = BookingDataFactory.criarReservaValida();

        bookingClient.createBooking(requestBody)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"))
                .body("bookingid", notNullValue())
                .body("booking.firstname", equalTo(requestBody.getFirstname()))
                .body("booking.lastname", equalTo(requestBody.getLastname()))
                .body("booking.totalprice", equalTo(requestBody.getTotalprice()))
                .body("booking.depositpaid", equalTo(requestBody.getDepositpaid()))
                .body("booking.bookingdates.checkin", equalTo(requestBody.getBookingdates().getCheckin()))
                .body("booking.bookingdates.checkout", equalTo(requestBody.getBookingdates().getCheckout()));
    }
}