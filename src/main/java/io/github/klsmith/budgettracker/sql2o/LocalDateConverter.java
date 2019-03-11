package io.github.klsmith.budgettracker.sql2o;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.sql2o.converters.Converter;
import org.sql2o.converters.ConverterException;

public class LocalDateConverter implements Converter<LocalDate> {

    @Override
    public LocalDate convert(Object obj) throws ConverterException {
        if (obj instanceof Date) {
            final Date date = (Date) obj;
            return date.toLocalDate();
        } else {
            return null;
        }
    }

    @Override
    public Object toDatabaseParam(LocalDate localDate) {
        if (localDate == null) {
            return null;
        } else {
            return Date.valueOf(localDate);
        }
    }

}
