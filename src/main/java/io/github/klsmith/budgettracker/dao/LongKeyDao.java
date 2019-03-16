package io.github.klsmith.budgettracker.dao;

import java.util.Optional;

public interface LongKeyDao<T> extends Dao<Long, T> {

    @Override
    default Optional<T> read(Long key) {
        return read(key.longValue());
    }

    /**
     * Read the row in the table that matches the given id.
     * 
     * @return an optional containing the row data, or an empty optional if there
     *         wasn't a row with that id.
     */
    Optional<T> read(long key);

}
