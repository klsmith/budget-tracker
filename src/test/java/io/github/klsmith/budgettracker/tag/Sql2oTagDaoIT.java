package io.github.klsmith.budgettracker.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.klsmith.budgettracker.money.MoneyEntry;
import io.github.klsmith.budgettracker.money.Sql2oMoneyEntryDao;
import io.github.klsmith.budgettracker.sql2o.Sql2oDaoIntegration;

class Sql2oTagDaoIT extends Sql2oDaoIntegration {

    private final Sql2oTagDao tagDao = new Sql2oTagDao(getSql2o());
    private final Sql2oMoneyEntryDao moneyEntryDao = new Sql2oMoneyEntryDao(getSql2o(), tagDao);
    private TagBuilder builder;

    @BeforeEach
    void setupBuilder() {
        builder = Tag.builder()
                .withId(1)
                .withName("Test");
    }

    @Test
    void testCreateSimpleString() {
        final Tag expected = builder.build();
        final Tag actual = tagDao.create(builder.getName());
        assertEquals(expected, actual);
    }

    @Test
    void testCreateSimpleData() {
        final Tag expected = builder.build();
        final Tag actual = tagDao.create(builder.build());
        assertEquals(expected, actual);
    }

    @Test
    void testReadById() {
        tagDao.create(builder.getName());
        final Optional<Tag> expected = Optional.of(builder.build());
        final Optional<Tag> actual = tagDao.read(1);
        assertEquals(expected, actual);
    }

    @Test
    void testReadByName() {
        tagDao.create(builder.getName());
        final Optional<Tag> expected = Optional.of(builder.build());
        final Optional<Tag> actual = tagDao.read(builder.getName());
        assertEquals(expected, actual);
    }

    @Test
    void testMapByMoneyEntry() {
        final MoneyEntry entry = MoneyEntry.builder()
                .withId(1)
                .withAmount(new BigDecimal("50.0000"))
                .withDate(LocalDate.of(1993, 8, 31))
                .build();
        moneyEntryDao.create(entry);
        tagDao.map(entry.getId(), "Test");
        tagDao.map(entry.getId(), "Test2");
        final List<Tag> expected = Arrays.asList(
                builder.build(),
                Tag.builder()
                        .withId(2)
                        .withName("Test2")
                        .build());
        final List<Tag> actual = tagDao.readForEntry(entry.getId());
        assertEquals(expected, actual);
    }

}
