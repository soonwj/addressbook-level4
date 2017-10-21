package seedu.address.commons.events.logic;

import seedu.address.commons.events.BaseEvent;

/**
 * Created by Philemon1 on 21/10/2017.
 */
public class GoogleAuthenticationSuccessEvent extends BaseEvent {
    private String commandType;
    private String authCode;

    public GoogleAuthenticationSuccessEvent(String inputCommandType, String inputAuthCode){
        commandType = inputCommandType;
        authCode = inputAuthCode;
    }

    public String getCommandType() {
        return commandType;
    }
    public String getAuthCode () {
        return authCode;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
