package seedu.address.commons.exceptions;

/**
 * Created by Philemon1 on 13/10/2017.
 */
public class InvalidGooglePersonException extends Exception {
    /**
     * @param message should contain relevant information on the failed constraint(s)
     */
    public InvalidGooglePersonException(String message) {
        super(message);
    }

}
