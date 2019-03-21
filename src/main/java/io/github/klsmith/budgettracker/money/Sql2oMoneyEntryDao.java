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
 * Implementation of MoneyEntryDao using Sql2o.
 */
public class Sql2oMoneyEntryDao extends Sql2oDao implements MoneyEntryDao {

    private final Sql2oTagDao tagDao;

    /**
     * Construct using Sql2o object and Sql2oTagDao.
     */
    public Sql2oMoneyEntryDao(Sql2o sql2o, Sql2oTagDao tagDao) {
        super(sql2o);
        this.tagDao = tagDao;
    }

    @Override
    public MoneyEntry create(MoneyEntry moneyEntryData) {
        return transaction(connection -> create(connection, moneyEntryData));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oMoneyEntryDao#create(MoneyEntry)
     */
    public MoneyEntry create(Connection connection, MoneyEntry moneyEntryData) {
        connection.createQuery("INSERT INTO MoneyEntry (amount, date) VALUES (:amountParam, :dateParam);")
                .addParameter("amountParam", moneyEntryData.getAmount().toString())
                .addParameter("dateParam", moneyEntryData.getDate())
                .executeUpdate();
        final long id = getLastInsertId(connection);
        for (Tag tag : moneyEntryData.getTags()) {
            tagDao.map(connection, id, tag.getName());
        }
        return read(connection, id)
                .orElseThrow(() -> new DaoException("Could not read row immediately after creation."));
    }

    @Override
    public Optional<MoneyEntry> read(long id) {
        return transaction(connection -> read(connection, id));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oMoneyEntryDao#read(long)
     */
    public Optional<MoneyEntry> read(Connection connection, long id) {
        final MoneyEntryBuilder builder = connection
                .createQuery("SELECT * FROM MoneyEntry WHERE id = :idParam;")
                .addParameter("idParam", id)
                .executeAndFetchFirst(MoneyEntryBuilder.class);
        if (null == builder) {
            return Optional.empty();
        }
        final List<Tag> tags = tagDao.readForEntry(connection, id);
        return Optional.of(builder.withTags(tags).build());
    }

    @Override
    public List<MoneyEntry> read(LocalDate date) {
        return transaction(connection -> read(connection, date));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oMoneyEntryDao#read(LocalDate)
     */
    public List<MoneyEntry> read(Connection connection, LocalDate date) {
        final List<MoneyEntryBuilder> response = connection
                .createQuery("SELECT * FROM MoneyEntry WHERE date = :dateParam;")
                .addParameter("dateParam", date)
                .executeAndFetch(MoneyEntryBuilder.class);
        final List<MoneyEntry> results = new ArrayList<>(response.size());
        for (MoneyEntryBuilder builder : response) {
            final List<Tag> tags = tagDao.readForEntry(connection, builder.getId());
            results.add(builder.withTags(tags).build());
        }
        return results;
    }

    @Override
    public Optional<MoneyEntry> update(long id, MoneyEntry moneyEntryData) {
        return transaction(connection -> update(connection, id, moneyEntryData));
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oMoneyEntryDao#update(long, MoneyEntry)
     */
    public Optional<MoneyEntry> update(Connection connection, long id, MoneyEntry moneyEntryData) {
        connection.createQuery("UPDATE MoneyEntry SET amount = :amountParam, date = :dateParam WHERE id = :idParam;")
                .addParameter("amountParam", moneyEntryData.getAmount().toString())
                .addParameter("dateParam", moneyEntryData.getDate())
                .addParameter("idParam", id)
                .executeUpdate();
        for (Tag tag : moneyEntryData.getTags()) {
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
     * @see Sql2oMoneyEntryDao#update(long, MoneyEntry)
     */
    public void delete(Connection connection, long id) {
        connection.createQuery("DELETE FROM MoneyEntry WHERE id = :idParam;")
                .addParameter("idParam", id)
                .executeUpdate();
    }

}
