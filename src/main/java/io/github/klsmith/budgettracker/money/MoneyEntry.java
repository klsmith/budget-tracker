package io.github.klsmith.budgettracker.money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.github.klsmith.budgettracker.tag.Tag;

/**
 * Represents some exchange of money on a specific date, associated to some
 * arbitrary amount of tags.
 */
public class MoneyEntry {

    private final long id;
    private final BigDecimal amount;
    private final LocalDate date;
    private final List<Tag> tags;

    public MoneyEntry(MoneyEntry entry, Collection<Tag> newTags) {
        this(entry.getId(), entry.getAmount(), entry.getDate(), newTags);
    }

    public MoneyEntry(long id, BigDecimal amount, LocalDate date, Collection<Tag> tags) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.tags = Collections.unmodifiableList(new ArrayList<>(tags));
    }

    public MoneyEntry(long id, BigDecimal amount, LocalDate date, Tag... tags) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        final List<Tag> temp = new ArrayList<>(tags.length);
        for (Tag tag : tags) {
            temp.add(tag);
        }
        this.tags = Collections.unmodifiableList(temp);
    }

    /**
     * @return a unique {@link MoneyEntry} identifier.
     */
    public long getId() {
        return id;
    }

    /**
     * @return the amount of money exchanged.
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
        return Objects.hash(id, amount, date, tags);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoneyEntry) {
            final MoneyEntry other = (MoneyEntry) obj;
            return Objects.equals(id, other.id)
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
