package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Indicates a request to jump to the list of persons
 */
//@@author sidhmads
public class FindLocationRequestEvent extends BaseEvent {

    public final ReadOnlyPerson targetPerson;

    public FindLocationRequestEvent(ReadOnlyPerson targetPerson) {
        this.targetPerson = targetPerson;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
