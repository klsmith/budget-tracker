package io.github.klsmith.budgettracker.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;

import ch.vorburger.exec.ManagedProcessException;
import io.github.klsmith.budgettracker.money.MoneyEntry;
import io.github.klsmith.budgettracker.money.Sql2oMoneyEntryDao;
import io.github.klsmith.budgettracker.test.util.DatabaseSchemaConstructor;
import io.github.klsmith.budgettracker.test.util.EmbeddedDatabase;

class Sql2oTagDaoIT {

    private final EmbeddedDatabase database = new EmbeddedDatabase();
    private final Sql2o sql2o = getSql2o();
    private final Sql2oTagDao tagDao = new Sql2oTagDao(sql2o);
    private final Sql2oMoneyEntryDao moneyEntryDao = new Sql2oMoneyEntryDao(sql2o, tagDao);
    private final DatabaseSchemaConstructor constructor = new DatabaseSchemaConstructor(sql2o);

    private Sql2o getSql2o() {
        return new Sql2o(
                database.getUrl(),
                database.getUser(),
                database.getPass());
    }

    @BeforeEach
    void setup() throws ManagedProcessException {
        database.setup();
        constructor.transaction(connection -> {
            constructor.setupMoneyEntryTable(connection);
            constructor.setupTagTable(connection);
            constructor.setupTagMoneyEntryMapTable(connection);
            return null;
        });
    }

    @AfterEach
    void tearDown() throws ManagedProcessException {
        database.tearDown();
    }

    @Test
    void testCreateSimpleString() {
        final Tag expected = new Tag(1, "Test");
        final Tag actual = tagDao.create("Test");
        assertEquals(expected, actual);
    }

    @Test
    void testCreateSimpleData() {
        final Tag expected = new Tag(1, "Test");
        final Tag actual = tagDao.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void testReadById() {
        tagDao.create("Test");
        final Optional<Tag> expected = Optional.of(new Tag(1, "Test"));
        final Optional<Tag> actual = tagDao.read(1);
        assertEquals(expected, actual);
    }

    @Test
    void testReadByName() {
        tagDao.create("Test");
        final Optional<Tag> expected = Optional.of(new Tag(1, "Test"));
        final Optional<Tag> actual = tagDao.read("Test");
        assertEquals(expected, actual);
    }

    @Test
    void testMapByMoneyEntry() {
        final MoneyEntry entry = new MoneyEntry(1,
                new BigDecimal("50.0000"),
                LocalDate.of(1993, 8, 31));
        moneyEntryDao.create(entry);
        tagDao.map(entry.getId(), "Test");
        tagDao.map(entry.getId(), "Test2");
        final List<Tag> expected = Arrays.asList(
                new Tag(1, "Test"),
                new Tag(2, "Test2"));
        final List<Tag> actual = tagDao.readForEntry(entry.getId());
        assertEquals(expected, actual);
    }

}
