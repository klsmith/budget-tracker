package io.github.klsmith.budgettracker.money;

import java.time.LocalDate;
import java.util.List;

import io.github.klsmith.budgettracker.dao.LongKeyDao;

/**
 * All of the database operations required for MoneyEntry.
 */
public interface MoneyEntryDao extends LongKeyDao<MoneyEntry> {

    /**
     * Create an new row in the MoneyEntry table, with the given data.<br/>
     * </br>
     * <strong>Note:</strong> the id field must be ignored in all implementations.
     * 
     * @return the newly created row from the database.
     */
    @Override
    MoneyEntry create(MoneyEntry moneyEntryData);

    /**
     * Read all rows that are associated with the given date.
     * 
     * @return a list containing the row data, or an empty list if there weren't any
     *         rows with the given date.
     */
    List<MoneyEntry> read(LocalDate date);

}
