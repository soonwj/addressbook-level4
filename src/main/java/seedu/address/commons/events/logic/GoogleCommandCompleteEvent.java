package seedu.address.commons.events.logic;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;

//@@author philemontan
/**
 * An event fired indicating a GoogleCommand instance has successfully completed. Attributes redirectUrl and
 * commandType are provided by the firer, for identifying the the GoogleCommand subclass of the instance.
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
