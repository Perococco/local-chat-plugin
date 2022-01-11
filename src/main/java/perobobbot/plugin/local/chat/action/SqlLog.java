package perobobbot.plugin.local.chat.action;

import lombok.NonNull;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import perobobbot.plugin.local.chat.LocalAction;

public class SqlLog implements LocalAction {

    @Override
    public @NonNull String getName() {
        return "show-sql";
    }

    @Override
    public @NonNull String getDescription() {
        return "Toggle SQL logging: show-sql {true|false}";
    }

    @Override
    public void execute(@NonNull String[] parameters) {
        if (parameters.length < 1) {
            System.err.println("Error: "+getDescription());
            return;
        }
        final var val = parameters[0];
        final var on = val.equalsIgnoreCase("true") || val.equalsIgnoreCase("t");
        Configurator.setLevel("org.hibernate.SQL",on? Level.DEBUG:Level.OFF);
    }
}
