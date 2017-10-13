package seedu.address.commons.events.logic;

import seedu.address.commons.events.BaseEvent;



/**
 * Created by Philemon1 on 12/10/2017.
 */
public class GoogleApiAuthServiceCredentialsSetupCompleted extends BaseEvent {

    public GoogleApiAuthServiceCredentialsSetupCompleted() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
