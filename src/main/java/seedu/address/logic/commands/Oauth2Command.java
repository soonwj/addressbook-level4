package seedu.address.logic.commands;

import java.io.IOException;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.GoogleAuthenticationSuccessEvent;
import seedu.address.commons.events.ui.Oauth2BrowserRequestEvent;


/**This class is the parent class for all commands requiring OAuth2 authentication using the BrowserPanel.
 * This class cannot be instantiated, as it is requires child classes with a defined COMMAND_TYPE.
 * Child classes are also expected to implement the event listener: handleAuthenticationSuccessEvent()
 * Created by Philemon1 on 21/10/2017.
 */
public abstract class Oauth2Command extends Command {
    public static final String INVALID_COMMAND_TYPE_MESSAGE = "The COMMAND_TYPE cannot be null";
    private static final String REDIRECT_URL = "https://cs2103tdummyendpoint.herokuapp.com";
    protected final String commandType;

    protected Oauth2Command(String inputType)  {
        if (inputType == null || inputType.charAt(inputType.length() - 1) == '_') {
            assert true : "Child classes of Oauth2Command must provide a valid command type!";
        }
        commandType = inputType;
    }
    protected Oauth2Command() {
        this(null);
    }

    public static String getRedirectUrl() {
        return REDIRECT_URL;
    }

    /**
     * Main common functionality for all Oauth2Command childs classes. Fires an event intended for the BrowserPanel,
     * triggering it to start the UI authentication process within the BrowserPanel
     * @throws IOException
     */
    protected void triggerBrowserAuth() throws IOException {
        try {
            Oauth2BrowserRequestEvent trigger = new Oauth2BrowserRequestEvent(commandType, getAuthenticationUrl());
            EventsCenter.getInstance().post(trigger);
        } catch (IOException E) {
            throw E;
        }
    }

    /**
     * Event listener to be implemented by child classes
     */
    @Subscribe
    protected abstract void handleAuthenticationSuccessEvent(GoogleAuthenticationSuccessEvent event);

    /**
     * All child classes should provide this URL based on their scope required
     * @return the authentication URL, based on the scope required of the command
     */
    public abstract String getAuthenticationUrl ();



}
