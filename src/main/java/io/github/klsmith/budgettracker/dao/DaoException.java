package io.github.klsmith.budgettracker.dao;

/**
 * Represents exceptions that occur within the DAO layer of the application.
 */
public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

}
