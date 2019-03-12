package io.github.klsmith.budgettracker.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.github.klsmith.budgettracker.money.MoneyEntry;
import io.github.klsmith.budgettracker.money.Sql2oMoneyEntryDao;
import io.github.klsmith.budgettracker.sql2o.Sql2oDaoIntegration;

class Sql2oTagDaoIT extends Sql2oDaoIntegration {

    private final Sql2oTagDao tagDao = new Sql2oTagDao(getSql2o());
    private final Sql2oMoneyEntryDao moneyEntryDao = new Sql2oMoneyEntryDao(getSql2o(), tagDao);

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
