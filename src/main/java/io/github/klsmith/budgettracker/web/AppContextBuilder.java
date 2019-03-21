package io.github.klsmith.budgettracker.web;

import java.util.Objects;

import io.github.klsmith.budgettracker.money.MoneyEntryDao;
import io.github.klsmith.budgettracker.tag.TagDao;

public class AppContextBuilder {

    private MoneyEntryDao moneyEntryDao;
    private TagDao tagDao;

    AppContextBuilder() {
        moneyEntryDao = null;
        tagDao = null;
    }

    public AppContextBuilder withMoneyEntryDao(MoneyEntryDao moneyEntryDao) {
        this.moneyEntryDao = Objects.requireNonNull(moneyEntryDao,
                "Cannot have a null MoneyEntryDao.");
        return this;
    }

    public MoneyEntryDao getMoneyEntryDao() {
        return moneyEntryDao;
    }

    public AppContextBuilder withTagDao(TagDao tagDao) {
        this.tagDao = Objects.requireNonNull(tagDao,
                "Cannot have a null TagDao.");
        return this;
    }

    public TagDao getTagDao() {
        return tagDao;
    }

    public AppContext build() {
        Objects.requireNonNull(moneyEntryDao,
                "Cannot build because the MoneyEntryDao has not been set.");
        Objects.requireNonNull(tagDao,
                "Cannot build because the TagDao has not been set.");
        return new AppContext(this);
    }

}
