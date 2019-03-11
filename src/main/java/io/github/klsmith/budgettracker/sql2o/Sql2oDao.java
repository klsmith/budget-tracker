package io.github.klsmith.budgettracker.sql2o;

import java.util.function.Function;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public abstract class Sql2oDao {

    private final Sql2o sql2o;

    protected Sql2oDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public Sql2o getSql2o() {
        return sql2o;
    }

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

    public long getLastInsertId() {
        return transaction(this::getLastInsertId);
    }

    public long getLastInsertId(Connection connection) {
        return connection.createQuery("SELECT LAST_INSERT_ID();")
                .executeAndFetchFirst(Long.class)
                .longValue();
    }

}
