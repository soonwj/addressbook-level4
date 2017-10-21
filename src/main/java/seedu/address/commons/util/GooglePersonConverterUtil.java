package seedu.address.commons.util;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.InvalidGooglePersonException;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

import java.util.ArrayList;
import java.util.List;


/**This class provides the service of two-way conversion between Google Person(s) and Doc Person(s)
 * Both classes and their composed classes in each package have the same name;
 * Handling: seedu.address.model.person.Person will be imported, while Gooogle classes have to be fully qualified.
 * Created by Philemon1 on 12/10/2017.
 */
public abstract class GooglePersonConverterUtil {
    public static final String DEFAULT_TAGS = "ImportedFromGoogle";
    public static final String DEFAULT_EMAIL = "INVALID_EMAIL@INVALID.COM";
    public static final String DEFAULT_ADDRESS = "INVALID_ADDRESS PLEASE UPDATE THIS";


    /**
     * Conversion: (Single) Google Person -> DoC Person
     * @param person input parameter of a single Google Person
     * @return the converted DoC version of the Google Person
     * @throws InvalidGooglePersonException if the Google Person instance has a null name or phone
     */
    public static Person singleGoogleToDocPersonConversion(com.google.api.services.people.v1.model.Person person) throws
            InvalidGooglePersonException {
        //Property declarations for the DoC Person
        String tempName = null;
        String tempPhoneNumber = null;
        String tempEmailAddress = null;
        String tempAddress = null;
        seedu.address.model.person.Person tempPerson;

        //Try block to extract required properties
        try {
            tempName = person.getNames().get(0).getDisplayName();
            tempPhoneNumber = person.getPhoneNumbers().get(0).getValue();
            tempEmailAddress = person.getEmailAddresses().get(0).getValue();
            tempAddress = person.getAddresses().get(0).getFormattedValue();
        } catch (IndexOutOfBoundsException | NullPointerException E) {
            if (tempName == null | tempPhoneNumber == null) {
                throw new InvalidGooglePersonException("Name and Phone number cannot be null");
            }
        }

        //Process Name and Number in accordance to DoC Name and Number regex
        tempName = processName(tempName);
        tempPhoneNumber = processNumber(tempPhoneNumber);

        //Setting optional properties to default constants if null
        if (tempEmailAddress == null) {
            tempEmailAddress = DEFAULT_EMAIL;
        }
        if (tempAddress == null) {
            tempAddress = DEFAULT_ADDRESS;
        }

        //Instantiating DoC person to return
        try {
            Name nameObj = new Name(tempName);
            Email emailAddressObj = new Email(tempEmailAddress);
            Phone phoneObj = new Phone(tempPhoneNumber);
            Address addressObj = new Address(tempAddress);
            tempPerson = new seedu.address.model.person.Person(nameObj,
                    phoneObj, emailAddressObj, addressObj, SampleDataUtil.getTagSet(DEFAULT_TAGS));
            return tempPerson;
        } catch (IllegalValueException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Conversion: (Single) DoC Person -> Google Person
     * Note: Google Person Photo is read only and cannot be set from the Google People API
     * @param person input parameter of a single Google Person
     * @return the converted Google version of the input DoC Person
     */
    public static com.google.api.services.people.v1.model.Person singleDocToGooglePersonConversion(Person person) {
        /**
         * Creating Google Person properties, excluding tags
         */
        //Google Name
        com.google.api.services.people.v1.model.Name googleName = new com.google.api.services.people.v1.model.Name()
                .setGivenName(person.getName().fullName);
        //Google Phone Number
        com.google.api.services.people.v1.model.PhoneNumber googleNumber =
                new com.google.api.services.people.v1.model.PhoneNumber().setValue(person.getPhone().value);
        //Google Email Address
        com.google.api.services.people.v1.model.EmailAddress googleEmail =
                new com.google.api.services.people.v1.model.EmailAddress()
                .setValue(person.getEmail().value);
        //Google Address
        com.google.api.services.people.v1.model.Address googleAddress =
                new com.google.api.services.people.v1.model.Address().setFormattedValue(person.getAddress().value);
        //Google Person
        com.google.api.services.people.v1.model.Person tempPerson =
                new com.google.api.services.people.v1.model.Person();

        /**
         * Creating Lists from single properties, to fulfil Google Person requirements
         */
        List<com.google.api.services.people.v1.model.Name> googleNameList = makeListFromOne(googleName);
        List<com.google.api.services.people.v1.model.PhoneNumber> googlePhoneNumberList = makeListFromOne(googleNumber);
        List<com.google.api.services.people.v1.model.EmailAddress> googleEmailAddressList = makeListFromOne(googleEmail);
        List<com.google.api.services.people.v1.model.Address> googleAddressList = makeListFromOne(googleAddress);


        /**
         * Creating a List of Google UserDefined objects, to use as DoC Person Tags
         */
        List<com.google.api.services.people.v1.model.UserDefined> googleTagList =
                new ArrayList<com.google.api.services.people.v1.model.UserDefined>();

        for (Tag t : person.getTags()) {
            com.google.api.services.people.v1.model.UserDefined tempGoogleTag =
                    new com.google.api.services.people.v1.model.UserDefined();
            tempGoogleTag.setKey("tag");
            tempGoogleTag.setValue(t.tagName);
            googleTagList.add(tempGoogleTag);
        }

        /**
         * Finalizing the return Google Person object
         */
        tempPerson.setNames(googleNameList);
        tempPerson.setPhoneNumbers(googlePhoneNumberList);
        tempPerson.setEmailAddresses(googleEmailAddressList);
        tempPerson.setAddresses(googleAddressList);
        tempPerson.setUserDefined(googleTagList);

        return tempPerson;
    }

    /**
     * Helper method that returns an ArrayList<E> created with a single E instance.
     * This is required when instantiating a Google Person
     */
    private static  <E> List<E> makeListFromOne(E singlePropertyInput) {
        ArrayList<E> tempList = new ArrayList<>();
        tempList.add(singlePropertyInput);
        return tempList;
    }




    /**
     * Main functionality of the Util: converts a Google Person to a ..model.person.Person
     * @param person
     * @return Returns a model.person.Person instance, converted from the Google Person instance
     * @throws InvalidGooglePersonException if input parameter Person has null name or phone
     */
    public static Person convertPerson(com.google.api.services.people.v1.model.Person person)
            throws InvalidGooglePersonException {
        String tempName = new String();
        String tempPhoneNumber = new String();
        String tempEmailAddress;
        String tempAddress;
        seedu.address.model.person.Person tempPerson;
        try {
            tempName = person.getNames().get(0).getDisplayName();
            tempPhoneNumber = person.getPhoneNumbers().get(0).getValue();
            tempEmailAddress = person.getEmailAddresses().get(0).getValue();
            tempAddress = person.getAddresses().get(0).getFormattedValue();
        } catch (IndexOutOfBoundsException | NullPointerException E) {
            tempEmailAddress = null;
            tempAddress = null;
        }

        if (tempName == null | tempPhoneNumber == null) {
            throw new InvalidGooglePersonException("Name and Phone number cannot be null");
        }
        //Assumed to be non-null for now
        tempName = processName(tempName);
        tempPhoneNumber = processNumber(tempPhoneNumber);

        if (tempEmailAddress == null) {
            tempEmailAddress = DEFAULT_EMAIL;
        }
        if (tempAddress == null) {
            tempAddress = DEFAULT_ADDRESS;
        }
        try {
            Name nameObj = new Name(tempName);
            Email emailAddressObj = new Email(tempEmailAddress);
            Phone phoneObj = new Phone(tempPhoneNumber);
            Address addressObj = new Address(tempAddress);
            tempPerson = new seedu.address.model.person.Person(nameObj,
                    phoneObj, emailAddressObj, addressObj, SampleDataUtil.getTagSet(DEFAULT_TAGS));
            return tempPerson;

        } catch (IllegalValueException e) {
            System.out.println(e);
        }
        return null;
    }


    public static String processName(String tempName) {
        return tempName.replaceAll("[^a-zA-Z0-9]", " ");
    }

    /**
     * Helper method that removes all characters not in the allowed regex for a model.person.Person.Phone's value
     * @param tempNumber
     * @return the corrected phone number in the form of a String
     */
    public static String processNumber(String tempNumber) {
        if (tempNumber == null) {
            tempNumber = "00000000";
        }
        if (tempNumber.contains("+65")) {
            tempNumber.replace("+65", "");
        }
        return tempNumber.replaceAll("[^0-9]", "");
    }


}
