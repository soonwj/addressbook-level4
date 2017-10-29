package seedu.address.commons.events.logic;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;

/**
 * Created by Philemon1 on 22/10/2017.
 */
public class GoogleCommandCompleteEvent extends BaseEvent {
    private String redirectUrl;
    private String commandType;

    public GoogleCommandCompleteEvent(String inputUrl, String inputCommandType) {
        redirectUrl = inputUrl;
        commandType = inputCommandType;
        EventsCenter.getInstance().post(new NewResultAvailableEvent(commandType
                + " has successfully completed", false));
    }
    public String getRedirectUrl() {
        return redirectUrl;
    }
    public String getCommandType() {
        return commandType;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
