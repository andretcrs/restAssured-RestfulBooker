package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.client.BookingClient;
import br.com.desafio.factory.BookingDataFactory;
import br.com.desafio.model.request.BookingRequest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        Response response = bookingClient.deleteBooking(bookingId, token);

        assertThat(response.statusCode())
                .as("O status code deve ser 201 (Created) conforme padrão da API")
                .isEqualTo(201);

        assertThat(response.body().asString()).contains("Created");

        bookingClient.getBookingById(bookingId)
                .then()
                .statusCode(404);
    }

    @Test
    @Story("Tentar excluir reserva sem token")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deve retornar 403 Forbidden ao tentar excluir uma reserva aleatória com token inválido")
    void deveRetornarErroAoExcluirSemToken() {

        int idQualquer = bookingClient.getAllBookings().path("[0].bookingid");

        Response response = bookingClient.deleteBooking(idQualquer, "token_invalido");

        assertThat(response.statusCode())
                .as("A exclusão deve ser negada (403 Forbidden) com token inválido")
                .isEqualTo(403);
    }
}