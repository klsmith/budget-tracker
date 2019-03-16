package io.github.klsmith.budgettracker.web;

import java.util.Objects;

import io.github.klsmith.budgettracker.money.MoneyEntryDao;
import io.github.klsmith.budgettracker.tag.TagDao;

public class AppContext {

    private final MoneyEntryDao moneyEntryDao;
    private final TagDao tagDao;

    AppContext(AppContextBuilder builder) {
        moneyEntryDao = Objects.requireNonNull(builder.getMoneyEntryDao());
        tagDao = Objects.requireNonNull(builder.getTagDao());
    }

    public static AppContextBuilder builder() {
        return new AppContextBuilder();
    }

    public MoneyEntryDao getMoneyEntryDao() {
        return moneyEntryDao;
    }

    public TagDao getTagDao() {
        return tagDao;
    }

}
