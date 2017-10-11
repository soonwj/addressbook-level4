package seedu.address.commons.events.logic;

import seedu.address.commons.events.BaseEvent;

/**
 * Created by Philemon1 on 11/10/2017.
 */
public class GoogleAuthRequestEvent extends BaseEvent {

    public GoogleAuthRequestEvent() {}

    @Override
    public String toString() { return this.getClass().getSimpleName(); }
}
