package io.github.klsmith.budgettracker.money.budget;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import io.github.klsmith.budgettracker.dao.LongKeyDao;

/**
 * All of the database operations required for {@link Budget}.
 */
public interface BudgetDao extends LongKeyDao<Budget> {

    /**
     * Create a new row in the {@link Budget} table, with the given data.<br/>
     * </br>
     * <strong>Note:</strong> the id field must be ignored in all implementations.
     * 
     * @return the newly created row from the database.
     */
    @Override
    Budget create(Budget budget);

    /**
     * Read all rows that are associated with the given date.
     * 
     * @return a list containing the row data, or an empty list if there weren't any
     *         rows with the given date.
     */
    List<Budget> read(LocalDate date);

    /**
     * Update an existing row with the given id in the {@link Budget} table, with
     * the given data.</br>
     * <strong>Note:</strong> the id field in the data must be ignored in all
     * implementations.
     * 
     * @return the updated row from the database.
     */
    Optional<Budget> update(long id, Budget budget);

    /**
     * Delete an existing row with the given id from the {@link Budget} table.
     */
    void delete(long id);

}
