package perobobbot.plugin.local.chat;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.Synchronized;
import perobobbot.chat.core.ChatConnection;
import perobobbot.chat.core.ChatPlatform;
import perobobbot.data.service.BotService;
import perobobbot.lang.*;
import perobobbot.plugin.local.chat.action.*;

import java.io.PrintStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class LocalChatPlatform implements ChatPlatform {

    private final @NonNull LocalSender localSender;

    private final SubscriptionHolder subscriptionHolder = new SubscriptionHolder();
    private volatile @NonNull ImmutableMap<ChatConnectionInfo, LocalConnection> localConnections = ImmutableMap.of();
    private final @NonNull Listeners<MessageListener> listeners = new Listeners<>();
    private final @NonNull Instants instants;

    private final PrintStream output = System.out;

    private final MessageLoop messageLoop = new MessageLoop();

    public LocalChatPlatform(@NonNull ApplicationCloser applicationCloser,
                             @NonNull BotService botService,
                             @NonNull StandardInputProvider standardInputProvider,
                             @NonNull Instants instants,
                             @NonNull UserAuthenticator userAuthenticator) {
        final GuiContext guiContext = new GuiContext(messageLoop::pushMessage);
        final ApplicationCloser closer = () -> {subscriptionHolder.unsubscribe();applicationCloser.execute();};

        final var localExecutor = new SimpleLocalExecutor(output, this::onLocalMessages,
                                                     new CreateBot(botService),
                                                     new ListBots(botService),
                                                     new StopServer(closer),
                                                     new ShowGui(guiContext),
                                                     new HideGui(guiContext),
                                                     new SqlLog(),
                new Su(userAuthenticator)
        );

        this.messageLoop.setLocalExecutor(localExecutor);

        this.instants = instants;
        this.subscriptionHolder.replaceWith(() -> standardInputProvider.addListener(localExecutor::handleMessage));
        this.localSender = new ToStandardOutputSender(instants);
        this.messageLoop.start();
    }


    @Override
    public @NonNull Optional<CompletionStage<ChatConnection>> findConnection(@NonNull ChatConnectionInfo chatConnectionInfo) {
        return Optional.ofNullable(localConnections.get(chatConnectionInfo))
                       .map(CompletableFuture::completedFuture);
    }

    private void onLocalMessages(@NonNull LocalMessage localMessage) {
        findTargetedBot(localMessage)
                .ifPresent(b ->
                           {
                               final MessageContext ctx = MessageContext.builder()
                                                                        .chatConnectionInfo(b)
                                                                        .messageFromMe(false)
                                                                        .content(localMessage.getMessage())
                                                                        .rawPayload(localMessage.getMessage())
                                                                        .messageOwner(LocalChat.LOCAL_USER)
                                                                        .receptionTime(instants.now())
                                                                        .channelInfo(LocalChat.CONSOLE_CHANNEL_INFO)
                                                                        .build();
                               listeners.warnListeners(l -> l.onMessage(ctx));
                           }
                );
    }


    private @NonNull Optional<ChatConnectionInfo> findTargetedBot(@NonNull LocalMessage localMessage) {
        final var connections = this.localConnections;
        if (localMessage.getBotName().isBlank()) {
            if (connections.isEmpty()) {
                return Optional.empty();
            } else if (connections.size() == 1) {
                return connections.keySet().stream().findFirst();
            } else {
                output.println("Bot name must be provided when multiple bots are registered: #botname message");
                return Optional.empty();
            }
        } else {
            final var bot = connections.keySet()
                                       .stream()
                                       .filter(b -> b.getBotName().equalsIgnoreCase(localMessage.getBotName()))
                                       .findFirst();
            if (bot.isEmpty()) {
                output.println("Unknown bot '" + localMessage.getBotName() + "'");
            }
            return bot;
        }

    }

    @Override
    public @NonNull Platform getPlatform() {
        return Platform.LOCAL;
    }

    @Override
    public @NonNull CompletionStage<ChatConnection> connect(@NonNull ChatConnectionInfo chatConnectionInfo) {
        return CompletableFuture.completedFuture(getLocalConnection(chatConnectionInfo));
    }

    @Synchronized
    private @NonNull LocalConnection getLocalConnection(@NonNull ChatConnectionInfo connectionInfo) {
        final LocalConnection existingConnection = localConnections.get(connectionInfo);
        if (existingConnection != null) {
            return existingConnection;
        }
        final LocalConnection newConnection = this.createLocalConnectionForBot(connectionInfo);
        this.localConnections = MapTool.add(this.localConnections, connectionInfo, newConnection);
        return newConnection;
    }

    private @NonNull LocalConnection createLocalConnectionForBot(@NonNull ChatConnectionInfo connectionInfo) {
        return new LocalConnection(connectionInfo, localSender);
    }

    @Override
    public @NonNull Subscription addMessageListener(@NonNull MessageListener listener) {
        return listeners.addListener(listener);
    }

    @Override
    @Synchronized
    public void dispose() {
        localConnections.values().forEach(LocalConnection::dispose);
        localConnections = ImmutableMap.of();
        subscriptionHolder.unsubscribe();
        messageLoop.requestStop();
    }


}
