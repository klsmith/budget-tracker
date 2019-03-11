package io.github.klsmith.budgettracker.money;

import java.util.Optional;

public interface MoneyEntryDao {

    public MoneyEntry create(MoneyEntry moneyEntryData);

    public Optional<MoneyEntry> read(long id);

}
