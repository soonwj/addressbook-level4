package seedu.address.commons.events.logic;

import seedu.address.commons.auth.GoogleApiAuth;
import seedu.address.commons.events.BaseEvent;
/**
 * Created by Philemon1 on 11/10/2017.
 */
public class GoogleAuthRequestEvent extends BaseEvent {
    private GoogleApiAuth authServiceRef;

    public GoogleAuthRequestEvent(GoogleApiAuth inputAuthService) {
        this.authServiceRef = inputAuthService;
    }

    public GoogleApiAuth getAuthServiceRef() {
        return authServiceRef;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
