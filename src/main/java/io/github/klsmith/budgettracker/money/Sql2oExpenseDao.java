package io.github.klsmith.budgettracker.money;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import io.github.klsmith.budgettracker.dao.DaoException;
import io.github.klsmith.budgettracker.sql2o.Sql2oDao;
import io.github.klsmith.budgettracker.tag.Sql2oTagDao;
import io.github.klsmith.budgettracker.tag.Tag;

/**
 * Implementation of {@link ExpenseDao} using Sql2o.
 */
public class Sql2oExpenseDao extends Sql2oDao implements ExpenseDao {

    private final Sql2oTagDao tagDao;

    /**
     * Construct using Sql2o object and Sql2oTagDao.
     */
    public Sql2oExpenseDao(Sql2o sql2o, Sql2oTagDao tagDao) {
        super(sql2o);
        this.tagDao = tagDao;
    }

    @Override
    public Expense create(Expense expenseData) {
        return transaction(connection -> create(connection, expenseData));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oExpenseDao#create(Expense)
     */
    public Expense create(Connection connection, Expense expenseData) {
        connection.createQuery("INSERT INTO Expense (amount, date) VALUES (:amountParam, :dateParam);")
                .addParameter("amountParam", expenseData.getAmount().toString())
                .addParameter("dateParam", expenseData.getDate())
                .executeUpdate();
        final long id = getLastInsertId(connection);
        for (Tag tag : expenseData.getTags()) {
            tagDao.map(connection, id, tag.getName());
        }
        return read(connection, id)
                .orElseThrow(() -> new DaoException("Could not read row immediately after creation."));
    }

    @Override
    public Optional<Expense> read(long id) {
        return transaction(connection -> read(connection, id));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oExpenseDao#read(long)
     */
    public Optional<Expense> read(Connection connection, long id) {
        final ExpenseBuilder builder = connection
                .createQuery("SELECT * FROM Expense WHERE id = :idParam;")
                .addParameter("idParam", id)
                .executeAndFetchFirst(ExpenseBuilder.class);
        if (null == builder) {
            return Optional.empty();
        }
        final List<Tag> tags = tagDao.readForExpense(connection, id);
        return Optional.of(builder.withTags(tags).build());
    }

    @Override
    public List<Expense> read(LocalDate date) {
        return transaction(connection -> read(connection, date));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oExpenseDao#read(LocalDate)
     */
    public List<Expense> read(Connection connection, LocalDate date) {
        final List<ExpenseBuilder> response = connection
                .createQuery("SELECT * FROM Expense WHERE date = :dateParam;")
                .addParameter("dateParam", date)
                .executeAndFetch(ExpenseBuilder.class);
        final List<Expense> results = new ArrayList<>(response.size());
        for (ExpenseBuilder builder : response) {
            final List<Tag> tags = tagDao.readForExpense(connection, builder.getId());
            results.add(builder.withTags(tags).build());
        }
        return results;
    }

    @Override
    public Optional<Expense> update(long id, Expense expenseData) {
        return transaction(connection -> update(connection, id, expenseData));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oExpenseDao#update(long, Expense)
     */
    public Optional<Expense> update(Connection connection, long id, Expense expenseData) {
        connection.createQuery("UPDATE Expense SET amount = :amountParam, date = :dateParam WHERE id = :idParam;")
                .addParameter("amountParam", expenseData.getAmount().toString())
                .addParameter("dateParam", expenseData.getDate())
                .addParameter("idParam", id)
                .executeUpdate();
        for (Tag tag : expenseData.getTags()) {
            tagDao.map(connection, id, tag.getName());
        }
        return read(connection, id);
    }

    @Override
    public void delete(long id) {
        transaction(connection -> {
            delete(connection, id);
            return null;
        });
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oExpenseDao#update(long, Expense)
     */
    public void delete(Connection connection, long id) {
        connection.createQuery("DELETE FROM Expense WHERE id = :idParam;")
                .addParameter("idParam", id)
                .executeUpdate();
    }

}
