package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;

/**This class is the parent class for all commands requiring OAuth2 authentication using the BrowserPanel
 * Created by Philemon1 on 21/10/2017.
 */
public abstract class Oauth2Command extends Command {
    //Subclass have to define COMMAND_TYPE
    protected String COMMAND_TYPE;
    private final String REDIRECT_URL = "https://cs2103tdummyendpoint.herokuapp.com";

    public String getRedirectUrl() {
        return REDIRECT_URL;
    }

    /**
     * Child classes are expected to define their own COMMAND_TYPE, as the browser panel
     * will trigger events contextually based on it
     */
    protected abstract void setCommandType();

    protected void triggerBrowserAuth() {
//        EventsCenter.getInstance().post();
    }

}
