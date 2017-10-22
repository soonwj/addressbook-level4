package seedu.address.commons.events.logic;

import seedu.address.commons.events.BaseEvent;

/**
 * Created by Philemon1 on 22/10/2017.
 */
public class GoogleCommandCompleteEvent extends BaseEvent {
    private String redirectUrl;

    public GoogleCommandCompleteEvent(String inputUrl) {
        redirectUrl = inputUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
