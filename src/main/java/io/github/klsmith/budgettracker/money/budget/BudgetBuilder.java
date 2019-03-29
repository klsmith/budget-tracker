package io.github.klsmith.budgettracker.money.budget;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.github.klsmith.budgettracker.tag.Tag;

public class BudgetBuilder {

    private long id;
    private BigDecimal amount;
    private LocalDate date;
    private final List<Tag> tags;

    BudgetBuilder(Budget budget) {
        this(budget.getId(),
                budget.getAmount(),
                budget.getDate().orElse(null),
                budget.getTags());
    }

    BudgetBuilder(long id, BigDecimal amount, LocalDate date, List<Tag> tags) {
        this();
        withId(id);
        withAmount(amount);
        withDate(date);
        withTags(tags);
    }

    BudgetBuilder() {
        this.id = -1;
        this.amount = null;
        this.date = null;
        this.tags = new ArrayList<>();
    }

    public BudgetBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return id;
    }

    public BudgetBuilder withAmount(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount, "Cannot have a null amount.");
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BudgetBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public BudgetBuilder withTag(String tagName) {
        return withTag(Tag.builder()
                .withName(tagName)
                .build());
    }

    public BudgetBuilder withTag(Tag tag) {
        this.tags.add(Objects.requireNonNull(tag, "Cannot have a null tag"));
        return this;
    }

    public BudgetBuilder withTags(List<Tag> tags) {
        if (null != tags) {
            for (Tag tag : tags) {
                withTag(tag);
            }
        }
        return this;
    }

    public BudgetBuilder removeTag(Tag tag) {
        if (null != tag) {
            this.tags.remove(tag);
        }
        return this;
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public Budget build() {
        Objects.requireNonNull(amount, "Cannot build because amount has not been set.");
        return new Budget(this);
    }

}
