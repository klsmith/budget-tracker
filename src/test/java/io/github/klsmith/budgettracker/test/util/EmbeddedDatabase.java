package io.github.klsmith.budgettracker.test.util;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

public class EmbeddedDatabase {

    private static final int TEST_PORT = 3445;
    private static final String TEST_USER = "root";
    private static final String TEST_PASS = "";
    private static final String JDBC_URL_FORMAT = "jdbc:mariadb://localhost:%s/test";

    private DB database;

    public void setup() throws ManagedProcessException {
        if (null != database) {
            throw new IllegalStateException("Cannot set up the test database, it is still in use.");
        }
        DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
        config.setPort(TEST_PORT);
        database = DB.newEmbeddedDB(config.build());
        database.start();
    }

    public void tearDown() throws ManagedProcessException {
        try {
            database.stop();
        }
        finally {
            database = null;
        }
    }

    public String getUrl() {
        return String.format(JDBC_URL_FORMAT, TEST_PORT);
    }

    public String getUser() {
        return TEST_USER;
    }

    public String getPass() {
        return TEST_PASS;
    }

}
