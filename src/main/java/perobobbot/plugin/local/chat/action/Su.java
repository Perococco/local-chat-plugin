package perobobbot.plugin.local.chat.action;

import lombok.NonNull;
import perobobbot.lang.UserAuthenticator;

public class Su extends LocalActionBase {

    private final @NonNull UserAuthenticator userAuthenticator;

    public Su(@NonNull UserAuthenticator userAuthenticator) {
        super("su", "authenticate as user");
        this.userAuthenticator = userAuthenticator;
    }

    @Override
    public void execute(@NonNull String[] parameters) {
        if (parameters.length != 0 && parameters.length != 2) {
            System.out.println("Usage : su or su <login> <password>");
        }
        if (parameters.length == 0) {
            this.userAuthenticator.clearAuthentication();
        }
        if (parameters.length == 2) {
            if (this.userAuthenticator.authenticate(parameters[0],parameters[1])) {
                System.out.println("Authenticated as "+parameters[0]);
            } else {
                System.out.println("Authentication failed");
            }
        }
    }
}
