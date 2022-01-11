package perobobbot.plugin.local.chat;

import lombok.NonNull;
import lombok.Value;

@Value
public class LocalMessage {

    @NonNull String botName;
    @NonNull String message;
}
