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

@Epic("Booking")
@Feature("Create Booking")
@DisplayName("Testes de Criação de Reserva")
public class BookingPostTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Story("Criar reserva com sucesso e validar contrato")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Valida contrato e dados da reserva sem interromper no primeiro erro")
    void deveCriarReservaComSucesso() {
        BookingRequest requestBody = BookingDataFactory.criarReservaValida();
        Response response = bookingClient.createBooking(requestBody);

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat((Object) response.path("bookingid"))
                    .as("ID da reserva")
                    .isNotNull();

            softly.assertThat(response.path("booking.firstname").toString())
                    .as("Nome")
                    .isEqualTo(requestBody.getFirstname());

            softly.assertThat(response.path("booking.lastname").toString())
                    .as("Sobrenome")
                    .isEqualTo(requestBody.getLastname());

            softly.assertThat((Integer) response.path("booking.totalprice"))
                    .as("Preço")
                    .isEqualTo(requestBody.getTotalprice());

            softly.assertThat((Boolean) response.path("booking.depositpaid"))
                    .as("Status Depósito")
                    .isEqualTo(requestBody.getDepositpaid());

            softly.assertThat(response.path("booking.bookingdates.checkin").toString())
                    .as("Data Check-in")
                    .isEqualTo(requestBody.getBookingdates().getCheckin());

            softly.assertThat(response.path("booking.bookingdates.checkout").toString())
                    .as("Data Check-out")
                    .isEqualTo(requestBody.getBookingdates().getCheckout());
        });
    }
}