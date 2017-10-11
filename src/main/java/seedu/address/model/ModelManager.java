package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.api.services.people.v1.model.Address;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.UserDefined;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<ReadOnlyPerson> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(ReadOnlyPerson person) throws DuplicatePersonException {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(target, editedPerson);

        addressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    @Override
    public void export() throws PersonNotFoundException {
        ObservableList<ReadOnlyPerson> contacts = addressBook.getPersonList();

        if (contacts.size() == 0) {
            throw new PersonNotFoundException();
        }
        else {
            ArrayList<String> contactResourceNames = new ArrayList<>();

            for(ReadOnlyPerson contact : contacts) {
                Person newPerson = new Person();

                List<Name> names = new ArrayList<>();
                names.add(new Name().setDisplayName(contact.getName().toString()));
                newPerson.setNames(names);

                List<PhoneNumber> phones = new ArrayList<>();
                phones.add(new PhoneNumber().setValue(contact.getPhone().toString()));
                newPerson.setPhoneNumbers(phones);

                List<Address> addresses = new ArrayList<>();
                addresses.add(new Address().setExtendedAddress(contact.getAddress().toString()));
                newPerson.setAddresses(addresses);

                List<EmailAddress> emails = new ArrayList<>();
                emails.add(new EmailAddress().setValue(contact.getEmail().toString()));
                newPerson.setEmailAddresses(emails);

                List<UserDefined> tags = new ArrayList<>();
                Set<Tag> tagList = contact.getTags();
                for(Tag t : tagList) {
                    UserDefined tag = new UserDefined();
                    tag.setKey("Tag");
                    tag.setValue(t.toString());
                    tags.add(tag);
                }
                newPerson.setUserDefined(tags);

                Person createdPerson = peopleService.people().createContact(newPerson).execute();
                contactResourceNames.add(createdPerson.getResourceName());
            }

            ContactGroup newGroup = new ContactGroup();
            newGroup.setMemberResourceNames(contactResourceNames);
            newGroup.setName("AddressBook Contacts @ " + new Date().getTime());

            personService.contactGroups().create(newGroup).execute();
        }
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code ReadOnlyPerson} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<ReadOnlyPerson> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && filteredPersons.equals(other.filteredPersons);
    }

}
