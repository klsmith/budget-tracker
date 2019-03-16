package io.github.klsmith.budgettracker;

import org.sql2o.Sql2o;

import io.github.klsmith.budgettracker.money.MoneyEntryController;
import io.github.klsmith.budgettracker.money.MoneyEntryService;
import io.github.klsmith.budgettracker.money.Sql2oMoneyEntryDao;
import io.github.klsmith.budgettracker.tag.Sql2oTagDao;
import io.github.klsmith.budgettracker.web.AppContext;
import spark.Service;

public class Main {

    public static void main(String[] args) {
        final Main main = new Main();
        main.run();
    }

    public void run() {
        final Service spark = Service.ignite().port(4567);
        final Sql2o sql2o = new Sql2o(
                "jdbc:mariadb://localhost:3445/test",
                "root",
                "");
        final Sql2oTagDao tagDao = new Sql2oTagDao(sql2o);
        final Sql2oMoneyEntryDao moneyEntryDao = new Sql2oMoneyEntryDao(sql2o, tagDao);
        final AppContext context = AppContext.builder()
                .withMoneyEntryDao(moneyEntryDao)
                .withTagDao(tagDao)
                .build();
        final MoneyEntryService moneyEntryService = new MoneyEntryService(context);
        final MoneyEntryController moneyEntryController = new MoneyEntryController(moneyEntryService);
        moneyEntryController.route(spark);
        spark.get("/", (request, response) -> "Project: budget-tracker");
    }

}
