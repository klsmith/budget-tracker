package io.github.klsmith.budgettracker.money;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;
import spark.Response;
import spark.Service;

public class MoneyEntryController {

    private static final Logger logger = LoggerFactory.getLogger(MoneyEntryController.class);

    private static final String ID_PARAM = ":id";
    private static final String RESPONSE_TYPE_APPLICATION_JSON = "application/json";

    private MoneyEntryService service;

    public MoneyEntryController(MoneyEntryService service) {
        this.service = Objects.requireNonNull(service, "Cannot have a null MoneyEntryService.");
    }

    public void route(Service spark) {
        spark.post("/api/money/entry", this::postMoneyEntry);
        spark.get("/api/money/entry/" + ID_PARAM, this::getMoneyEntryById);
    }

    public String postMoneyEntry(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        return parseJson(request.body())
                .map(service::create)
                .map(this::toJson)
                .orElseThrow(RuntimeException::new);
    }

    public String getMoneyEntryById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        return service.find(id)
                .map(this::toJson)
                .orElseGet(() -> {
                    response.status(404);
                    return "{ \"message\"=\"Cannot find MoneyEntry with id=" + id + "\" }";
                });
    }

    private String toJson(MoneyEntry entry) {
        return new Gson().toJson(entry);
    }

    private Optional<MoneyEntry> parseJson(String json) {
        try {
            final JsonObject root = new JsonParser()
                    .parse(json)
                    .getAsJsonObject();
            final MoneyEntryBuilder builder = MoneyEntry.builder();
            builder.withAmount(root.get("amount").getAsBigDecimal());
            final JsonObject dateObj = root.get("date").getAsJsonObject();
            builder.withDate(LocalDate.of(
                    dateObj.get("year").getAsInt(),
                    dateObj.get("month").getAsInt(),
                    dateObj.get("day").getAsInt()));
            root.get("tags").getAsJsonArray()
                    .forEach(tagElement -> {
                        builder.withTag(tagElement.getAsString());
                    });
            return Optional.of(builder.build());
        }
        catch (RuntimeException e) {
            logger.warn("Error parsing MoneyEntry", e);
        }
        return Optional.empty();
    }

}
