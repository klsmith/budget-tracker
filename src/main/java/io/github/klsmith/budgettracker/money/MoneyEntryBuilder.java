package io.github.klsmith.budgettracker.money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.github.klsmith.budgettracker.tag.Tag;

public class MoneyEntryBuilder {

    private long id;
    private BigDecimal amount;
    private LocalDate date;
    private final List<Tag> tags;

    MoneyEntryBuilder(MoneyEntry entry) {
        this(entry.getId(),
                entry.getAmount(),
                entry.getDate(),
                entry.getTags());
    }

    MoneyEntryBuilder(long id, BigDecimal amount, LocalDate date, List<Tag> tags) {
        this();
        withId(id);
        withAmount(amount);
        withDate(date);
        withTags(tags);
    }

    MoneyEntryBuilder() {
        this.id = -1;
        this.amount = null;
        this.date = null;
        this.tags = new ArrayList<>();
    }

    public MoneyEntryBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return id;
    }

    public MoneyEntryBuilder withAmount(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount, "Cannot have a null amount.");
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MoneyEntryBuilder withDate(LocalDate date) {
        this.date = Objects.requireNonNull(date, "Cannot have a null date.");
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public MoneyEntryBuilder withTag(Tag tag) {
        this.tags.add(Objects.requireNonNull(tag, "Cannot have a null tag"));
        return this;
    }

    public MoneyEntryBuilder withTags(List<Tag> tags) {
        if (null != tags) {
            for (Tag tag : tags) {
                withTag(tag);
            }
        }
        return this;
    }

    public MoneyEntryBuilder removeTag(Tag tag) {
        if (null != tag) {
            this.tags.remove(tag);
        }
        return this;
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public MoneyEntry build() {
        Objects.requireNonNull(amount, "Cannot build because amount has not been set.");
        Objects.requireNonNull(date, "Cannot build because date has not been set.");
        return new MoneyEntry(this);
    }

}
