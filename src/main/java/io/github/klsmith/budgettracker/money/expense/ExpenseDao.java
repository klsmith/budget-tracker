package io.github.klsmith.budgettracker.money.expense;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import io.github.klsmith.budgettracker.dao.LongKeyDao;

/**
 * All of the database operations required for {@link Expense}.
 */
public interface ExpenseDao extends LongKeyDao<Expense> {

    /**
     * Create an new row in the {@link Expense} table, with the given data.<br/>
     * </br>
     * <strong>Note:</strong> the id field must be ignored in all implementations.
     * 
     * @return the newly created row from the database.
     */
    @Override
    Expense create(Expense expense);

    /**
     * Read all rows that are associated with the given date.
     * 
     * @return a list containing the row data, or an empty list if there weren't any
     *         rows with the given date.
     */
    List<Expense> read(LocalDate date);

    /**
     * Update an existing row with the given id in the {@link Expense} table, with
     * the given data.</br>
     * <strong>Note:</strong> the id field in the data must be ignored in all
     * implementations.
     * 
     * @return the updated row from the database.
     */
    Optional<Expense> update(long id, Expense expense);

    /**
     * Delete an existing row with the given id from the {@link Expense} table.
     */
    void delete(long id);

}
