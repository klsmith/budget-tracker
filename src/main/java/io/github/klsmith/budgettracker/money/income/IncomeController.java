package io.github.klsmith.budgettracker.money.income;

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

public class IncomeController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeController.class);

    private static final String ID_PARAM = ":idParam";
    private static final String RESPONSE_TYPE_APPLICATION_JSON = "application/json";

    private final IncomeService service;

    public IncomeController(IncomeService service) {
        this.service = Objects.requireNonNull(service,
                String.format("Cannot have a null %s.", IncomeService.class.getSimpleName()));
    }

    public void route(Service spark) {
        spark.post("/api/money/income", this::postIncome);
        spark.get("/api/money/income/" + ID_PARAM, this::getIncomeById);
        spark.put("/api/money/income/" + ID_PARAM, this::putIncomeById);
        spark.delete("/api/money/income/" + ID_PARAM, this::deleteIncomeById);
    }

    public String postIncome(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        return parseJson(request.body()).map(service::create).map(this::toJson).orElseThrow(RuntimeException::new);
    }

    public String getIncomeById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        return service.find(id).map(this::toJson).orElseGet(() -> {
            response.status(404);
            return String.format("{ \"message\"=\"Cannot find %s with id=%s\" }", Income.class.getSimpleName(),
                    Long.valueOf(id));
        });
    }

    public String putIncomeById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        return parseJson(request.body()).flatMap(income -> service.update(id, income)).map(this::toJson)
                .orElseGet(() -> {
                    response.status(404);
                    return String.format("{ \"message\"=\"Cannot find %s with id=%s\" }", Income.class.getSimpleName(),
                            Long.valueOf(id));
                });
    }

    public String deleteIncomeById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        service.delete(id);
        return String.format("{ \"message\"=\"Successfully deleted %s with id=%s\" }", Income.class.getSimpleName(),
                Long.valueOf(id));
    }

    private String toJson(Income income) {
        return new Gson().toJson(income);
    }

    private Optional<Income> parseJson(String json) {
        try {
            final JsonObject root = new JsonParser().parse(json).getAsJsonObject();
            final IncomeBuilder builder = Income.builder();
            builder.withAmount(root.get("amount").getAsBigDecimal());
            final JsonObject dateObj = root.get("date").getAsJsonObject();
            builder.withDate(LocalDate.of(dateObj.get("year").getAsInt(), dateObj.get("month").getAsInt(),
                    dateObj.get("day").getAsInt()));
            root.get("tags").getAsJsonArray().forEach(tagElement -> {
                builder.withTag(tagElement.getAsString());
            });
            return Optional.of(builder.build());
        } catch (RuntimeException e) {
            logger.warn("Error parsing {}", Income.class.getSimpleName(), e);
        }
        return Optional.empty();
    }

}
