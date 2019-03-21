package io.github.klsmith.budgettracker.money;

import java.util.Optional;

import io.github.klsmith.budgettracker.web.AppContext;

public class ExpenseService {

    private final AppContext context;

    public ExpenseService(AppContext context) {
        this.context = context;
    }

    public Expense create(Expense expense) {
        return context.getExpenseDao().create(expense);
    }

    public Optional<Expense> find(long id) {
        return context.getExpenseDao().read(id);
    }

    public Optional<Expense> update(long id, Expense expense) {
        return context.getExpenseDao().update(id, expense);
    }

    public void delete(long id) {
        context.getExpenseDao().delete(id);
    }

}
