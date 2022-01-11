package perobobbot.plugin.local.chat;

import lombok.NonNull;

public interface LocalExecutor {

    void handleMessage(@NonNull String line);
}
