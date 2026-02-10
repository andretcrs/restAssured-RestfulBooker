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
@Feature("Create Booking")
@DisplayName("Testes de Criação de Reserva")
public class BookingPostTest extends BaseTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    @Story("Criar reserva com sucesso usando dados aleatórios")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Deve criar uma nova reserva usando Faker e validar se os dados retornados são os mesmos enviados")
    void deveCriarReservaComSucesso() {
        BookingRequest requestBody = BookingDataFactory.criarReservaValida();

        Response response = bookingClient.createBooking(requestBody);

        assertThat(response.statusCode()).isEqualTo(200);


        Integer bookingId = response.path("bookingid");
        String firstname = response.path("booking.firstname");
        String lastname = response.path("booking.lastname");
        Integer totalprice = response.path("booking.totalprice");
        Boolean depositpaid = response.path("booking.depositpaid");

        assertThat(bookingId).as("O ID da reserva não deve ser nulo").isNotNull();

        assertThat(firstname)
                .as("O nome deve coincidir com o gerado pelo Faker")
                .isEqualTo(requestBody.getFirstname());

        assertThat(lastname)
                .as("O sobrenome deve coincidir com o gerado pelo Faker")
                .isEqualTo(requestBody.getLastname());

        assertThat(totalprice)
                .as("O preço deve coincidir com o gerado pelo Faker")
                .isEqualTo(requestBody.getTotalprice());

        assertThat(depositpaid)
                .as("O status do depósito deve coincidir com o gerado pelo Faker")
                .isEqualTo(requestBody.getDepositpaid());

        String checkin = response.path("booking.bookingdates.checkin");
        assertThat(checkin).isEqualTo(requestBody.getBookingdates().getCheckin());
    }
}