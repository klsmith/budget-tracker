package io.github.klsmith.budgettracker.web;

import java.util.Objects;

import io.github.klsmith.budgettracker.money.budget.BudgetDao;
import io.github.klsmith.budgettracker.money.expense.ExpenseDao;
import io.github.klsmith.budgettracker.money.income.IncomeDao;
import io.github.klsmith.budgettracker.tag.TagDao;

public class AppContextBuilder {

	private TagDao tagDao;
	private ExpenseDao expenseDao;
	private IncomeDao incomeDao;
	private BudgetDao budgetDao;

	AppContextBuilder() {
		expenseDao = null;
		incomeDao = null;
		tagDao = null;
	}

	public AppContextBuilder withExpenseDao(ExpenseDao expenseDao) {
		this.expenseDao = Objects.requireNonNull(expenseDao, "Cannot have a null ExpenseDao.");
		return this;
	}

	public ExpenseDao getExpenseDao() {
		return expenseDao;
	}

	public AppContextBuilder withIncomeDao(IncomeDao incomeDao) {
		this.incomeDao = Objects.requireNonNull(incomeDao, "Cannot have a null IncomeDao.");
		return this;
	}

	public IncomeDao getIncomeDao() {
		return incomeDao;
	}

	public AppContextBuilder withTagDao(TagDao tagDao) {
		this.tagDao = Objects.requireNonNull(tagDao, "Cannot have a null TagDao.");
		return this;
	}

	public TagDao getTagDao() {
		return tagDao;
	}

	public AppContextBuilder withBudgetDao(BudgetDao budgetDao) {
		this.budgetDao = Objects.requireNonNull(budgetDao, "Cannot have a null BudgetDao");
		return this;
	}

	public BudgetDao getBudgetDao() {
		return budgetDao;
	}

	public AppContext build() {
		Objects.requireNonNull(tagDao, "Cannot build because the TagDao has not been set.");
		Objects.requireNonNull(expenseDao, "Cannot build because the ExpenseDao has not been set.");
		Objects.requireNonNull(incomeDao, "Cannot build because the IncomeDao has not been set.");
		Objects.requireNonNull(budgetDao, "Cannot builder because the BudgetDao has not been set.");
		return new AppContext(this);
	}

}
