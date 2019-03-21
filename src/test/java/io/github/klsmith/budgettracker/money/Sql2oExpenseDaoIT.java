package io.github.klsmith.budgettracker.money;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.klsmith.budgettracker.money.expense.Expense;
import io.github.klsmith.budgettracker.money.expense.Sql2oExpenseDao;
import io.github.klsmith.budgettracker.sql2o.Sql2oDaoIntegration;
import io.github.klsmith.budgettracker.tag.Sql2oTagDao;
import io.github.klsmith.budgettracker.tag.Tag;

class Sql2oExpenseDaoIT extends Sql2oDaoIntegration {

    private final Sql2oTagDao tagDao = new Sql2oTagDao(getSql2o());
    private final Sql2oExpenseDao expenseDao = new Sql2oExpenseDao(getSql2o(), tagDao);
    private Expense testExpense;

    @BeforeEach
    void setupBuilder() {
        testExpense = Expense.builder()
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
        final Expense expected = testExpense;
        final Expense actual = expenseDao.create(testExpense);
        assertEquals(expected, actual);
    }

    @Test
    void testReadById() {
        expenseDao.create(testExpense);
        final Optional<Expense> expected = Optional.of(testExpense);
        final Optional<Expense> actual = expenseDao.read(testExpense.getId());
        assertEquals(expected, actual);
    }

    @Test
    void testReadByDate() {
        expenseDao.create(testExpense);
        final List<Expense> expected = Arrays.asList(testExpense);
        final List<Expense> actual = expenseDao.read(testExpense.getDate());
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate() {
        expenseDao.create(testExpense);
        final Expense newTestData = testExpense.asBuilder()
                .withAmount(new BigDecimal("120.0000"))
                .withDate(LocalDate.of(2018, 3, 16))
                .withTag(Tag.builder()
                        .withId(2)
                        .withName("Test")
                        .build())
                .build();
        final Optional<Expense> expected = Optional.of(newTestData);
        final Optional<Expense> actual = expenseDao.update(testExpense.getId(), newTestData);
        assertEquals(expected, actual);
    }

    @Test
    void testDelete() {
        expenseDao.create(testExpense);
        expenseDao.delete(testExpense.getId());
        final Optional<Expense> expected = Optional.empty();
        final Optional<Expense> actual = expenseDao.read(testExpense.getId());
        assertEquals(expected, actual);
    }

}
