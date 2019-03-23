package io.github.klsmith.budgettracker.money.income;

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
 * Implementation of {@link IncomeDao} using Sql2o.
 */
public class Sql2oIncomeDao extends Sql2oDao implements IncomeDao {

    private final Sql2oTagDao tagDao;

    /**
     * Construct using Sql2o object and Sql2oTagDao.
     */
    public Sql2oIncomeDao(Sql2o sql2o, Sql2oTagDao tagDao) {
        super(sql2o);
        this.tagDao = tagDao;
    }

    @Override
    public Income create(Income incomeData) {
        return transaction(connection -> create(connection, incomeData));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oIncomeDao#create(Income)
     */
    public Income create(Connection connection, Income incomeData) {
        connection.createQuery("INSERT INTO Income (amount, date) VALUES (:amountParam, :dateParam);")
                .addParameter("amountParam", incomeData.getAmount().toString())
                .addParameter("dateParam", incomeData.getDate()).executeUpdate();
        final long id = getLastInsertId(connection);
        for (Tag tag : incomeData.getTags()) {
            tagDao.mapIncome(connection, id, tag.getName());
        }
        return read(connection, id)
                .orElseThrow(() -> new DaoException("Could not read row immediately after creation."));
    }

    @Override
    public Optional<Income> read(long id) {
        return transaction(connection -> read(connection, id));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oIncomeDao#read(long)
     */
    public Optional<Income> read(Connection connection, long id) {
        final IncomeBuilder builder = connection.createQuery("SELECT * FROM Income WHERE id = :idParam;")
                .addParameter("idParam", id).executeAndFetchFirst(IncomeBuilder.class);
        if (null == builder) {
            return Optional.empty();
        }
        final List<Tag> tags = tagDao.readForIncome(connection, id);
        return Optional.of(builder.withTags(tags).build());
    }

    @Override
    public List<Income> read(LocalDate date) {
        return transaction(connection -> read(connection, date));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oIncomeDao#read(LocalDate)
     */
    public List<Income> read(Connection connection, LocalDate date) {
        final List<IncomeBuilder> response = connection.createQuery("SELECT * FROM Income WHERE date = :dateParam;")
                .addParameter("dateParam", date).executeAndFetch(IncomeBuilder.class);
        final List<Income> results = new ArrayList<>(response.size());
        for (IncomeBuilder builder : response) {
            final List<Tag> tags = tagDao.readForIncome(connection, builder.getId());
            results.add(builder.withTags(tags).build());
        }
        return results;
    }

    @Override
    public Optional<Income> update(long id, Income incomeData) {
        return transaction(connection -> update(connection, id, incomeData));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oIncomeDao#update(long, Income)
     */
    public Optional<Income> update(Connection connection, long id, Income incomeData) {
        connection.createQuery("UPDATE Income SET amount = :amountParam, date = :dateParam WHERE id = :idParam;")
                .addParameter("amountParam", incomeData.getAmount().toString())
                .addParameter("dateParam", incomeData.getDate()).addParameter("idParam", id).executeUpdate();
        for (Tag tag : incomeData.getTags()) {
            tagDao.mapIncome(connection, id, tag.getName());
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
     * @see Sql2oIncomeDao#update(long, Income)
     */
    public void delete(Connection connection, long id) {
        connection.createQuery("DELETE FROM Income WHERE id = :idParam;").addParameter("idParam", id).executeUpdate();
    }

}
