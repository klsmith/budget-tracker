package io.github.klsmith.budgettracker.money.income;

import java.util.Optional;

import io.github.klsmith.budgettracker.web.AppContext;

public class IncomeService {

    private final AppContext context;

    public IncomeService(AppContext context) {
        this.context = context;
    }

    public Income create(Income income) {
        return context.getIncomeDao().create(income);
    }

    public Optional<Income> find(long id) {
        return context.getIncomeDao().read(id);
    }

    public Optional<Income> update(long id, Income income) {
        return context.getIncomeDao().update(id, income);
    }

    public void delete(long id) {
        context.getIncomeDao().delete(id);
    }

}
