package io.github.klsmith.budgettracker.dao;

import java.util.Optional;

public interface Dao<P, T> {

    /**
     * Create an new row in the table, with the given data.<br/>
     * </br>
     * <strong>Note:</strong> the primary key field may be ignored, depending on the
     * table/implementation.
     * 
     * @return the newly created row from the database.
     */
    T create(T data);

    /**
     * Read the row in the table that matches the given primary key.
     * 
     * @return an optional containing the row data, or an empty optional if there
     *         wasn't a row with that primary key.
     */
    Optional<T> read(P key);

}
