package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.InvalidGooglePersonException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;


/**
 * Unit testing for the GooglePersonConverterUtil class
 * Both classes and their composed classes in each package have the same name;
 * Handling: seedu.address.model.person.Person will be imported, while Google classes have to be fully qualified.
 */
public class GooglePersonConverterUtilTest {
    /**
     * Google Person -> DoC Person Conversion Tests (IMPORT)
     */

    //Verifies the conversion result for Google Person with null parameters
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void convertGooglePersonWithNullNameException() throws InvalidGooglePersonException {
        com.google.api.services.people.v1.model.Person testPerson
                = getGooglePerson(null, "12345678", "blk001 Test Ave",
                "test@gmail.com");
        thrown.expect(InvalidGooglePersonException.class);
        GooglePersonConverterUtil.singleGoogleToDocPersonConversion(testPerson);
    }

    @Test
    public void convertGooglePersonWithNullPhoneException() throws InvalidGooglePersonException {
        com.google.api.services.people.v1.model.Person testPerson
                = getGooglePerson("Testee", null, "blk001 Test Ave", "test@gmail.com");
        thrown.expect(InvalidGooglePersonException.class);
        GooglePersonConverterUtil.singleGoogleToDocPersonConversion(testPerson);
    }

    @Test
    public void convertGooglePersonWithNullEmail() throws InvalidGooglePersonException {
        com.google.api.services.people.v1.model.Person testPerson
                = getGooglePerson("Testee", "12345678", "blk001 Test Ave", null);
        seedu.address.model.person.Person convertedPerson = GooglePersonConverterUtil.convertPerson(testPerson);
        assertEquals(convertedPerson.getEmail().toString(), GooglePersonConverterUtil.DEFAULT_EMAIL);
    }

    @Test
    public void convertGooglePersonWithNullAddress() throws InvalidGooglePersonException {
        com.google.api.services.people.v1.model.Person testPerson
                = getGooglePerson("Testee", "12345678", null, "test@gmail.com");
        seedu.address.model.person.Person convertedPerson = GooglePersonConverterUtil.convertPerson(testPerson);
        assertEquals(convertedPerson.getAddress().toString(), GooglePersonConverterUtil.DEFAULT_ADDRESS);
    }

    /**
     * DoC Person -> Google Person Conversion Tests
     */


    /**
     * Creates a GooglePerson from input parameters, to support testing
     * @param name
     * @param number
     * @param address
     * @param email
     * @return the desired Google Person
     */
    public com.google.api.services.people.v1.model.Person getGooglePerson(String name, String number, String address, String email) {
        ArrayList<com.google.api.services.people.v1.model.Name> names = new ArrayList<>();
        ArrayList<com.google.api.services.people.v1.model.PhoneNumber> phones = new ArrayList<>();
        ArrayList<com.google.api.services.people.v1.model.Address> addresses = new ArrayList<>();
        ArrayList<com.google.api.services.people.v1.model.EmailAddress> emails = new ArrayList<>();
        com.google.api.services.people.v1.model.Person personToReturn =
                new com.google.api.services.people.v1.model.Person();

        names.add(new com.google.api.services.people.v1.model.Name().setDisplayName(name));
        phones.add(new com.google.api.services.people.v1.model.PhoneNumber().setValue(number));
        addresses.add(new com.google.api.services.people.v1.model.Address().setFormattedValue(address));
        emails.add(new com.google.api.services.people.v1.model.EmailAddress().setValue(email));

        personToReturn.setNames(names);
        personToReturn.setAddresses(addresses);
        personToReturn.setEmailAddresses(emails);
        personToReturn.setPhoneNumbers(phones);

        return personToReturn;
    }
}
