package seedu.address.commons.util;


import com.google.api.services.people.v1.model.Person;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

import java.util.HashSet;
import java.util.Set;


/**This class is a service to convert Person objects from Google's API, to the Person object declared in
 * seedu.address.model.person.Person. Both classes in different packages have the same name.
 * Handling: Google's API Person is imported, while seedu.address.model.person.Person has to be fully qualified
 * Created by Philemon1 on 12/10/2017.
 */
public class GooglePersonConverterUtil {
    public static final String DEFAULT_TAGS = "ImportedFromGoogle";

    public static seedu.address.model.person.Person convertPerson(Person person){
        String tempName = person.getNames().get(0).getDisplayName();
        String tempPhoneNumber = person.getPhoneNumbers().get(0).getValue();
        String tempEmailAddress = null;
        String tempAddress = null;
        seedu.address.model.person.Person tempPerson;

        tempName = processName(tempName);

        if (tempPhoneNumber == null) {
            tempPhoneNumber = "00000000";
        }
        if (tempEmailAddress == null) {
            tempEmailAddress = "INVALID_EMAIL@INVALID.COM";
        }
        if (tempAddress == null) {
            tempAddress = "INVALID_ADDRESS PLEASE UPDATE THIS";
        }
        System.out.println(tempName);
        System.out.println(tempAddress);
        System.out.println(tempEmailAddress);
        System.out.println(tempPhoneNumber);

        try {
            Name nameObj = new Name(tempName);
            Email emailAddressObj = new Email(tempEmailAddress);
            Phone phoneObj = new Phone(tempPhoneNumber);
            Address addressObj = new Address(tempAddress);
            tempPerson = new seedu.address.model.person.Person(nameObj, phoneObj, emailAddressObj, addressObj, SampleDataUtil.getTagSet(DEFAULT_TAGS));
            return tempPerson;

        } catch (IllegalValueException e) {
            System.out.println("asdadasd");
            System.out.println(e);
        }
        return null;
    }

    public static String processName(String tempName) {
        String tempName1 = tempName.replace("(", "");
        String tempName2 = tempName1.replace(")", "");
        return tempName2;
    }




}
