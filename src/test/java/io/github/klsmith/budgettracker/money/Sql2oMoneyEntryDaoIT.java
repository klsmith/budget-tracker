package io.github.klsmith.budgettracker.money;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.github.klsmith.budgettracker.sql2o.Sql2oDaoIntegration;
import io.github.klsmith.budgettracker.tag.Sql2oTagDao;
import io.github.klsmith.budgettracker.tag.Tag;

class Sql2oMoneyEntryDaoIT extends Sql2oDaoIntegration {

    private final Sql2oTagDao tagDao = new Sql2oTagDao(getSql2o());
    private final Sql2oMoneyEntryDao moneyEntryDao = new Sql2oMoneyEntryDao(getSql2o(), tagDao);

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
