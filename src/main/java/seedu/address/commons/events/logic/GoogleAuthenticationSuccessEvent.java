package seedu.address.commons.events.logic;

import seedu.address.commons.events.BaseEvent;

/**
 * Created by Philemon1 on 21/10/2017.
 */
public class GoogleAuthenticationSuccessEvent extends BaseEvent {
    private String commandType;

    public GoogleAuthenticationSuccessEvent(String inputCommandType){
        commandType = inputCommandType;
    }

    public String getCommandType() {
        return commandType;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
