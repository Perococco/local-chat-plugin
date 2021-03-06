package perobobbot.plugin.local.chat.action;

import lombok.Getter;
import lombok.NonNull;
import perobobbot.plugin.local.chat.LocalAction;

public abstract class LocalActionBase implements LocalAction {

    @Getter
    private final @NonNull String name;
    @Getter
    private final @NonNull String description;

    public LocalActionBase(@NonNull String name, @NonNull String description) {
        this.name = name;
        this.description = description;
    }

}
