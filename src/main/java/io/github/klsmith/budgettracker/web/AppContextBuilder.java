package io.github.klsmith.budgettracker.web;

import java.util.Objects;

import io.github.klsmith.budgettracker.money.ExpenseDao;
import io.github.klsmith.budgettracker.money.budget.BudgetDao;
import io.github.klsmith.budgettracker.tag.TagDao;

public class AppContextBuilder {

    private ExpenseDao expenseDao;
    private TagDao tagDao;
    private BudgetDao budgetDao;

    AppContextBuilder() {
        expenseDao = null;
        tagDao = null;
    }

    public AppContextBuilder withExpenseDao(ExpenseDao expenseDao) {
        this.expenseDao = Objects.requireNonNull(expenseDao,
                String.format("Cannot have a null %s.",
                        ExpenseDao.class.getSimpleName()));
        return this;
    }

    public ExpenseDao getExpenseDao() {
        return expenseDao;
    }

    public AppContextBuilder withTagDao(TagDao tagDao) {
        this.tagDao = Objects.requireNonNull(tagDao,
                "Cannot have a null TagDao.");
        return this;
    }

    public TagDao getTagDao() {
        return tagDao;
    }

    public AppContextBuilder withBudgetDao(BudgetDao budgetDao) {
        this.budgetDao = Objects.requireNonNull(budgetDao,
                "Cannot have a null BudgetDao");
        return this;
    }

    public BudgetDao getBudgetDao() {
        return budgetDao;
    }

    public AppContext build() {
        Objects.requireNonNull(expenseDao,
                String.format("Cannot build because the %s has not been set.",
                        ExpenseDao.class.getSimpleName()));
        Objects.requireNonNull(tagDao,
                "Cannot build because the TagDao has not been set.");
        Objects.requireNonNull(budgetDao,
                "Cannot builder because the BudgetDao has not been set.");
        return new AppContext(this);
    }

}
