package io.github.klsmith.budgettracker.money.budget;

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

class Sql2oBudgetDaoIT extends Sql2oDaoIntegration {

    private final Sql2oTagDao tagDao = new Sql2oTagDao(getSql2o());
    private final Sql2oBudgetDao budgetDao = new Sql2oBudgetDao(getSql2o(), tagDao);
    private Budget testBudget;

    @BeforeEach
    void setupBuilder() {
        testBudget = Budget.builder()
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
        final Budget expected = testBudget;
        final Budget actual = budgetDao.create(testBudget);
        assertEquals(expected, actual);
    }

    @Test
    void testReadById() {
        budgetDao.create(testBudget);
        final Optional<Budget> expected = Optional.of(testBudget);
        final Optional<Budget> actual = budgetDao.read(testBudget.getId());
        assertEquals(expected, actual);
    }

    @Test
    void testReadByDate() {
        budgetDao.create(testBudget);
        final List<Budget> expected = Arrays.asList(testBudget);
        final List<Budget> actual = budgetDao.read(testBudget.getDate().get());
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate() {
        budgetDao.create(testBudget);
        final Budget newTestData = testBudget.asBuilder()
                .withAmount(new BigDecimal("120.0000"))
                .withDate(LocalDate.of(2018, 3, 16))
                .withTag(Tag.builder()
                        .withId(2)
                        .withName("Test")
                        .build())
                .build();
        final Optional<Budget> expected = Optional.of(newTestData);
        final Optional<Budget> actual = budgetDao.update(testBudget.getId(), newTestData);
        assertEquals(expected, actual);
    }

    @Test
    void testDelete() {
        budgetDao.create(testBudget);
        budgetDao.delete(testBudget.getId());
        final Optional<Budget> expected = Optional.empty();
        final Optional<Budget> actual = budgetDao.read(testBudget.getId());
        assertEquals(expected, actual);
    }

}
