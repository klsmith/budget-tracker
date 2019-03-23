package io.github.klsmith.budgettracker.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import io.github.klsmith.budgettracker.dao.DaoException;
import io.github.klsmith.budgettracker.sql2o.Sql2oDao;

/**
 * Implementation of TagDao using Sql2o.
 */
public class Sql2oTagDao extends Sql2oDao implements TagDao {

	/**
	 * Construct using an Sql2o object.
	 */
	public Sql2oTagDao(Sql2o sql2o) {
		super(sql2o);
	}

	@Override
	public Tag create(Tag tag) {
		return transaction(connection -> create(connection, tag));
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#create(Tag)
	 */
	public Tag create(Connection connection, Tag tag) {
		return create(connection, tag.getName());
	}

	@Override
	public Tag create(String tagName) {
		return transaction(connection -> create(connection, tagName));
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#create(String)
	 */
	public Tag create(Connection connection, String tagName) {
		connection.createQuery("INSERT INTO Tag (name) VALUES (:nameParam);").addParameter("nameParam", tagName)
				.executeUpdate();
		final long id = getLastInsertId(connection);
		return read(connection, id)
				.orElseThrow(() -> new DaoException("Could not read row immediately after creation."));
	}

	@Override
	public Tag mapExpense(long expenseId, String tagName) {
		return transaction(connection -> mapExpense(connection, expenseId, tagName));
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#mapExpense(long, String)
	 */
	public Tag mapExpense(Connection connection, long expenseId, String tagName) {
		final Tag tag = read(connection, tagName).orElseGet(() -> create(connection, tagName));
		connection
				.createQuery(
						"INSERT IGNORE INTO TagExpense " + "(tagId, expenseId) VALUES (:tagIdParam, :expenseIdParam);")
				.addParameter("tagIdParam", tag.getId()).addParameter("expenseIdParam", expenseId).executeUpdate();
		return tag;
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#map(long, String)
	 */
	public Tag mapIncome(Connection connection, long incomeId, String tagName) {
		final Tag tag = read(connection, tagName).orElseGet(() -> create(connection, tagName));
		connection
				.createQuery(
						"INSERT IGNORE INTO TagIncome " + "(tagId, incomeId) VALUES (:tagIdParam, :incomeIdParam);")
				.addParameter("tagIdParam", tag.getId()).addParameter("incomeIdParam", incomeId).executeUpdate();
		return tag;
	}

	@Override
	public List<Tag> mapExpense(long expenseId, List<Tag> tags) {
		return transaction(connection -> mapExpense(connection, expenseId, tags));
	}

	public List<Tag> mapExpense(Connection connection, long expenseId, List<Tag> tags) {
		final List<Tag> results = new ArrayList<>();
		for (Tag tag : tags) {
			final Tag newTag = mapExpense(connection, expenseId, tag.getName());
			results.add(newTag);
		}
		return results;
	}

	@Override
	public Tag mapBudget(long budgetId, String tagName) {
		return transaction(connection -> mapBudget(connection, budgetId, tagName));
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#mapBudget(long, String)
	 */
	public Tag mapBudget(Connection connection, long expenseId, String tagName) {
		final Tag tag = read(connection, tagName).orElseGet(() -> create(connection, tagName));
		connection
				.createQuery(
						"INSERT IGNORE INTO TagBudget " + "(tagId, budgetId) VALUES (:tagIdParam, :budgetIdParam);")
				.addParameter("tagIdParam", tag.getId()).addParameter("budgetIdParam", expenseId).executeUpdate();
		return tag;
	}

	@Override
	public List<Tag> mapBudget(long budgetId, List<Tag> tags) {
		return transaction(connection -> mapBudget(connection, budgetId, tags));
	}

	public List<Tag> mapBudget(Connection connection, long budgetId, List<Tag> tags) {
		final List<Tag> results = new ArrayList<>();
		for (Tag tag : tags) {
			final Tag newTag = mapBudget(connection, budgetId, tag.getName());
			results.add(newTag);
		}
		return results;
	}

	@Override
	public Optional<Tag> read(String tagName) {
		return transaction(connection -> read(connection, tagName));
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#read(String)
	 */
	public Optional<Tag> read(Connection connection, String tagName) {
		return Optional
				.ofNullable(connection.createQuery("SELECT * FROM Tag WHERE name = :nameParam;")
						.addParameter("nameParam", tagName).executeAndFetchFirst(TagBuilder.class))
				.map(TagBuilder::build);
	}

	@Override
	public Optional<Tag> read(long id) {
		return transaction(connection -> read(connection, id));
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#read(long)
	 */
	public Optional<Tag> read(Connection connection, long id) {
		return Optional.ofNullable(connection.createQuery("SELECT * FROM Tag WHERE id = :idParam;")
				.addParameter("idParam", id).executeAndFetchFirst(TagBuilder.class)).map(TagBuilder::build);
	}

	@Override
	public List<Tag> readForExpense(long expenseId) {
		return transaction(connection -> readForExpense(connection, expenseId));
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#readForExpense(long)
	 */
	public List<Tag> readForExpense(Connection connection, long expenseId) {
		return connection
				.createQuery("SELECT Tag.id, Tag.name FROM Tag\n" + "JOIN TagExpense ON TagExpense.tagId = Tag.id\n"
						+ "WHERE TagExpense.expenseId = :expenseIdParam;")
				.addParameter("expenseIdParam", expenseId).executeAndFetch(TagBuilder.class).stream()
				.map(TagBuilder::build).collect(Collectors.toList());
	}

	@Override
	public List<Tag> readForIncome(long incomeId) {
		return transaction(connection -> readForIncome(connection, incomeId));
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#readForExpense(long)
	 */
	public List<Tag> readForIncome(Connection connection, long incomeId) {
		return connection
				.createQuery("SELECT Tag.id, Tag.name FROM Tag\n" + "JOIN TagIncome ON TagIncome.tagId = Tag.id\n"
						+ "WHERE TagIncome.incomeId = :incomeIdParam;")
				.addParameter("incomeIdParam", incomeId).executeAndFetch(TagBuilder.class).stream()
				.map(TagBuilder::build).collect(Collectors.toList());
	}

	@Override
	public List<Tag> readForBudget(long budgetId) {
		return transaction(connection -> readForBudget(connection, budgetId));
	}

	/**
	 * Uses an existing connection instead of creating a new one.
	 * 
	 * @see Sql2oTagDao#readForBudget(long)
	 */
	public List<Tag> readForBudget(Connection connection, long budgetId) {
		return connection
				.createQuery("SELECT Tag.id, Tag.name FROM Tag\n" + "JOIN TagBudget ON TagBudget.tagId = Tag.id\n"
						+ "WHERE TagBudget.budgetId = :budgetIdParam;")
				.addParameter("budgetIdParam", budgetId).executeAndFetch(TagBuilder.class).stream()
				.map(TagBuilder::build).collect(Collectors.toList());
	}

	@Override
	public void delete(long id) {
		transaction(connection -> {
			delete(connection, id);
			return null;
		});
	}

	public void delete(Connection connection, long id) {
		connection.createQuery("DELETE FROM Tag WHERE id = :idParam;").addParameter("idParam", id).executeUpdate();
	}

}
