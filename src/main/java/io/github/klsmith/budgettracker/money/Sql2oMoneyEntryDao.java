package io.github.klsmith.budgettracker.money;

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

    public final Sql2oTagDao tagDao;

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
        final MoneyEntry entry = connection
                .createQuery("SELECT * FROM MoneyEntry WHERE id = :idParam;")
                .addParameter("idParam", id)
                .executeAndFetchFirst(MoneyEntry.class);
        if (null == entry) {
            return Optional.empty();
        }
        final List<Tag> tags = tagDao.readForEntry(connection, id);
        return Optional.of(new MoneyEntry(entry, tags));
    }

}
