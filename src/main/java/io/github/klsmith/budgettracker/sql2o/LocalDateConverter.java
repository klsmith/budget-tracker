package io.github.klsmith.budgettracker.sql2o;

import java.sql.Date;
import java.time.LocalDate;

import org.sql2o.converters.Converter;

/**
 * A converter class for Sql2o that handles {@link LocalDate} objects.
 */
public class LocalDateConverter implements Converter<LocalDate> {

    @Override
    public LocalDate convert(Object obj) {
        if (obj instanceof Date) {
            final Date date = (Date) obj;
            return date.toLocalDate();
        }
        return null;
    }

    @Override
    public Object toDatabaseParam(LocalDate localDate) {
        if (null != localDate) {
            return Date.valueOf(localDate);
        }
        return null;
    }

}
