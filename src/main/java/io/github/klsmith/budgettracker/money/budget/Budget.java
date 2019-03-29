package io.github.klsmith.budgettracker.money.budget;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.github.klsmith.budgettracker.tag.Tag;

/**
 * Represents a maximum amount of money that can be spent on a specific date
 * and/or with certain tags.
 */
public class Budget {

	private final long id;
	private final BigDecimal amount;
	private final LocalDate date;
	private final List<Tag> tags;

	Budget(BudgetBuilder builder) {
		id = builder.getId();
		amount = Objects.requireNonNull(builder.getAmount());
		date = builder.getDate();
		tags = Collections.unmodifiableList(new ArrayList<>(builder.getTags()));
	}

	public static BudgetBuilder builder() {
		return new BudgetBuilder();
	}

	public BudgetBuilder asBuilder() {
		return new BudgetBuilder(this);
	}

	/**
	 * @return a unique {@link Budget} identifier.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the budget amount.
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @return an Optional containing the date the budget applies to, or empty if
	 *         this budget does not apply to a specific date.
	 */
	public Optional<LocalDate> getDate() {
		return Optional.ofNullable(date);
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
	 * @return true if the object is a {@link Budget} and all fields are equal;
	 *         otherwise, false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Budget) {
			final Budget other = (Budget) obj;
			return id == other.id && Objects.equals(amount, other.amount) && localDateEquals(other.date)
					&& Objects.equals(tags, other.tags);
		}
		return false;
	}

	private boolean localDateEquals(LocalDate otherDate) {
		if (null == date && null == otherDate) {
			return true;
		}
		if (null == date || null == otherDate) {
			return false;
		}
		return date.isEqual(otherDate);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + "id=" + id + ", " + "amount=" + amount + ", " + "date=" + date + ", "
				+ "tags=" + tags + "}";
	}

}
