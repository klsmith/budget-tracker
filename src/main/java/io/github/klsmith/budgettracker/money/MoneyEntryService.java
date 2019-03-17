package io.github.klsmith.budgettracker.money;

import java.util.Optional;

import io.github.klsmith.budgettracker.web.AppContext;

public class MoneyEntryService {

    private final AppContext context;

    public MoneyEntryService(AppContext context) {
        this.context = context;
    }

    public MoneyEntry create(MoneyEntry entry) {
        return context.getMoneyEntryDao().create(entry);
    }

    public Optional<MoneyEntry> find(long id) {
        return context.getMoneyEntryDao().read(id);
    }

    public Optional<MoneyEntry> update(long id, MoneyEntry entry) {
        return context.getMoneyEntryDao().update(id, entry);
    }

    public void delete(long id) {
        context.getMoneyEntryDao().delete(id);
    }
    
}
