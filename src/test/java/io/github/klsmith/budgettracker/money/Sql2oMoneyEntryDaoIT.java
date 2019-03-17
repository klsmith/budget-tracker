package io.github.klsmith.budgettracker.money;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.klsmith.budgettracker.sql2o.Sql2oDaoIntegration;
import io.github.klsmith.budgettracker.tag.Sql2oTagDao;
import io.github.klsmith.budgettracker.tag.Tag;

class Sql2oMoneyEntryDaoIT extends Sql2oDaoIntegration {

    private final Sql2oTagDao tagDao = new Sql2oTagDao(getSql2o());
    private final Sql2oMoneyEntryDao moneyEntryDao = new Sql2oMoneyEntryDao(getSql2o(), tagDao);
    private MoneyEntry testEntry;

    @BeforeEach
    void setupBuilder() {
        testEntry = MoneyEntry.builder()
                .withId(1)
                .withAmount(new BigDecimal("100.0000"))
                .withDate(LocalDate.of(1993, 8, 31))
                .withTag(Tag.builder()
                        .withId(1)
                        .withName("Food")
                        .build())
                .build();
    }

    @Test
    void testCreateSimple() {
        final MoneyEntry expected = testEntry;
        final MoneyEntry actual = moneyEntryDao.create(testEntry);
        assertEquals(expected, actual);
    }

    @Test
    void testReadById() {
        moneyEntryDao.create(testEntry);
        final Optional<MoneyEntry> expected = Optional.of(testEntry);
        final Optional<MoneyEntry> actual = moneyEntryDao.read(testEntry.getId());
        assertEquals(expected, actual);
    }

    @Test
    void testReadByDate() {
        moneyEntryDao.create(testEntry);
        final List<MoneyEntry> expected = Arrays.asList(testEntry);
        final List<MoneyEntry> actual = moneyEntryDao.read(testEntry.getDate());
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate() {
        moneyEntryDao.create(testEntry);
        final MoneyEntry newTestData = testEntry.asBuilder()
                .withAmount(new BigDecimal("120.0000"))
                .withDate(LocalDate.of(2018, 3, 16))
                .withTag(Tag.builder()
                        .withId(2)
                        .withName("Test")
                        .build())
                .build();
        final Optional<MoneyEntry> expected = Optional.of(newTestData);
        final Optional<MoneyEntry> actual = moneyEntryDao.update(testEntry.getId(), newTestData);
        assertEquals(expected, actual);
    }

    @Test
    void testDelete() {
        moneyEntryDao.create(testEntry);
        moneyEntryDao.delete(testEntry.getId());
        final Optional<MoneyEntry> expected = Optional.empty();
        final Optional<MoneyEntry> actual = moneyEntryDao.read(testEntry.getId());
        assertEquals(expected, actual);
    }

}
