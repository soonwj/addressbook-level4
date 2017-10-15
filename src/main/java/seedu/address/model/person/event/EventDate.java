package seedu.address.model.person.event;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Event's header in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEventDate(String)}
 */
public class EventDate{

    public static final String MESSAGE_EVENT_DATE_CONSTRAINTS =
            "Person addresses can take any values, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String EVENT_DATE_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Validates given eventDate.
     *
     * @throws IllegalValueException if given eventDate string is invalid.
     */
    public EventDate(String eventDate) throws IllegalValueException {
        requireNonNull(eventDate);
        if (!isValidEventDate(eventDate)) {
            throw new IllegalValueException(MESSAGE_EVENT_DATE_CONSTRAINTS);
        }
        this.value = eventDate;
    }

    /**
     * Returns true if a given string is a valid eventDate.
     */
    public static boolean isValidEventDate(String test) {
        return test.matches(EVENT_DATE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventDate // instanceof handles nulls
                && this.value.equals(((EventDate) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
