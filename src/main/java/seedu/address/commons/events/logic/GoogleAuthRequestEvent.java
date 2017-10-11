package seedu.address.commons.events.logic;

import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.auth.GoogleApiAuth;
/**
 * Created by Philemon1 on 11/10/2017.
 */
public class GoogleAuthRequestEvent extends BaseEvent {

    public GoogleApiAuth authServiceRef;

    public GoogleAuthRequestEvent(GoogleApiAuth inputAuthService) {
        this.authServiceRef = inputAuthService;
    }

    @Override
    public String toString() { return this.getClass().getSimpleName(); }
}
