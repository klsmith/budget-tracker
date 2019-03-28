package io.github.klsmith.budgettracker.web;

import org.sql2o.Sql2o;

import io.github.klsmith.budgettracker.money.expense.ExpenseController;
import io.github.klsmith.budgettracker.money.expense.ExpenseService;
import io.github.klsmith.budgettracker.money.expense.Sql2oExpenseDao;
import io.github.klsmith.budgettracker.money.income.IncomeController;
import io.github.klsmith.budgettracker.money.income.IncomeService;
import io.github.klsmith.budgettracker.money.income.Sql2oIncomeDao;
import io.github.klsmith.budgettracker.tag.Sql2oTagDao;
import io.github.klsmith.budgettracker.tag.TagController;
import io.github.klsmith.budgettracker.tag.TagService;
import io.github.klsmith.budgettracker.template.PageController;
import spark.Service;

public class Main {

	public static void main(String[] args) {
		final Main main = new Main();
		main.run();
	}

	public void run() {
		final Service spark = Service.ignite().port(4567);
		spark.staticFileLocation("/public");
		final Sql2o sql2o = new Sql2o("jdbc:mariadb://localhost:3445/test", "root", "");
		final Sql2oTagDao tagDao = new Sql2oTagDao(sql2o);
		final Sql2oExpenseDao expenseDao = new Sql2oExpenseDao(sql2o, tagDao);
		final Sql2oIncomeDao incomeDao = new Sql2oIncomeDao(sql2o, tagDao);
		final AppContext context = AppContext.builder()
				.withExpenseDao(expenseDao)
				.withTagDao(tagDao)
				.withIncomeDao(incomeDao)
				.build();
		final ExpenseService expenseService = new ExpenseService(context);
		final IncomeService incomeService = new IncomeService(context);
		final TagService tagService = new TagService(context);
		final ExpenseController expenseController = new ExpenseController(expenseService);
		final IncomeController incomeController = new IncomeController(incomeService);
		final TagController tagController = new TagController(tagService);
		final PageController pageController = new PageController(tagService);
		expenseController.route(spark);
		incomeController.route(spark);
		tagController.route(spark);
		pageController.route(spark);
	}

}
