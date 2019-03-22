package io.github.klsmith.budgettracker.money.income;

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

class Sql2oIncomeDaoIT extends Sql2oDaoIntegration {

    private final Sql2oTagDao tagDao = new Sql2oTagDao(getSql2o());
    private final Sql2oIncomeDao incomeDao = new Sql2oIncomeDao(getSql2o(), tagDao);
    private Income testIncome;

    @BeforeEach
    void setupBuilder() {
        testIncome = Income.builder().withId(1).withAmount(new BigDecimal("100.0000"))
                .withDate(LocalDate.of(1993, 8, 31)).withTag(Tag.builder().withId(1).withName("Food").build()).build();
    }

    @Test
    void testCreateSimple() {
        final Income expected = testIncome;
        final Income actual = incomeDao.create(testIncome);
        assertEquals(expected, actual);
    }

    @Test
    void testReadById() {
        incomeDao.create(testIncome);
        final Optional<Income> expected = Optional.of(testIncome);
        final Optional<Income> actual = incomeDao.read(testIncome.getId());
        assertEquals(expected, actual);
    }

    @Test
    void testReadByDate() {
        incomeDao.create(testIncome);
        final List<Income> expected = Arrays.asList(testIncome);
        final List<Income> actual = incomeDao.read(testIncome.getDate());
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate() {
        incomeDao.create(testIncome);
        final Income newTestData = testIncome.asBuilder().withAmount(new BigDecimal("120.0000"))
                .withDate(LocalDate.of(2018, 3, 16)).withTag(Tag.builder().withId(2).withName("Test").build()).build();
        final Optional<Income> expected = Optional.of(newTestData);
        final Optional<Income> actual = incomeDao.update(testIncome.getId(), newTestData);
        assertEquals(expected, actual);
    }

    @Test
    void testDelete() {
        incomeDao.create(testIncome);
        incomeDao.delete(testIncome.getId());
        final Optional<Income> expected = Optional.empty();
        final Optional<Income> actual = incomeDao.read(testIncome.getId());
        assertEquals(expected, actual);
    }

}
