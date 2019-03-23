package io.github.klsmith.budgettracker;

import org.sql2o.Sql2o;

import io.github.klsmith.budgettracker.money.ExpenseController;
import io.github.klsmith.budgettracker.money.ExpenseService;
import io.github.klsmith.budgettracker.money.Sql2oExpenseDao;
import io.github.klsmith.budgettracker.money.budget.Sql2oBudgetDao;
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
        final Sql2oExpenseDao expenseDao = new Sql2oExpenseDao(sql2o, tagDao);
        final Sql2oBudgetDao budgetDao = new Sql2oBudgetDao(sql2o, tagDao);
        final AppContext context = AppContext.builder()
                .withExpenseDao(expenseDao)
                .withTagDao(tagDao)
                .withBudgetDao(budgetDao)
                .build();
        final ExpenseService expenseService = new ExpenseService(context);
        final ExpenseController expenseController = new ExpenseController(expenseService);
        expenseController.route(spark);
        spark.get("/", (request, response) -> "Project: budget-tracker");
    }

}
