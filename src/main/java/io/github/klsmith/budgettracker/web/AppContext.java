package io.github.klsmith.budgettracker.web;

import java.util.Objects;

import io.github.klsmith.budgettracker.money.ExpenseDao;
import io.github.klsmith.budgettracker.money.budget.BudgetDao;
import io.github.klsmith.budgettracker.tag.TagDao;

public class AppContext {

    private final ExpenseDao expenseDao;
    private final TagDao tagDao;
    private final BudgetDao budgetDao;

    AppContext(AppContextBuilder builder) {
        expenseDao = Objects.requireNonNull(builder.getExpenseDao());
        tagDao = Objects.requireNonNull(builder.getTagDao());
        budgetDao = Objects.requireNonNull(builder.getBudgetDao());
    }

    public static AppContextBuilder builder() {
        return new AppContextBuilder();
    }

    public ExpenseDao getExpenseDao() {
        return expenseDao;
    }

    public TagDao getTagDao() {
        return tagDao;
    }

    public BudgetDao getBudgetDao() {
        return budgetDao;
    }

}
