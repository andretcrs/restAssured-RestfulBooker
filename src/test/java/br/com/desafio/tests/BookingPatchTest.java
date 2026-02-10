package br.com.desafio.tests;

import br.com.desafio.base.BaseTest;
import br.com.desafio.client.BookingClient;
import br.com.desafio.factory.BookingDataFactory;
import br.com.desafio.model.request.BookingRequest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Booking")
@Feature("Partial Update Booking")
@DisplayName("Testes de Atualização Parcial (PATCH)")
public class BookingPatchTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();


    @Test
    @Story("Atualizar apenas nome e sobrenome com dados aleatórios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve alterar apenas o nome e sobrenome usando Faker e validar a integridade dos outros campos")
    void deveAtualizarNomeESobrenomeParcialmente() {

        BookingRequest initialRequest = BookingDataFactory.criarReservaValida();
        Integer bookingId = bookingClient.createBooking(initialRequest).path("bookingid");


        String fakeFirstName = BookingDataFactory.novoNome();
        String fakeLastName = BookingDataFactory.novoSobrenome();

        Map<String, String> partialUpdate = new HashMap<>();
        partialUpdate.put("firstname", fakeFirstName);
        partialUpdate.put("lastname", fakeLastName);

        Response response = bookingClient.patchBooking(bookingId, partialUpdate, token);

        assertThat(response.statusCode()).isEqualTo(200);

        String returnedFirstName = response.path("firstname");
        String returnedLastName = response.path("lastname");
        Integer totalPrice = response.path("totalprice");

        assertThat(returnedFirstName).isEqualTo(fakeFirstName);
        assertThat(returnedLastName).isEqualTo(fakeLastName);

        assertThat(totalPrice)
                .as("O preço total não deve ser alterado em um PATCH de nome")
                .isEqualTo(initialRequest.getTotalprice());
    }
}