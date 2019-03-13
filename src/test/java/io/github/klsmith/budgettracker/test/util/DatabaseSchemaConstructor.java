package io.github.klsmith.budgettracker.test.util;

import java.util.HashMap;
import java.util.Map;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import io.github.klsmith.budgettracker.sql2o.Sql2oDao;

/**
 * <p>
 * A class used for constructing the schema for the project.
 * </p>
 * <p>
 * Should be always be updated to reflect the preferred schema for the database.
 * </p>
 */
public class DatabaseSchemaConstructor extends Sql2oDao {

    private static final String URL_KEY = "url";
    private static final String USER_KEY = "user";
    private static final String PASS_KEY = "pass";

    /**
     * Construct a new DatabaseSchemaConstructor with the given jdbc url, username,
     * and password.
     */
    public DatabaseSchemaConstructor(String url, String user, String pass) {
        this(new Sql2o(url, user, pass));
    }

    /**
     * Construct a new DatabaseSchemaConstructor with the given Sql2o object.
     */
    public DatabaseSchemaConstructor(Sql2o sql2o) {
        super(sql2o);
    }

    /**
     * <p>
     * This entry point can be used to quickly set up a new database that is NOT
     * embedded in a test run.
     * </p>
     * <h3>argument syntax:</h3>
     * 
     * <pre>
     * url=jdbc:mariadb://localhost:3445/test user=root pass=test123
     * </pre>
     */
    public static void main(String[] args) {
        final Map<String, String> map = buildArgumentMap(args);
        final String url = map.get(URL_KEY);
        final String user = map.get(USER_KEY);
        final String pass = map.get(PASS_KEY);
        final DatabaseSchemaConstructor constructor = new DatabaseSchemaConstructor(url, user, pass);
        constructor.transaction(connection -> {
            constructor.setupMoneyEntryTable(connection);
            constructor.setupTagTable(connection);
            constructor.setupTagMoneyEntryMapTable(connection);
            return null;
        });
    }

    private static Map<String, String> buildArgumentMap(String[] args) {
        final Map<String, String> map = new HashMap<>();
        for (String arg : args) {
            final String[] split = arg.split("=", 2);
            map.put(split[0], split[1]);
        }
        return map;
    }

    /**
     * Create the MoneyEntry table schema.
     */
    public void setupMoneyEntryTable(Connection connection) {
        final Query query = connection.createQuery(
                "CREATE TABLE MoneyEntry ("
                        + "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "date DATE NOT NULL,"
                        + "amount DECIMAL(13, 4) NOT NULL,"
                        + "PRIMARY KEY(id));");
        query.executeUpdate();
    }

    /**
     * Create the Tag table schema.
     */
    public void setupTagTable(Connection connection) {
        final Query query = connection.createQuery(
                "CREATE TABLE Tag ("
                        + "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "name VARCHAR(32) NOT NULL,"
                        + "PRIMARY KEY(id));");
        query.executeUpdate();
    }

    /**
     * Create the TagMoneyEntry table schema.
     */
    public void setupTagMoneyEntryMapTable(Connection connection) {
        final Query query = connection.createQuery(
                "CREATE TABLE TagMoneyEntry ("
                        + "tagId BIGINT UNSIGNED NOT NULL,"
                        + "moneyEntryId BIGINT UNSIGNED NOT NULL,"
                        + "PRIMARY KEY(tagId, moneyEntryId));");
        query.executeUpdate();
    }

}
