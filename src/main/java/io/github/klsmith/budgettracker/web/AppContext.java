package io.github.klsmith.budgettracker.web;

import java.util.Objects;

import io.github.klsmith.budgettracker.money.expense.ExpenseDao;
import io.github.klsmith.budgettracker.tag.TagDao;

public class AppContext {

    private final ExpenseDao expenseDao;
    private final TagDao tagDao;

    AppContext(AppContextBuilder builder) {
        expenseDao = Objects.requireNonNull(builder.getExpenseDao());
        tagDao = Objects.requireNonNull(builder.getTagDao());
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

}
