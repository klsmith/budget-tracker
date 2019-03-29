package io.github.klsmith.budgettracker.money.budget;

import java.util.Optional;

import io.github.klsmith.budgettracker.web.AppContext;

public class BudgetService {

    private final AppContext context;

    public BudgetService(AppContext context) {
        this.context = context;
    }

    public Budget create(Budget budget) {
        return context.getBudgetDao().create(budget);
    }

    public Optional<Budget> find(long id) {
        return context.getBudgetDao().read(id);
    }

    public Optional<Budget> update(long id, Budget budget) {
        return context.getBudgetDao().update(id, budget);
    }

    public void delete(long id) {
        context.getBudgetDao().delete(id);
    }

}
