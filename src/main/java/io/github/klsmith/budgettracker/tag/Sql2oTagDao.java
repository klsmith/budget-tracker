package io.github.klsmith.budgettracker.tag;

import java.util.List;
import java.util.Optional;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import io.github.klsmith.budgettracker.dao.DaoException;
import io.github.klsmith.budgettracker.sql2o.Sql2oDao;

public class Sql2oTagDao extends Sql2oDao implements TagDao {

    public Sql2oTagDao(Sql2o sql2o) {
        super(sql2o);
    }

    @Override
    public Tag create(Tag tag) {
        return transaction(connection -> create(connection, tag));
    }

    public Tag create(Connection connection, Tag tag) {
        return create(connection, tag.getName());
    }

    @Override
    public Tag create(String tagName) {
        return transaction(connection -> create(connection, tagName));
    }

    public Tag create(Connection connection, String tagName) {
        connection.createQuery("INSERT INTO Tag (name) VALUES (:nameParam);")
                .addParameter("nameParam", tagName)
                .executeUpdate();
        final long id = getLastInsertId(connection);
        return read(connection, id)
                .orElseThrow(() -> new DaoException("Could not read row immediately after creation."));
    }

    @Override
    public Tag map(long entryId, String tagName) {
        return transaction(connection -> map(connection, entryId, tagName));
    }

    public Tag map(Connection connection, long entryId, String tagName) {
        final Tag tag = read(connection, tagName)
                .orElseGet(() -> create(connection, tagName));
        connection.createQuery("INSERT INTO TagMoneyEntry "
                + "(tagId, moneyEntryId) VALUES (:tagIdParam, :entryIdParam);")
                .addParameter("tagIdParam", tag.getId())
                .addParameter("entryIdParam", entryId)
                .executeUpdate();
        return tag;
    }

    @Override
    public Optional<Tag> read(String tagName) {
        return transaction(connection -> read(connection, tagName));
    }

    public Optional<Tag> read(Connection connection, String tagName) {
        return Optional.ofNullable(connection
                .createQuery("SELECT * FROM Tag WHERE name = :nameParam;")
                .addParameter("nameParam", tagName)
                .executeAndFetchFirst(Tag.class));
    }

    @Override
    public Optional<Tag> read(long id) {
        return transaction(connection -> read(connection, id));
    }

    public Optional<Tag> read(Connection connection, long id) {
        return Optional.ofNullable(connection
                .createQuery("SELECT * FROM Tag WHERE id = :idParam;")
                .addParameter("idParam", id)
                .executeAndFetchFirst(Tag.class));
    }

    public long getLastInsertId(Connection connection) {
        return connection.createQuery("SELECT LAST_INSERT_ID();")
                .executeAndFetchFirst(Long.class)
                .longValue();
    }

    @Override
    public List<Tag> readForEntry(long entryId) {
        return transaction(connection -> readForEntry(connection, entryId));
    }

    public List<Tag> readForEntry(Connection connection, long entryId) {
        return connection.createQuery("SELECT Tag.id, Tag.name FROM Tag\n"
                + "JOIN TagMoneyEntry ON TagMoneyEntry.tagId = Tag.id\n"
                + "WHERE TagMoneyEntry.moneyEntryId = :entryIdParam;")
                .addParameter("entryIdParam", entryId)
                .executeAndFetch(Tag.class);
    }

}
