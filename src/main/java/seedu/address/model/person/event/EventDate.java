package seedu.address.model.person.event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Event's header in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEventDate(String)}
 */
public class EventDate{

    public static final String MESSAGE_EVENT_DATE_CONSTRAINTS =
            "Event must have a valid date input\n" +
                    "Format: day/month/year";

    public final String value;
    public final LocalDate eventLocalDate;

    /**
     * Validates given eventDate.
     *
     * @throws IllegalValueException if given eventDate string is invalid.
     */
    public EventDate(String eventDate) throws IllegalValueException {
        requireNonNull(eventDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            eventLocalDate = LocalDate.parse(eventDate, formatter);
        }
        catch (DateTimeParseException ex) {
            throw new IllegalValueException(MESSAGE_EVENT_DATE_CONSTRAINTS, ex);
        }
        this.value = eventDate;
    }

    /**
     * Returns true if a given string is a valid eventDate.
     */

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