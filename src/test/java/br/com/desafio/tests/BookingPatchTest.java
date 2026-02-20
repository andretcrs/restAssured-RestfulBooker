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

import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("Booking")
@Feature("Partial Update Booking")
@DisplayName("Testes de Atualização Parcial (PATCH)")
public class BookingPatchTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Story("Atualizar apenas nome e sobrenome com dados aleatórios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Deve alterar apenas o nome e sobrenome e validar o contrato e integridade dos dados")
    void deveAtualizarNomeESobrenomeParcialmente() {
        BookingRequest initialRequest = BookingDataFactory.criarReservaValida();
        Integer bookingId = bookingClient.createBooking(initialRequest).path("bookingid");

        String fakeFirstName = BookingDataFactory.novoNome();
        String fakeLastName = BookingDataFactory.novoSobrenome();

        Map<String, Object> partialUpdate = new HashMap<>();
        partialUpdate.put("firstname", fakeFirstName);
        partialUpdate.put("lastname", fakeLastName);

        Response response = bookingClient.patchBooking(bookingId, partialUpdate, getToken());

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-patch-schema.json"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.path("firstname").toString())
                    .as("First Name")
                    .isEqualTo(fakeFirstName);

            softly.assertThat(response.path("lastname").toString())
                    .as("Last Name")
                    .isEqualTo(fakeLastName);

            softly.assertThat((Integer) response.path("totalprice"))
                    .as("Preço Total")
                    .isEqualTo(initialRequest.getTotalprice());

            softly.assertThat((Boolean) response.path("depositpaid"))
                    .as("Deposit Paid")
                    .isEqualTo(initialRequest.getDepositpaid());

            softly.assertThat(response.path("bookingdates.checkin").toString())
                    .as("Check-in")
                    .isEqualTo(initialRequest.getBookingdates().getCheckin());

            softly.assertThat(response.path("bookingdates.checkout").toString())
                    .as("Check-out")
                    .isEqualTo(initialRequest.getBookingdates().getCheckout());
        });
    }
}