package io.github.klsmith.budgettracker.money.income;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import io.github.klsmith.budgettracker.dao.LongKeyDao;

/**
 * All of the database operations required for {@link Income}.
 */
public interface IncomeDao extends LongKeyDao<Income> {

    /**
     * Create an new row in the {@link Income} table, with the given data.<br/>
     * </br>
     * <strong>Note:</strong> the id field must be ignored in all implementations.
     * 
     * @return the newly created row from the database.
     */
    @Override
    Income create(Income income);

    /**
     * Read all rows that are associated with the given date.
     * 
     * @return a list containing the row data, or an empty list if there weren't any
     *         rows with the given date.
     */
    List<Income> read(LocalDate date);

    /**
     * Update an existing row with the given id in the {@link Income} table, with
     * the given data.</br>
     * <strong>Note:</strong> the id field in the data must be ignored in all
     * implementations.
     * 
     * @return the updated row from the database.
     */
    Optional<Income> update(long id, Income income);

    /**
     * Delete an existing row with the given id from the {@link Income} table.
     */
    void delete(long id);

}
