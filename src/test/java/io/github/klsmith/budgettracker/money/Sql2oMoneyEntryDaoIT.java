package io.github.klsmith.budgettracker.money;

import static io.github.klsmith.budgettracker.test.util.DatabaseSetupUtil.setupMoneyEntryTable;
import static io.github.klsmith.budgettracker.test.util.DatabaseSetupUtil.setupTagMoneyEntryMapTable;
import static io.github.klsmith.budgettracker.test.util.DatabaseSetupUtil.setupTagTable;
import static io.github.klsmith.budgettracker.test.util.DatabaseSetupUtil.transaction;
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
import io.github.klsmith.budgettracker.tag.Sql2oTagDao;
import io.github.klsmith.budgettracker.tag.Tag;
import io.github.klsmith.budgettracker.test.util.EmbeddedDatabase;

class Sql2oMoneyEntryDaoIT {

    private final EmbeddedDatabase database = new EmbeddedDatabase();
    private final Sql2o sql2o = getSql2o();
    private final Sql2oTagDao tagDao = new Sql2oTagDao(sql2o);
    private final Sql2oMoneyEntryDao moneyEntryDao = new Sql2oMoneyEntryDao(sql2o, tagDao);

    private Sql2o getSql2o() {
        return new Sql2o(
                database.getUrl(),
                database.getUser(),
                database.getPass());
    }

    @BeforeEach
    void setup() throws ManagedProcessException {
        database.setup();
        transaction(sql2o,
                connection -> {
                    setupMoneyEntryTable(connection);
                    setupTagTable(connection);
                    setupTagMoneyEntryMapTable(connection);
                });
    }

    @AfterEach
    void tearDown() throws ManagedProcessException {
        database.tearDown();
    }

    @Test
    void testCreateSimple() {
        final MoneyEntry expected = new MoneyEntry(1,
                new BigDecimal("100.0000"),
                LocalDate.of(1993, 8, 31),
                new Tag(1, "Food"));
        final MoneyEntry actual = moneyEntryDao.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void testReadById() {
        final MoneyEntry input = new MoneyEntry(1,
                new BigDecimal("100.0000"),
                LocalDate.of(1993, 8, 31),
                new Tag(1, "Food"));
        moneyEntryDao.create(input);
        final Optional<MoneyEntry> expected = Optional.of(input);
        final Optional<MoneyEntry> actual = moneyEntryDao.read(1);
        assertEquals(expected, actual);
    }

    @Test
    void testReadByDate() {
        final MoneyEntry input = new MoneyEntry(1,
                new BigDecimal("100.0000"),
                LocalDate.of(1993, 8, 31),
                new Tag(1, "Food"));
        moneyEntryDao.create(input);
        final List<MoneyEntry> expected = Arrays.asList(input);
        final List<MoneyEntry> actual = moneyEntryDao.read(LocalDate.of(1993, 8, 31));
        assertEquals(expected, actual);
    }

}
