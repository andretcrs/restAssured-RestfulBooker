package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.client.BookingClient;
import br.com.desafio.factory.BookingDataFactory;
import br.com.desafio.model.request.BookingRequest;
import br.com.desafio.model.response.BookingIdResponse;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Booking")
@Feature("Get Bookings")
@DisplayName("Testes de Consulta de Reservas")
public class BookingGetTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Story("Buscar todos os bookings")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve retornar a lista de todos os booking IDs e validar o contrato")
    void deveBuscarTodosOsBookings() {
        Response response = bookingClient.getAllBookings();

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-list-schema.json"));

        List<BookingIdResponse> bookings = response.then()
                .extract()
                .jsonPath()
                .getList("", BookingIdResponse.class);

        assertThat(bookings)
                .as("A lista de bookings não deve ser nula")
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    @Story("Buscar reserva específica por ID")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Deve criar uma reserva com Faker e buscá-la pelo ID para validar os detalhes")
    void deveBuscarReservaPorId() {
        BookingRequest request = BookingDataFactory.criarReservaValida();
        int idCriado = bookingClient.createBooking(request)
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        Response response = bookingClient.getBookingById(idCriado);

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-id-schema.json"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.path("firstname").toString())
                    .as("Nome")
                    .isEqualTo(request.getFirstname());

            softly.assertThat(response.path("lastname").toString())
                    .as("Sobrenome")
                    .isEqualTo(request.getLastname());

            softly.assertThat((Integer) response.path("totalprice"))
                    .as("Preço")
                    .isEqualTo(request.getTotalprice());

            softly.assertThat((Boolean) response.path("depositpaid"))
                    .as("Status Depósito")
                    .isEqualTo(request.getDepositpaid());

            softly.assertThat(response.path("bookingdates.checkin").toString())
                    .as("Check-in")
                    .isEqualTo(request.getBookingdates().getCheckin());
        });
    }

    @Test
    @Story("Buscar bookings por nome")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve filtrar a busca por nome e validar o contrato da lista retornada")
    void deveBuscarBookingsPorNome() {
        BookingRequest request = BookingDataFactory.criarReservaValida();
        bookingClient.createBooking(request).then().statusCode(200);

        Response response = bookingClient.getBookingsByName(request.getFirstname(), request.getLastname());

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-list-schema.json"));

        List<BookingIdResponse> bookings = response.then()
                .extract()
                .jsonPath()
                .getList("", BookingIdResponse.class);

        assertThat(bookings).isNotEmpty();
    }

    @Test
    @Story("Buscar reserva inexistente")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve retornar erro 404 ao buscar um ID aleatório que não existe")
    void deveRetornarErroAoBuscarIdInexistente() {
        int idInexistente = 999999 + (int)(Math.random() * 100000);

        bookingClient.getBookingById(idInexistente)
                .then()
                .statusCode(404);
    }
}