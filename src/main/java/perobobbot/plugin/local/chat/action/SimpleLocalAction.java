package perobobbot.plugin.local.chat.action;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.plugin.local.chat.LocalAction;

@RequiredArgsConstructor
public class SimpleLocalAction implements LocalAction {

    @Getter
    private final @NonNull String name;
    @Getter
    private final @NonNull String description;
    private final @NonNull Runnable execution;

    @Override
    public void execute(@NonNull String[] parameters) {
        execution.run();
    }
}
