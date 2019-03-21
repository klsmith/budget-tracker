package io.github.klsmith.budgettracker.money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.github.klsmith.budgettracker.tag.Tag;

/**
 * Represents a loss of money on a specific date, associated to some arbitrary
 * amount of tags.
 */
public class Expense {

    private final long id;
    private final BigDecimal amount;
    private final LocalDate date;
    private final List<Tag> tags;

    Expense(ExpenseBuilder builder) {
        id = builder.getId();
        amount = Objects.requireNonNull(builder.getAmount());
        date = Objects.requireNonNull(builder.getDate());
        tags = Collections.unmodifiableList(new ArrayList<>(builder.getTags()));
    }

    public static ExpenseBuilder builder() {
        return new ExpenseBuilder();
    }

    public ExpenseBuilder asBuilder() {
        return new ExpenseBuilder(this);
    }

    /**
     * @return a unique {@link Expense} identifier.
     */
    public long getId() {
        return id;
    }

    /**
     * @return the amount of money lost.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @return the date the money was exchanged.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return a list of tags, may be empty.
     */
    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Long.valueOf(id), amount, date, tags);
    }

    /**
     * @return true if the object is a {@link Expense} and all fields are equal;
     *         otherwise, false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Expense) {
            final Expense other = (Expense) obj;
            return id == other.id
                    && Objects.equals(amount, other.amount)
                    && date.isEqual(other.date)
                    && Objects.equals(tags, other.tags);
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{"
                + "id=" + id + ", "
                + "amount=" + amount + ", "
                + "date=" + date + ", "
                + "tags=" + tags
                + "}";
    }

}
