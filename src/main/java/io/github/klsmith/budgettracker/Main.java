package io.github.klsmith.budgettracker;

import org.sql2o.Sql2o;

import io.github.klsmith.budgettracker.money.budget.BudgetController;
import io.github.klsmith.budgettracker.money.budget.BudgetService;
import io.github.klsmith.budgettracker.money.budget.Sql2oBudgetDao;
import io.github.klsmith.budgettracker.money.expense.ExpenseController;
import io.github.klsmith.budgettracker.money.expense.ExpenseService;
import io.github.klsmith.budgettracker.money.expense.Sql2oExpenseDao;
import io.github.klsmith.budgettracker.money.income.IncomeController;
import io.github.klsmith.budgettracker.money.income.IncomeService;
import io.github.klsmith.budgettracker.money.income.Sql2oIncomeDao;
import io.github.klsmith.budgettracker.tag.Sql2oTagDao;
import io.github.klsmith.budgettracker.tag.TagController;
import io.github.klsmith.budgettracker.tag.TagService;
import io.github.klsmith.budgettracker.web.AppContext;
import spark.Service;

public class Main {

    public static void main(String[] args) {
        final Main main = new Main();
        main.run();
    }

    public void run() {
        final Service spark = Service.ignite().port(4567);
        final Sql2o sql2o = new Sql2o("jdbc:mariadb://localhost:3445/test", "root", "");
        final Sql2oTagDao tagDao = new Sql2oTagDao(sql2o);
        final Sql2oExpenseDao expenseDao = new Sql2oExpenseDao(sql2o, tagDao);
        final Sql2oIncomeDao incomeDao = new Sql2oIncomeDao(sql2o, tagDao);
        final Sql2oBudgetDao budgetDao = new Sql2oBudgetDao(sql2o, tagDao);
        final AppContext context = AppContext.builder()
                .withTagDao(tagDao)
                .withExpenseDao(expenseDao)
                .withIncomeDao(incomeDao)
                .withBudgetDao(budgetDao)
                .build();

        final TagService tagService = new TagService(context);
        final TagController tagController = new TagController(tagService);
        tagController.route(spark);

        final ExpenseService expenseService = new ExpenseService(context);
        final ExpenseController expenseController = new ExpenseController(expenseService);
        expenseController.route(spark);

        final IncomeService incomeService = new IncomeService(context);
        final IncomeController incomeController = new IncomeController(incomeService);
        incomeController.route(spark);

        final BudgetService budgetService = new BudgetService(context);
        final BudgetController budgetController = new BudgetController(budgetService);
        budgetController.route(spark);

        spark.get("/", (request, response) -> "Project: budget-tracker");
    }

}
