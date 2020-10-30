package liquibase.configuration;

import liquibase.Scope;
import liquibase.logging.LogFactory;

import java.util.logging.Level;

/**
 * Configuration container for global properties.
 */
public class SqlConfiguration extends AbstractConfigurationContainer {

    public static final String SQL_LOG_LEVEL = "logLevel";


    public SqlConfiguration() {
        super("liquibase.sql");

        getContainer().addProperty(SQL_LOG_LEVEL, Level.class)
                .setDescription("Log level to execute sql interactions at")
                .setDefaultValue(Level.FINE)
                .setValueHandler(new ConfigurationValueHandler() {
                    @Override
                    public Object convert(Object value) {
                        if (value instanceof Level) {
                            return value;
                        }

                        try {
                            return Scope.getCurrentScope().getSingleton(LogFactory.class).parseLogLevel(String.valueOf(value));
                        } catch (IllegalArgumentException e) {
                            Scope.getCurrentScope().getLog(getClass()).warning("liquibase.sql.loglevel value of " + value + " is not a logLevel recognized by Liquibase so SQL information sent to FINE level logs by default. For more information check https://docs.liquibase.com");
                            return Level.FINE;
                        }
                    }
                });
    }

    @Override
    public void setValue(String propertyName, Object value) {
        super.setValue(propertyName, value);
    }


    public Level getLogLevel() {
        return getContainer().getValue(SQL_LOG_LEVEL, Level.class);
    }

    public SqlConfiguration setLogLevel(Level logLevel) {
        getContainer().setValue(SQL_LOG_LEVEL, logLevel);
        return this;
    }
}