package seedu.address.commons.events.logic;

import seedu.address.commons.events.BaseEvent;

/**
 * Created by Philemon1 on 12/10/2017.
 */
public class GoogleAuthSuccessEvent extends BaseEvent {

    public GoogleAuthSuccessEvent() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
