package io.github.klsmith.budgettracker.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
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
    private Tag testTag;

    @BeforeEach
    void setupBuilder() {
        testTag = Tag.builder()
                .withId(1)
                .withName("Test")
                .build();
    }

    @Test
    void testCreateSimpleString() {
        final Tag expected = testTag;
        final Tag actual = tagDao.create(testTag.getName());
        assertEquals(expected, actual);
    }

    @Test
    void testCreateSimpleData() {
        final Tag expected = testTag;
        final Tag actual = tagDao.create(testTag);
        assertEquals(expected, actual);
    }

    @Test
    void testReadById() {
        tagDao.create(testTag.getName());
        final Optional<Tag> expected = Optional.of(testTag);
        final Optional<Tag> actual = tagDao.read(1);
        assertEquals(expected, actual);
    }

    @Test
    void testReadByName() {
        tagDao.create(testTag.getName());
        final Optional<Tag> expected = Optional.of(testTag);
        final Optional<Tag> actual = tagDao.read(testTag.getName());
        assertEquals(expected, actual);
    }

    @Test
    void testMapByMoneyEntry() {
        final MoneyEntry entry = moneyEntryDao.create(MoneyEntry.builder()
                .withAmount(new BigDecimal("50.0000"))
                .withDate(LocalDate.of(1993, 8, 31))
                .build());
        final Tag tagA = tagDao.map(entry.getId(), "Test");
        final Tag tagB = tagDao.map(entry.getId(), "Test2");
        final List<Tag> expected = Arrays.asList(tagA, tagB);
        final List<Tag> actual = tagDao.readForEntry(entry.getId());
        assertEquals(expected, actual);
    }

    @Test
    void testDelete() {
        final Tag tag = tagDao.create("Test");
        final long id = tag.getId();
        tagDao.delete(id);
        final Optional<Tag> expected = Optional.empty();
        final Optional<Tag> actual = tagDao.read(id);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteRemovesMapping() {
        final MoneyEntry entry = moneyEntryDao.create(MoneyEntry.builder()
                .withAmount(new BigDecimal("50.0000"))
                .withDate(LocalDate.of(1993, 8, 31))
                .build());
        final Tag tag = tagDao.map(entry.getId(), "Test");
        tagDao.delete(tag.getId());
        final List<Tag> expected = Collections.emptyList();
        final List<Tag> actual = tagDao.readForEntry(entry.getId());
        assertEquals(expected, actual);
    }

}
