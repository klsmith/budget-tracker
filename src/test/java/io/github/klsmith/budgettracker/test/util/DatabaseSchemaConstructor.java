package io.github.klsmith.budgettracker.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import ch.vorburger.exec.ManagedProcessException;
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

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSchemaConstructor.class);

    private static class Config {

        private static final String URL_KEY = "url";
        private static final String USER_KEY = "user";
        private static final String PASS_KEY = "pass";
        private static final String RUN_DB_KEY = "runDb";

        public final String url;
        public final String user;
        public final String pass;
        public final boolean runDb;

        public Config(String[] args) {
            final Map<String, String> map = buildArgumentMap(args);
            url = map.getOrDefault(URL_KEY, "jdbc:mariadb://localhost:3445/test");
            user = map.getOrDefault(USER_KEY, "root");
            pass = map.getOrDefault(PASS_KEY, "");
            runDb = Boolean.parseBoolean(map.get(RUN_DB_KEY));
        }

        private static Map<String, String> buildArgumentMap(String[] args) {
            final Map<String, String> map = new HashMap<>();
            for (String arg : args) {
                final String[] split = arg.split("=", 2);
                map.put(split[0], split[1]);
            }
            return map;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()
                    + "{ url=" + url
                    + ", user=" + user
                    + ", pass=" + pass
                    + ", runDb=" + runDb
                    + "}";
        }

    }

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
     * 
     * @throws ManagedProcessException
     */
    public static void main(String[] args) throws ManagedProcessException {
        final Config config = new Config(args);
        logger.info("config={}", config);
        final EmbeddedDatabase database = (config.runDb ? new EmbeddedDatabase() : null);
        if (null != database) {
            database.setup();
        }
        final DatabaseSchemaConstructor constructor = new DatabaseSchemaConstructor(
                config.url,
                config.user,
                config.pass);
        constructor.setupFullSchema();
        boolean running = true;
        try (final Scanner scanner = new Scanner(System.in)) {
            while (running) {
                final String line = scanner.nextLine();
                if ("exit".equals(line.trim())) {
                    running = false;
                }
            }
        }
        if (null != database) {
            database.tearDown();
        }
    }

    public void setupFullSchema() {
        transaction(connection -> {
            setupExpenseTable(connection);
            setupTagTable(connection);
            setupTagExpenseMapTable(connection);
            return null;
        });
    }

    /**
     * Create the {@link Expense} table schema.
     */
    public void setupExpenseTable(Connection connection) {
        try (final Query query = connection.createQuery(
                "CREATE TABLE Expense ("
                        + "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "date DATE NOT NULL,"
                        + "amount DECIMAL(13, 4) NOT NULL,"
                        + "PRIMARY KEY(id));")) {
            query.executeUpdate();
        }
    }

    /**
     * Create the Tag table schema.
     */
    public void setupTagTable(Connection connection) {
        try (final Query query = connection.createQuery(
                "CREATE TABLE Tag ("
                        + "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "name VARCHAR(32) NOT NULL,"
                        + "PRIMARY KEY(id));")) {
            query.executeUpdate();
        }
    }

    /**
     * Create the TagExpense table schema.
     */
    public void setupTagExpenseMapTable(Connection connection) {
        try (final Query query = connection.createQuery(
                "CREATE TABLE TagExpense ("
                        + "tagId BIGINT UNSIGNED NOT NULL,"
                        + "expenseId BIGINT UNSIGNED NOT NULL,"
                        + "PRIMARY KEY(tagId, expenseId));")) {
            query.executeUpdate();
        }
    }

}
