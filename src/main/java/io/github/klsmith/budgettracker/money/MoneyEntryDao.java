package io.github.klsmith.budgettracker.money;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * All of the database operations required for MoneyEntry.
 */
public interface MoneyEntryDao {

    /**
     * Create an new row in the MoneyEntry table, with the given data.<br/>
     * </br>
     * <strong>Note:</strong> the id field will be ignored.
     * 
     * @return the newly created row from the database.
     */
    public MoneyEntry create(MoneyEntry moneyEntryData);

    /**
     * Read the row in the MoneyEntry table that matches the given id.
     * 
     * @return an optional containing the row data, or an empty optional if there
     *         wasn't a row with that id.
     */
    public Optional<MoneyEntry> read(long id);

    /**
     * Read all rows that are associated with the given date.
     * 
     * @return a list containing the row data, or an empty list if there weren't any
     *         rows with the given date.
     */
    public List<MoneyEntry> read(LocalDate date);

}
