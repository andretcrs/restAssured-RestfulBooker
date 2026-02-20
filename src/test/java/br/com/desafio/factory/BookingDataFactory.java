package br.com.desafio.factory;

import br.com.desafio.model.request.BookingRequest;
import br.com.desafio.model.response.BookingDatesResponse;
import net.datafaker.Faker;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.Date;

public class BookingDataFactory {

    private static final Faker faker = new Faker(new Locale("pt-BR"));
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static BookingRequest criarReservaValida() {
        return new BookingRequest(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().numberBetween(100, 5000),
                faker.bool().bool(),
                criarDatasValidas(),
                faker.options().option("Breakfast", "Dinner", "WiFi", "Late Checkout")
        );
    }

    public static BookingDatesResponse criarDatasValidas() {
        BookingDatesResponse dates = new BookingDatesResponse();

        Date checkinDate = faker.date().future(10, 1, TimeUnit.DAYS);
        Date checkoutDate = faker.date().future(5, TimeUnit.DAYS, checkinDate);

        dates.setCheckin(formatarData(checkinDate));
        dates.setCheckout(formatarData(checkoutDate));

        return dates;
    }


    private static String formatarData(Date data) {
        return data.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(formatter);
    }

    public static String novoNome() { return faker.name().firstName(); }
    public static String novoSobrenome() { return faker.name().lastName(); }
    public static int novoPreco() { return faker.number().numberBetween(100, 5000); }
}