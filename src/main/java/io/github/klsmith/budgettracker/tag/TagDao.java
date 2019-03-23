package io.github.klsmith.budgettracker.tag;

import java.util.List;
import java.util.Optional;

import io.github.klsmith.budgettracker.dao.LongKeyDao;
import io.github.klsmith.budgettracker.money.expense.Expense;
import io.github.klsmith.budgettracker.money.income.Income;

/**
 * All of the database operations required for Tag.
 */
public interface TagDao extends LongKeyDao<Tag> {

    /**
     * Create an new row in the Tag table, with the given data.<br/>
     * </br>
     * <strong>Note:</strong> the id field must be ignored in all implementations.
     * 
     * @return the newly created row from the database.
     */
    @Override
    public Tag create(Tag tag);

    /**
     * Create an new row in the Tag table, with the given name.
     * 
     * @return the newly created row from the database.
     */
    public Tag create(String tagName);

    /**
     * Associate the given tag name to the given {@link Expense} id.<br/>
     * 
     * @return the Tag object that was mapped.
     */
    public Tag map(long expenseId, String tagName);

    public List<Tag> map(long expenseId, List<Tag> tags);

    /**
     * Read the row in the Tag table that matches the given name.
     * 
     * @return an optional containing the row data, or an empty optional if there
     *         wasn't a row with that id.
     */
    public Optional<Tag> read(String tagName);

    /**
     * Read all of the rows in the Tag table that are mapped to the given
     * {@link Expense} id.
     * 
     * @return a list containing the row data, or an empty list if none of the rows
     *         match.
     */
    public List<Tag> readForExpense(long expenseId);

    /**
     * Read all of the rows in the Tag table that are mapped to the given
     * {@link Income} id.
     * 
     * @return a list containing the row data, or an empty list if none of the rows
     *         match.
     */
    public List<Tag> readForIncome(long incomeId);

    public void delete(long id);

}
