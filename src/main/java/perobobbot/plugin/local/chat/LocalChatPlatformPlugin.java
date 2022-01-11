package perobobbot.plugin.local.chat;

import jplugman.annotation.Extension;
import jplugman.api.Disposable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import perobobbot.data.service.BotService;
import perobobbot.lang.*;
import perobobbot.plugin.ChatPlatformPluginData;
import perobobbot.plugin.PerobobbotPlugin;


@Extension(point = PerobobbotPlugin.class,version = "1.0.0")
public class LocalChatPlatformPlugin implements PerobobbotPlugin, Disposable {

    @Getter
    private final @NonNull ChatPlatformPluginData data;

    public LocalChatPlatformPlugin(@NonNull ApplicationCloser applicationCloser,
                                   @NonNull BotService botService,
                                   @NonNull StandardInputProvider standardInputProvider,
                                   @NonNull Instants instants,
                                   @NonNull UserAuthenticator userAuthenticator) {
        final var chatPlatform = new LocalChatPlatform(applicationCloser,botService,standardInputProvider,instants,userAuthenticator);
        this.data = new ChatPlatformPluginData(chatPlatform);
    }

    @Override
    public @NonNull String getName() {
        return Platform.LOCAL.name();
    }

    @Override
    @Synchronized
    public void dispose() {
        data.chatPlatform().dispose();
    }


}
