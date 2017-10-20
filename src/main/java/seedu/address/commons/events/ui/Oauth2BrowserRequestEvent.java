package seedu.address.commons.events.ui;

import java.io.IOException;

import seedu.address.commons.events.BaseEvent;

/**This event is used by all Oauth2Command to trigger the browser authentication logic
 * Created by Philemon1 on 21/10/2017.
 */
public class Oauth2BrowserRequestEvent extends BaseEvent {
    public static final String INVALID_INPUT_MESSAGE = "This event must be created with a COMMAND_TYPE";
    private final String commandType;

    public Oauth2BrowserRequestEvent(String inputType) throws IOException {
        if (inputType == null) {
            throw new IOException(INVALID_INPUT_MESSAGE);
        }
        this.commandType = inputType;
    }

    public Oauth2BrowserRequestEvent() throws IOException {
        throw new IOException(INVALID_INPUT_MESSAGE);
    }

    public String getCommandType() {
        return this.commandType;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
