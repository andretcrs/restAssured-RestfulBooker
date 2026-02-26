package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.client.BookingClient;
import br.com.desafio.factory.BookingDataFactory;
import br.com.desafio.model.request.BookingRequest;
import br.com.desafio.model.response.BookingIdResponse;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

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
        bookingClient.getAllBookings()
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-list-schema.json"))
                .body("$", hasSize(greaterThan(0)));
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

        bookingClient.getBookingById(idCriado)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-id-schema.json"))
                .body("firstname", equalTo(request.getFirstname()))
                .body("lastname", equalTo(request.getLastname()))
                .body("totalprice", equalTo(request.getTotalprice()))
                .body("depositpaid", equalTo(request.getDepositpaid()))
                .body("bookingdates.checkin", equalTo(request.getBookingdates().getCheckin()));
    }

    @Test
    @Story("Buscar bookings por nome")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve filtrar a busca por nome e validar o contrato da lista retornada")
    void deveBuscarBookingsPorNome() {
        BookingRequest request = BookingDataFactory.criarReservaValida();
        bookingClient.createBooking(request).then().statusCode(200);

        bookingClient.getBookingsByName(request.getFirstname(), request.getLastname())
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-list-schema.json"))
                .body("$", not(empty()));
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