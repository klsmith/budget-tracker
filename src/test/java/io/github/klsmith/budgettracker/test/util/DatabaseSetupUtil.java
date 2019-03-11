package io.github.klsmith.budgettracker.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

public class DatabaseSetupUtil {

    private static final String URL_KEY = "url";
    private static final String USER_KEY = "user";
    private static final String PASS_KEY = "pass";

    public static void main(String[] args) {
        final Map<String, String> map = buildArgumentMap(args);
        final String url = map.get(URL_KEY);
        final String user = map.get(USER_KEY);
        final String pass = map.get(PASS_KEY);
        transaction(url, user, pass, connection -> {
            setupMoneyEntryTable(connection);
            setupTagTable(connection);
            setupTagMoneyEntryMapTable(connection);
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

    public static void transaction(String url, String user, String pass, Consumer<Connection> consumer) {
        transaction(new Sql2o(url, user, pass), consumer);
    }

    public static void transaction(Sql2o sql2o, Consumer<Connection> consumer) {
        final Connection connection = sql2o.beginTransaction();
        try {
            connection.setRollbackOnClose(true);
            consumer.accept(connection);
            connection.commit(false);
        }
        finally {
            connection.close();
        }
    }

    public static void setupMoneyEntryTable(String url, String user, String pass) {
        transaction(url, user, pass, DatabaseSetupUtil::setupMoneyEntryTable);
    }

    public static void setupMoneyEntryTable(Connection connection) {
        final Query query = connection.createQuery(
                "CREATE TABLE MoneyEntry ("
                        + "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "date DATE NOT NULL,"
                        + "amount DECIMAL(13, 4) NOT NULL,"
                        + "PRIMARY KEY(id));");
        query.executeUpdate();
    }

    public static void setupTagTable(String url, String user, String pass) {
        transaction(url, user, pass, DatabaseSetupUtil::setupTagTable);
    }

    public static void setupTagTable(Connection connection) {
        final Query query = connection.createQuery(
                "CREATE TABLE Tag ("
                        + "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "name VARCHAR(32) NOT NULL,"
                        + "PRIMARY KEY(id));");
        query.executeUpdate();
    }

    public static void setupTagMoneyEntryMapTable(String url, String user, String pass) {
        transaction(url, user, pass, DatabaseSetupUtil::setupTagMoneyEntryMapTable);
    }

    public static void setupTagMoneyEntryMapTable(Connection connection) {
        final Query query = connection.createQuery(
                "CREATE TABLE TagMoneyEntry ("
                        + "tagId BIGINT UNSIGNED NOT NULL,"
                        + "moneyEntryId BIGINT UNSIGNED NOT NULL,"
                        + "PRIMARY KEY(tagId, moneyEntryId));");
        query.executeUpdate();
    }

}
