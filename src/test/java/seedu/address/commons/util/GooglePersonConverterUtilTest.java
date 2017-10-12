package seedu.address.commons.util;

import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.Address;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.Name;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.commons.exceptions.InvalidGooglePersonException;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Unit testing for the GooglePersonConverterUtil class
 * Checks that the converter handles exceptions gracefully, and sets default values correctly
 */
public class GooglePersonConverterUtilTest {
    private GooglePersonConverterUtil convertTool = new GooglePersonConverterUtil();

    /**
     * Verifies the conversion result for Google Person with null parameters
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void convertGooglePersonWithNullNameException() throws InvalidGooglePersonException{
        Person testPerson = getGooglePerson(null, "12345678", "blk001 Test Ave"
                , "test@gmail.com");
        thrown.expect(InvalidGooglePersonException.class);
        GooglePersonConverterUtil.convertPerson(testPerson);
    }

    @Test
    public void convertGooglePersonWithNullPhoneException() throws InvalidGooglePersonException{
        Person testPerson = getGooglePerson("Testee", null, "blk001 Test Ave"
                , "test@gmail.com");
        thrown.expect(InvalidGooglePersonException.class);
        GooglePersonConverterUtil.convertPerson(testPerson);
    }

    @Test
    public void convertGooglePersonWithNullEmail() throws InvalidGooglePersonException{
        Person testPerson = getGooglePerson("Testee", "12345678", "blk001 Test Ave"
                , null);
        seedu.address.model.person.Person convertedPerson = GooglePersonConverterUtil.convertPerson(testPerson);
        assertEquals(convertedPerson.getEmail().toString(), GooglePersonConverterUtil.DEFAULT_EMAIL);
    }

    @Test
    public void convertGooglePersonWithNullAddress() throws InvalidGooglePersonException{
        Person testPerson = getGooglePerson("Testee", "12345678", null
                , "test@gmail.com");
        seedu.address.model.person.Person convertedPerson = GooglePersonConverterUtil.convertPerson(testPerson);
        assertEquals(convertedPerson.getAddress().toString(), GooglePersonConverterUtil.DEFAULT_ADDRESS);
    }

    public Person getGooglePerson(String name, String number, String address, String email) {
        ArrayList<Name> names = new ArrayList<>();
        ArrayList<PhoneNumber> phones = new ArrayList<>();
        ArrayList<Address> addresses = new ArrayList<>();
        ArrayList<EmailAddress> emails = new ArrayList<>();
        Person personToReturn = new Person();

        names.add(new Name().setDisplayName(name));
        phones.add(new PhoneNumber().setValue(number));
        addresses.add(new Address().setFormattedValue(address));
        emails.add(new EmailAddress().setValue(email));

        personToReturn.setNames(names);
        personToReturn.setAddresses(addresses);
        personToReturn.setEmailAddresses(emails);
        personToReturn.setPhoneNumbers(phones);

        return personToReturn;
    }
}
