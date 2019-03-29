package io.github.klsmith.budgettracker.money.budget;

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

public class BudgetController {

    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);

    private static final String ID_PARAM = ":idParam";
    private static final String RESPONSE_TYPE_APPLICATION_JSON = "application/json";

    private final BudgetService service;

    public BudgetController(BudgetService service) {
        this.service = Objects.requireNonNull(service,
                String.format("Cannot have a null %s.",
                        BudgetService.class.getSimpleName()));
    }

    public void route(Service spark) {
        spark.post("/api/money/budget", this::postBudget);
        spark.get("/api/money/budget/" + ID_PARAM, this::getBudgetById);
        spark.put("/api/money/budget/" + ID_PARAM, this::putBudgetById);
        spark.delete("/api/money/budget/" + ID_PARAM, this::deleteBudgetById);
    }

    public String postBudget(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        return parseJson(request.body())
                .map(service::create)
                .map(this::toJson)
                .orElseThrow(RuntimeException::new);
    }

    public String getBudgetById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        return service.find(id)
                .map(this::toJson)
                .orElseGet(() -> {
                    response.status(404);
                    return String.format("{ \"message\"=\"Cannot find %s with id=%s\" }",
                            Budget.class.getSimpleName(), Long.valueOf(id));
                });
    }

    public String putBudgetById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        return parseJson(request.body())
                .flatMap(budget -> service.update(id, budget))
                .map(this::toJson)
                .orElseGet(() -> {
                    response.status(404);
                    return String.format("{ \"message\"=\"Cannot find %s with id=%s\" }",
                            Budget.class.getSimpleName(), Long.valueOf(id));
                });
    }

    public String deleteBudgetById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        service.delete(id);
        return String.format("{ \"message\"=\"Successfully deleted %s with id=%s\" }",
                Budget.class.getSimpleName(), Long.valueOf(id));
    }

    private String toJson(Budget budget) {
        return new Gson().toJson(budget);
    }

    private Optional<Budget> parseJson(String json) {
        try {
            final JsonObject root = new JsonParser()
                    .parse(json)
                    .getAsJsonObject();
            final BudgetBuilder builder = Budget.builder();
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
            logger.warn("Error parsing {}", Budget.class.getSimpleName(), e);
        }
        return Optional.empty();
    }

}
