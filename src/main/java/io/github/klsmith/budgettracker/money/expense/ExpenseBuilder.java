package io.github.klsmith.budgettracker.money.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.github.klsmith.budgettracker.tag.Tag;

public class ExpenseBuilder {

    private long id;
    private BigDecimal amount;
    private LocalDate date;
    private final List<Tag> tags;

    ExpenseBuilder(Expense expense) {
        this(expense.getId(),
                expense.getAmount(),
                expense.getDate(),
                expense.getTags());
    }

    ExpenseBuilder(long id, BigDecimal amount, LocalDate date, List<Tag> tags) {
        this();
        withId(id);
        withAmount(amount);
        withDate(date);
        withTags(tags);
    }

    ExpenseBuilder() {
        this.id = -1;
        this.amount = null;
        this.date = null;
        this.tags = new ArrayList<>();
    }

    public ExpenseBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return id;
    }

    public ExpenseBuilder withAmount(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount, "Cannot have a null amount.");
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ExpenseBuilder withDate(LocalDate date) {
        this.date = Objects.requireNonNull(date, "Cannot have a null date.");
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public ExpenseBuilder withTag(String tagName) {
        return withTag(Tag.builder()
                .withName(tagName)
                .build());
    }

    public ExpenseBuilder withTag(Tag tag) {
        this.tags.add(Objects.requireNonNull(tag, "Cannot have a null tag"));
        return this;
    }

    public ExpenseBuilder withTags(List<Tag> tags) {
        if (null != tags) {
            for (Tag tag : tags) {
                withTag(tag);
            }
        }
        return this;
    }

    public ExpenseBuilder removeTag(Tag tag) {
        if (null != tag) {
            this.tags.remove(tag);
        }
        return this;
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public Expense build() {
        Objects.requireNonNull(amount, "Cannot build because amount has not been set.");
        Objects.requireNonNull(date, "Cannot build because date has not been set.");
        return new Expense(this);
    }

}
