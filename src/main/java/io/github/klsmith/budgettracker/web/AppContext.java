package io.github.klsmith.budgettracker.web;

import java.util.Objects;

import io.github.klsmith.budgettracker.money.budget.BudgetDao;
import io.github.klsmith.budgettracker.money.expense.ExpenseDao;
import io.github.klsmith.budgettracker.money.income.IncomeDao;
import io.github.klsmith.budgettracker.tag.TagDao;

public class AppContext {

	private final TagDao tagDao;
	private final ExpenseDao expenseDao;
	private final IncomeDao incomeDao;
	private final BudgetDao budgetDao;

	AppContext(AppContextBuilder builder) {
		tagDao = Objects.requireNonNull(builder.getTagDao());
		expenseDao = Objects.requireNonNull(builder.getExpenseDao());
		incomeDao = Objects.requireNonNull(builder.getIncomeDao());
		budgetDao = Objects.requireNonNull(builder.getBudgetDao());
	}

	public static AppContextBuilder builder() {
		return new AppContextBuilder();
	}

	public TagDao getTagDao() {
		return tagDao;
	}

	public ExpenseDao getExpenseDao() {
		return expenseDao;
	}

	public IncomeDao getIncomeDao() {
		return incomeDao;
	}

	public BudgetDao getBudgetDao() {
		return budgetDao;
	}

}
