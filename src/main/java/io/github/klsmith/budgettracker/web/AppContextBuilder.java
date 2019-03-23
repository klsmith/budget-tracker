package io.github.klsmith.budgettracker.web;

import java.util.Objects;

import io.github.klsmith.budgettracker.money.expense.ExpenseDao;
import io.github.klsmith.budgettracker.money.income.IncomeDao;
import io.github.klsmith.budgettracker.tag.TagDao;

public class AppContextBuilder {

    private ExpenseDao expenseDao;
    private IncomeDao incomeDao;
    private TagDao tagDao;

    AppContextBuilder() {
        expenseDao = null;
        incomeDao = null;
        tagDao = null;
    }

    public AppContextBuilder withExpenseDao(ExpenseDao expenseDao) {
        this.expenseDao = Objects.requireNonNull(expenseDao,
                String.format("Cannot have a null %s.", ExpenseDao.class.getSimpleName()));
        return this;
    }

    public ExpenseDao getExpenseDao() {
        return expenseDao;
    }

    public AppContextBuilder withIncomeDao(IncomeDao incomeDao) {
        this.incomeDao = Objects.requireNonNull(incomeDao,
                String.format("Cannot have a null %s.", IncomeDao.class.getSimpleName()));
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

    public AppContext build() {
        Objects.requireNonNull(expenseDao,
                String.format("Cannot build because the %s has not been set.", ExpenseDao.class.getSimpleName()));
        Objects.requireNonNull(incomeDao,
                String.format("Cannot build because the %s has not been set.", IncomeDao.class.getSimpleName()));
        Objects.requireNonNull(tagDao, "Cannot build because the TagDao has not been set.");
        return new AppContext(this);
    }

}
