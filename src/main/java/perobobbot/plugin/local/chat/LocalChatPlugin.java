package perobobbot.plugin.local.chat;

import com.google.common.collect.ImmutableSet;
import jplugman.api.Plugin;
import jplugman.api.Requirement;
import jplugman.api.ServiceProvider;
import lombok.NonNull;
import perobobbot.data.service.BotService;
import perobobbot.lang.ApplicationCloser;
import perobobbot.lang.Instants;
import perobobbot.lang.StandardInputProvider;
import perobobbot.lang.UserAuthenticator;

public class LocalChatPlugin implements Plugin {

    @Override
    public @NonNull Class<?> getServiceClass() {
        return LocalChatPlatformPlugin.class;
    }

    @Override
    public @NonNull Object loadService(@NonNull ModuleLayer pluginLayer, @NonNull ServiceProvider serviceProvider) {
        return new LocalChatPlatformPlugin(
                serviceProvider.getSingleService(ApplicationCloser.class),
                serviceProvider.getSingleService(BotService.class),
                serviceProvider.getSingleService(StandardInputProvider.class),
                serviceProvider.getSingleService(Instants.class),
                serviceProvider.getSingleService(UserAuthenticator.class)
        );
    }

    @Override
    public @NonNull ImmutableSet<Requirement<?>> getRequirements() {
        return ImmutableSet.of(
                Requirements.APPLICATION_CLOSER,
                Requirements.BOT_SERVICE,
                Requirements.STANDARD_INPUT_PROVIDER,
                Requirements.INSTANTS,
                Requirements.USER_AUTHENTICATOR
        );
    }

}
