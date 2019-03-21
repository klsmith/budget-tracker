package io.github.klsmith.budgettracker.sql2o;

import java.time.LocalDate;
import java.util.Map;

import org.sql2o.converters.Converter;
import org.sql2o.converters.ConvertersProvider;

/**
 * Provides custom converter implementation so that Sql2o can access them.
 */
public class Sql2oConvertersProvider implements ConvertersProvider {

    @Override
    public void fill(Map<Class<?>, Converter<?>> mapToFill) {
        mapToFill.put(LocalDate.class, new LocalDateConverter());
    }

}
