package perobobbot.plugin.local.chat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import perobobbot.lang.Looper;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@RequiredArgsConstructor
public class MessageLoop extends Looper {

    private final BlockingDeque<String> messages = new LinkedBlockingDeque<>();

    @Setter
    private LocalExecutor localExecutor;

    public void pushMessage(@NonNull String message) {
        this.messages.push(message);
    }


    @Override
    protected @NonNull IterationCommand performOneIteration() throws Exception {
        final String message = this.messages.take();
        final LocalExecutor localExecutor = this.localExecutor;
        localExecutor.handleMessage(message);
        return IterationCommand.CONTINUE;
    }
}
