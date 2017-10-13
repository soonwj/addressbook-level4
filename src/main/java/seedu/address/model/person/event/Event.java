package seedu.address.model.person.event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's event in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEvent(String)}
 */
public class Event {

    public static final String MESSAGE_EVENT_CONSTRAINTS =
            "Person event can take in any two strings separated by '@'";
    public static final String MESSAGE_EVENT_DATE_CONSTRAINTS =
            "Person event can only date in (day)/(month) in numbers";
    public static final String EVENT_VALIDATION_REGEX = ".*@.*";

    public final String eventAll;
    public final String eventName;
    public static String eventDateString;
    public final Date eventDate;

    /**
     * Validates given email.
     *
     * @throws IllegalValueException if given email address string is invalid.
     */
    public Event(String event) throws IllegalValueException {
        requireNonNull(event);
        String trimmedEvent = event.trim();
        if (!isValidEvent(trimmedEvent)) {
            throw new IllegalValueException(MESSAGE_EVENT_CONSTRAINTS);
        }
        this.eventAll = trimmedEvent;
        String[] splitEvent = trimmedEvent.split("@");
        this.eventName = splitEvent[0].trim();
        try {
            this.eventDateString = splitEvent[1].trim();
            DateFormat df = new SimpleDateFormat("dd/MM");
            this.eventDate = df.parse(eventDateString);
        }
        catch (ParseException ex) {
            throw new IllegalValueException(MESSAGE_EVENT_DATE_CONSTRAINTS);
        }
    }

    public static String periodTillEvent() {
        LocalDate eventLocalDate;
        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM");
            eventLocalDate = LocalDate.parse(eventDateString, formatter);
        }
        catch (DateTimeParseException ex) {
            System.out.printf(eventDateString + " is not parsable.");
            throw ex;
        }
        ZoneId sgt = ZoneId.of("GMT+8");
        LocalDate currentDate = LocalDate.now(sgt);
        Period period = currentDate.until(eventLocalDate);
        int days, months;
        months = period.getMonths();
        days = period.getDays();
        return "Months: " + months + " Days: " + days;
    }

    /**
     * Returns if a given string is a valid event name.
     */
    public static boolean isValidEvent(String test) {
        return test.matches(EVENT_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Event // instanceof handles nulls
                && this.eventAll.equals(((Event) other).eventAll)); // state check
    }

    @Override
    public int hashCode() { return eventAll.hashCode(); }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return '[' + eventAll + ']';
    }

}
