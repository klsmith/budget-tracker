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
    private MoneyEntryBuilder builder;

    @BeforeEach
    void setupBuilder() {
        builder = MoneyEntry.builder()
                .withId(1)
                .withAmount(new BigDecimal("100.0000"))
                .withDate(LocalDate.of(1993, 8, 31))
                .withTag(Tag.builder()
                        .withId(1)
                        .withName("Food")
                        .build());
    }

    @Test
    void testCreateSimple() {
        final MoneyEntry expected = builder.build();
        final MoneyEntry actual = moneyEntryDao.create(builder.build());
        assertEquals(expected, actual);
    }

    @Test
    void testReadById() {
        moneyEntryDao.create(builder.build());
        final Optional<MoneyEntry> expected = Optional.of(builder.build());
        final Optional<MoneyEntry> actual = moneyEntryDao.read(builder.getId());
        assertEquals(expected, actual);
    }

    @Test
    void testReadByDate() {
        moneyEntryDao.create(builder.build());
        final List<MoneyEntry> expected = Arrays.asList(builder.build());
        final List<MoneyEntry> actual = moneyEntryDao.read(builder.getDate());
        assertEquals(expected, actual);
    }

}
