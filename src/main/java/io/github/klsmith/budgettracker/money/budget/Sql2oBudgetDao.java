package io.github.klsmith.budgettracker.money.budget;

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
 * Implementation of {@link BudgetDao} using Sql2o.
 */
public class Sql2oBudgetDao extends Sql2oDao implements BudgetDao {

    private final Sql2oTagDao tagDao;

    /**
     * Construct using Sql2o object and Sql2oTagDao.
     */
    public Sql2oBudgetDao(Sql2o sql2o, Sql2oTagDao tagDao) {
        super(sql2o);
        this.tagDao = tagDao;
    }

    @Override
    public Budget create(Budget budgetData) {
        return transaction(connection -> create(connection, budgetData));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oBudgetDao#create(Budget)
     */
    public Budget create(Connection connection, Budget budgetData) {
        connection.createQuery("INSERT INTO Budget (amount, date) VALUES (:amountParam, :dateParam);")
                .addParameter("amountParam", budgetData.getAmount().toString())
                .addParameter("dateParam", budgetData.getDate().orElse(null))
                .executeUpdate();
        final long id = getLastInsertId(connection);
        for (Tag tag : budgetData.getTags()) {
            tagDao.mapBudget(connection, id, tag.getName());
        }
        return read(connection, id)
                .orElseThrow(() -> new DaoException("Could not read row immediately after creation."));
    }

    @Override
    public Optional<Budget> read(long id) {
        return transaction(connection -> read(connection, id));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oBudgetDao#read(long)
     */
    public Optional<Budget> read(Connection connection, long id) {
        final BudgetBuilder builder = connection
                .createQuery("SELECT * FROM Budget WHERE id = :idParam;")
                .addParameter("idParam", id)
                .executeAndFetchFirst(BudgetBuilder.class);
        if (null == builder) {
            return Optional.empty();
        }
        final List<Tag> tags = tagDao.readForBudget(connection, id);
        return Optional.of(builder.withTags(tags).build());
    }

    @Override
    public List<Budget> read(LocalDate date) {
        return transaction(connection -> read(connection, date));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oBudgetDao#read(LocalDate)
     */
    public List<Budget> read(Connection connection, LocalDate date) {
        final List<BudgetBuilder> response = connection
                .createQuery("SELECT * FROM Budget WHERE date = :dateParam;")
                .addParameter("dateParam", date)
                .executeAndFetch(BudgetBuilder.class);
        final List<Budget> results = new ArrayList<>(response.size());
        for (BudgetBuilder builder : response) {
            final List<Tag> tags = tagDao.readForBudget(connection, builder.getId());
            results.add(builder.withTags(tags).build());
        }
        return results;
    }

    @Override
    public Optional<Budget> update(long id, Budget budgetData) {
        return transaction(connection -> update(connection, id, budgetData));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oBudgetDao#update(long, Budget)
     */
    public Optional<Budget> update(Connection connection, long id, Budget budgetData) {
        connection.createQuery("UPDATE Budget SET amount = :amountParam, date = :dateParam WHERE id = :idParam;")
                .addParameter("amountParam", budgetData.getAmount().toString())
                .addParameter("dateParam", budgetData.getDate().orElse(null))
                .addParameter("idParam", id)
                .executeUpdate();
        for (Tag tag : budgetData.getTags()) {
            tagDao.mapBudget(connection, id, tag.getName());
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
     * @see Sql2oBudgetDao#delete(id)
     */
    public void delete(Connection connection, long id) {
        connection.createQuery("DELETE FROM Budget WHERE id = :idParam;")
                .addParameter("idParam", id)
                .executeUpdate();
    }

}
