package br.com.desafio.client;

import br.com.desafio.model.request.BookingRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookingClient {

    private static final String BOOKING_ENDPOINT = "/booking";
    private static final String BOOKING_ID_ENDPOINT = "/booking/{id}";

    private static final String APP_JSON = "application/json";

    @Step("Buscar todos os bookings")
    public Response getAllBookings() {
        return given()
                .header("Accept", APP_JSON)
                .when()
                .get(BOOKING_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Buscar bookings por nome: {firstname} {lastname}")
    public Response getBookingsByName(String firstname, String lastname) {
        return given()
                .header("Accept", APP_JSON)
                .queryParam("firstname", firstname)
                .queryParam("lastname", lastname)
                .when()
                .get(BOOKING_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Buscar bookings por período: checkin={checkin}, checkout={checkout}")
    public Response getBookingsByDate(String checkin, String checkout) {
        return given()
                .header("Accept", APP_JSON)
                .queryParam("checkin", checkin)
                .queryParam("checkout", checkout)
                .when()
                .get(BOOKING_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Buscar booking por ID: {id}")
    public Response getBookingById(int id) {
        return given()
                .header("Accept", APP_JSON)
                .pathParam("id", id)
                .when()
                .get(BOOKING_ID_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Criar uma nova reserva")
    public Response createBooking(BookingRequest bookingRequest) {
        return given()
                .header("Content-Type", APP_JSON)
                .header("Accept", APP_JSON)
                .body(bookingRequest)
                .when()
                .post(BOOKING_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Atualizar reserva com ID: {id}")
    public Response updateBooking(int id, BookingRequest bookingRequest, String token) {
        return given()
                .header("Content-Type", APP_JSON)
                .header("Accept", APP_JSON)
                .header("Cookie", "token=" + token)
                .pathParam("id", id)
                .body(bookingRequest)
                .when()
                .put(BOOKING_ID_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Atualização parcial da reserva ID: {id}")
    public Response patchBooking(int id, Object bookingPart, String token) {
        return given()
                .header("Content-Type", APP_JSON)
                .header("Accept", APP_JSON)
                .header("Cookie", "token=" + token)
                .pathParam("id", id)
                .body(bookingPart)
                .when()
                .patch(BOOKING_ID_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Excluir reserva com ID: {id}")
    public Response deleteBooking(int id, String token) {
        return given()
                .header("Content-Type", APP_JSON)
                .header("Cookie", "token=" + token)
                .pathParam("id", id)
                .when()
                .delete(BOOKING_ID_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}