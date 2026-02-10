package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.client.BookingClient;
import br.com.desafio.factory.BookingDataFactory;
import br.com.desafio.model.request.BookingRequest;
import br.com.desafio.model.response.BookingIdResponse;
import br.com.desafio.model.response.BookingResponse;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Booking")
@Feature("Get Bookings")
@DisplayName("Testes de Consulta de Reservas")
public class BookingGetTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    // Nota: Como os testes de GET (consulta) geralmente não exigem Token na Restful Booker,
    // não precisamos usar a variável 'token' herdada aqui, mas o BaseTest garante que
    // a API está online antes de começar.

    @Test
    @Story("Buscar todos os bookings")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve retornar a lista de todos os booking IDs cadastrados")
    void deveBuscarTodosOsBookings() {
        Response response = bookingClient.getAllBookings();

        List<BookingIdResponse> bookings = response.then()
                .statusCode(200)
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
        // Criação de massa de dados dinâmica
        BookingRequest request = BookingDataFactory.criarReservaValida();
        int idCriado = bookingClient.createBooking(request)
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        Response response = bookingClient.getBookingById(idCriado);

        BookingResponse booking = response.then()
                .statusCode(200)
                .extract()
                .as(BookingResponse.class);

        // Validação profunda com AssertJ
        assertThat(booking.getFirstname()).isEqualTo(request.getFirstname());
        assertThat(booking.getLastname()).isEqualTo(request.getLastname());
        assertThat(booking.getTotalprice()).isEqualTo(request.getTotalprice());
    }

    @Test
    @Story("Buscar bookings por nome")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve criar uma reserva com Faker e filtrar a busca por esse nome e sobrenome")
    void deveBuscarBookingsPorNome() {
        BookingRequest request = BookingDataFactory.criarReservaValida();
        bookingClient.createBooking(request).then().statusCode(200);

        Response response = bookingClient.getBookingsByName(request.getFirstname(), request.getLastname());

        List<BookingIdResponse> bookings = response.then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("", BookingIdResponse.class);

        assertThat(bookings)
                .as("A busca por filtros de nome deve retornar ao menos a reserva que acabamos de criar")
                .isNotEmpty();
    }

    @Test
    @Story("Buscar reserva inexistente")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve retornar erro 404 ao buscar um ID aleatório que não existe")
    void deveRetornarErroAoBuscarIdInexistente() {
        // Gerando um ID improvável de existir
        int idInexistente = 999999 + (int)(Math.random() * 100000);

        bookingClient.getBookingById(idInexistente)
                .then()
                .statusCode(404);
    }
}