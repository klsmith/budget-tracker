package io.github.klsmith.budgettracker.web;

import java.util.Objects;

import io.github.klsmith.budgettracker.money.expense.ExpenseDao;
import io.github.klsmith.budgettracker.money.income.IncomeDao;
import io.github.klsmith.budgettracker.tag.TagDao;

public class AppContext {

    private final ExpenseDao expenseDao;
    private final IncomeDao incomeDao;

    private final TagDao tagDao;

    AppContext(AppContextBuilder builder) {
        expenseDao = Objects.requireNonNull(builder.getExpenseDao());
        incomeDao = Objects.requireNonNull(builder.getIncomeDao());
        tagDao = Objects.requireNonNull(builder.getTagDao());
    }

    public static AppContextBuilder builder() {
        return new AppContextBuilder();
    }

    public ExpenseDao getExpenseDao() {
        return expenseDao;
    }

    public IncomeDao getIncomeDao() {
        return incomeDao;
    }

    public TagDao getTagDao() {
        return tagDao;
    }

}
