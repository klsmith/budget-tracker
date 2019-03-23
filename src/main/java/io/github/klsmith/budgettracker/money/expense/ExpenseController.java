package io.github.klsmith.budgettracker.money.expense;

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

public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    private static final String ID_PARAM = ":idParam";
    private static final String RESPONSE_TYPE_APPLICATION_JSON = "application/json";

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = Objects.requireNonNull(service,
                String.format("Cannot have a null %s.",
                        ExpenseService.class.getSimpleName()));
    }

    public void route(Service spark) {
        spark.post("/api/money/expense", this::postExpense);
        spark.get("/api/money/expense/" + ID_PARAM, this::getExpenseById);
        spark.put("/api/money/expense/" + ID_PARAM, this::putExpenseById);
        spark.delete("/api/money/expense/" + ID_PARAM, this::deleteExpenseById);
    }

    public String postExpense(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        return parseJson(request.body())
                .map(service::create)
                .map(this::toJson)
                .orElseThrow(RuntimeException::new);
    }

    public String getExpenseById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        return service.find(id)
                .map(this::toJson)
                .orElseGet(() -> {
                    response.status(404);
                    return String.format("{ \"message\"=\"Cannot find %s with id=%s\" }",
                            Expense.class.getSimpleName(), Long.valueOf(id));
                });
    }

    public String putExpenseById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        return parseJson(request.body())
                .flatMap(expense -> service.update(id, expense))
                .map(this::toJson)
                .orElseGet(() -> {
                    response.status(404);
                    return String.format("{ \"message\"=\"Cannot find %s with id=%s\" }",
                            Expense.class.getSimpleName(), Long.valueOf(id));
                });
    }

    public String deleteExpenseById(Request request, Response response) {
        response.type(RESPONSE_TYPE_APPLICATION_JSON);
        final long id = Long.parseLong(request.params(ID_PARAM));
        service.delete(id);
        return String.format("{ \"message\"=\"Successfully deleted %s with id=%s\" }",
                Expense.class.getSimpleName(), Long.valueOf(id));
    }

    private String toJson(Expense expense) {
        return new Gson().toJson(expense);
    }

    private Optional<Expense> parseJson(String json) {
        try {
            final JsonObject root = new JsonParser()
                    .parse(json)
                    .getAsJsonObject();
            final ExpenseBuilder builder = Expense.builder();
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
            logger.warn("Error parsing {}", Expense.class.getSimpleName(), e);
        }
        return Optional.empty();
    }

}
