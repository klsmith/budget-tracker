package io.github.klsmith.budgettracker.sql2o;

import java.util.function.Function;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 * Contains some table-agnostic operations for Dao implementations that use
 * Sql2o.
 */
public abstract class Sql2oDao {

    private final Sql2o sql2o;

    /**
     * Takes an Sql2o object in order to provide it to all sub-classes.
     */
    protected Sql2oDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    /**
     * Provides the Sql2o object.
     */
    public final Sql2o getSql2o() {
        return sql2o;
    }

    /**
     * Generically handles committing / rolling back a transaction depending on
     * whether or not the operations where successful.
     */
    public <R> R transaction(Function<Connection, R> function) {
        final Connection connection = sql2o.beginTransaction();
        try {
            connection.setRollbackOnClose(true);
            final R result = function.apply(connection);
            connection.commit(false);
            return result;
        }
        finally {
            connection.close();
        }
    }

    /**
     * @return the most recent AUTO_INCREMENT value generated.
     */
    public long getLastInsertId() {
        return transaction(c -> Long.valueOf(getLastInsertId(c)))
                .longValue();
    }

    /**
     * Uses an existing connection instead of creating a new one.
     * 
     * @see Sql2oDao#getLastInsertId()
     */
    public long getLastInsertId(Connection connection) {
        return connection.createQuery("SELECT LAST_INSERT_ID();")
                .executeAndFetchFirst(Long.class)
                .longValue();
    }

}
