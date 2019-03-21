package io.github.klsmith.budgettracker.sql2o;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.sql2o.Sql2o;

import ch.vorburger.exec.ManagedProcessException;
import io.github.klsmith.budgettracker.test.util.DatabaseSchemaConstructor;
import io.github.klsmith.budgettracker.test.util.EmbeddedDatabase;

public abstract class Sql2oDaoIntegration {

    private final EmbeddedDatabase database = new EmbeddedDatabase();
    private final Sql2o sql2o = createSql2o();
    private final DatabaseSchemaConstructor constructor = new DatabaseSchemaConstructor(sql2o);

    private Sql2o createSql2o() {
        return new Sql2o(
                database.getUrl(),
                database.getUser(),
                database.getPass());
    }

    protected final Sql2o getSql2o() {
        return sql2o;
    }

    @BeforeEach
    protected void setupDatabase() throws ManagedProcessException {
        database.setup();
        constructor.transaction(connection -> {
            constructor.setupMoneyEntryTable(connection);
            constructor.setupTagTable(connection);
            constructor.setupTagMoneyEntryMapTable(connection);
            return null;
        });
    }

    @AfterEach
    protected void tearDownDatabase() throws ManagedProcessException {
        database.tearDown();
    }

}
