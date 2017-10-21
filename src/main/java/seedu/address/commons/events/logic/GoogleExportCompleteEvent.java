package seedu.address.commons.events.logic;

import seedu.address.commons.events.BaseEvent;

/**
 * Created by Philemon1 on 22/10/2017.
 */
public class GoogleExportCompleteEvent extends BaseEvent {
    private String exportedContactsViewUrl;

    public GoogleExportCompleteEvent(String inputExportedContactsViewUrl) {
        exportedContactsViewUrl = inputExportedContactsViewUrl;
    }
    public String getExportedContactsViewUrl() {
        return exportedContactsViewUrl;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
