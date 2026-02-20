package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.client.BookingClient;
import br.com.desafio.factory.BookingDataFactory;
import br.com.desafio.model.request.BookingRequest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Epic("Booking")
@Feature("Delete Booking")
@DisplayName("Testes de Exclusão de Reserva")
public class BookingDeleteTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Story("Excluir uma reserva com sucesso")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Deve criar uma reserva com Faker, excluí-la e confirmar que ela sumiu")
    void deveExcluirReservaComSucesso() {

        BookingRequest request = BookingDataFactory.criarReservaValida();

        Integer bookingId = bookingClient.createBooking(request)
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        bookingClient.deleteBooking(bookingId, getToken())
                .then()
                .statusCode(201);

        bookingClient.getBookingById(bookingId)
                .then()
                .statusCode(404);
    }

    @Test
    @Story("Tentar excluir reserva sem token")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve retornar 403 Forbidden ao tentar excluir uma reserva aleatória com token inválido")
    void deveRetornarErroAoExcluirSemToken() {

        int idQualquer = bookingClient.getAllBookings()
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .path("[0].bookingid");

        bookingClient.deleteBooking(idQualquer, "token_invalido")
                .then()
                .statusCode(403);
    }
}