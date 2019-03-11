package io.github.klsmith.budgettracker.money;

import static io.github.klsmith.budgettracker.test.util.DatabaseSetupUtil.transaction;
import static io.github.klsmith.budgettracker.test.util.DatabaseSetupUtil.setupMoneyEntryTable;
import static io.github.klsmith.budgettracker.test.util.DatabaseSetupUtil.setupTagMoneyEntryMapTable;
import static io.github.klsmith.budgettracker.test.util.DatabaseSetupUtil.setupTagTable;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;

import ch.vorburger.exec.ManagedProcessException;
import io.github.klsmith.budgettracker.tag.Tag;
import io.github.klsmith.budgettracker.tag.Sql2oTagDao;
import io.github.klsmith.budgettracker.test.util.EmbeddedDatabase;

class Sql2oMoneyEntryDaoIT {

    private static final EmbeddedDatabase database = new EmbeddedDatabase();
    private static final Sql2o sql2o = getSql2o();
    private static final Sql2oTagDao tagDao = new Sql2oTagDao(sql2o);
    private static final Sql2oMoneyEntryDao moneyEntryDao = new Sql2oMoneyEntryDao(sql2o, tagDao);

    private static Sql2o getSql2o() {
        return new Sql2o(
                database.getUrl(),
                database.getUser(),
                database.getPass());
    }

    @BeforeAll
    static void setup() throws ManagedProcessException {
        database.setup();
        transaction(sql2o,
                connection -> {
                    setupMoneyEntryTable(connection);
                    setupTagTable(connection);
                    setupTagMoneyEntryMapTable(connection);
                });
    }

    @AfterAll
    static void tearDown() throws ManagedProcessException {
        database.tearDown();
    }

    @Test
    void testCreateReadSimple() {
        final MoneyEntry expected = new MoneyEntry(1,
                new BigDecimal("100.0000"),
                LocalDate.now(),
                new Tag(1, "Food"));
        final MoneyEntry actual = moneyEntryDao.create(expected);
        assertEquals(expected, actual);
    }

}
