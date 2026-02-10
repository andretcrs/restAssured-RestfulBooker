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
@Feature("Update Booking")
@DisplayName("Testes de Atualização de Reserva")
public class BookingPutTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Story("Atualizar reserva existente")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Deve atualizar todos os campos de uma reserva usando Faker e validar o retorno")
    void deveAtualizarReservaComSucesso() {
        BookingRequest createRequest = BookingDataFactory.criarReservaValida();
        Integer bookingId = bookingClient.createBooking(createRequest)
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        BookingRequest updateRequest = BookingDataFactory.criarReservaValida();

        Response response = bookingClient.updateBooking(bookingId, updateRequest, token);

        assertThat(response.statusCode()).isEqualTo(200);

        String firstname = response.path("firstname");
        String lastname = response.path("lastname");
        Integer totalPrice = response.path("totalprice");
        Boolean depositPaid = response.path("depositpaid");

        assertThat(firstname).as("O nome deve ser o novo nome gerado")
                .isEqualTo(updateRequest.getFirstname());

        assertThat(lastname).as("O sobrenome deve ser o novo sobrenome gerado")
                .isEqualTo(updateRequest.getLastname());

        assertThat(totalPrice).as("O preço deve ser o novo valor gerado")
                .isEqualTo(updateRequest.getTotalprice());

        assertThat(depositPaid).as("O status do depósito deve ser o novo valor")
                .isEqualTo(updateRequest.getDepositpaid());
    }

    @Test
    @Story("Atualizar reserva sem token")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve retornar 403 (Forbidden) ao tentar atualizar usando dados Faker mas com token inválido")
    void deveRetornarErroAoAtualizarSemToken() {
        BookingRequest request = BookingDataFactory.criarReservaValida();

        Response response = bookingClient.updateBooking(1, request, "token_invalido");

        assertThat(response.statusCode())
                .as("A API deve proibir a atualização (403 Forbidden) com credenciais inválidas")
                .isEqualTo(403);
    }
}