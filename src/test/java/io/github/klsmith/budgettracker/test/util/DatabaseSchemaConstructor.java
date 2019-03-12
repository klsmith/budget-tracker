package io.github.klsmith.budgettracker.test.util;

import java.util.HashMap;
import java.util.Map;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import io.github.klsmith.budgettracker.sql2o.Sql2oDao;

public class DatabaseSchemaConstructor extends Sql2oDao {

    private static final String URL_KEY = "url";
    private static final String USER_KEY = "user";
    private static final String PASS_KEY = "pass";

    public DatabaseSchemaConstructor(String url, String user, String pass) {
        this(new Sql2o(url, user, pass));
    }

    public DatabaseSchemaConstructor(Sql2o sql2o) {
        super(sql2o);
    }

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

    public void setupMoneyEntryTable(Connection connection) {
        final Query query = connection.createQuery(
                "CREATE TABLE MoneyEntry ("
                        + "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "date DATE NOT NULL,"
                        + "amount DECIMAL(13, 4) NOT NULL,"
                        + "PRIMARY KEY(id));");
        query.executeUpdate();
    }

    public void setupTagTable(Connection connection) {
        final Query query = connection.createQuery(
                "CREATE TABLE Tag ("
                        + "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "name VARCHAR(32) NOT NULL,"
                        + "PRIMARY KEY(id));");
        query.executeUpdate();
    }

    public void setupTagMoneyEntryMapTable(Connection connection) {
        final Query query = connection.createQuery(
                "CREATE TABLE TagMoneyEntry ("
                        + "tagId BIGINT UNSIGNED NOT NULL,"
                        + "moneyEntryId BIGINT UNSIGNED NOT NULL,"
                        + "PRIMARY KEY(tagId, moneyEntryId));");
        query.executeUpdate();
    }

}
