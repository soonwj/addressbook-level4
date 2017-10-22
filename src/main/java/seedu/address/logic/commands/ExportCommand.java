package seedu.address.logic.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.services.people.v1.PeopleService;

import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.CreateContactGroupRequest;
import com.google.api.services.people.v1.model.ModifyContactGroupMembersRequest;
import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.GoogleAuthenticationSuccessEvent;
import seedu.address.commons.events.logic.GoogleCommandCompleteEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.util.GooglePersonConverterUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

/**Purpose: Exports contacts to Google Contacts, fulfilling Google's OAuth2 protocol.
 * Inherits from GoogleCommand & Oauth2Command
 */
public class ExportCommand extends GoogleCommand {
    public static final String COMMAND_WORD = "export";

    //Scope includes write access to a users' Google Contacts
    public static final String ACCESS_SCOPE = "https://www.googleapis.com/auth/contacts";

    private static final String googleContactsGroupView = "https://contacts.google.com/label/";
    private PeopleService peopleService;

    public ExportCommand() {
        super(COMMAND_WORD, ACCESS_SCOPE);
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public CommandResult execute() throws CommandException {
        //Fires an event to the BrowserPanel
        try {
            triggerBrowserAuth();
        } catch (IOException e) {
            throw new CommandException("Failed to trigger browser auth");
        }
        return new CommandResult("Authentication in process");
    }

    /**
     * Event listener for a successful authentication
     * @param event Should be fired from the BrowserPanel, with an authcode
     */
    @Override
    @Subscribe
    protected void handleAuthenticationSuccessEvent(GoogleAuthenticationSuccessEvent event) {
        //Fire event to alert status bar of conversion process
        EventsCenter.getInstance().post(
                new NewResultAvailableEvent("Successfully authenticated - Conversion in process now"));

        if (!commandTypeCheck(event.getCommandType())) {
            return;
        }
        //set up credentials
        setupCredentials(event.getAuthCode());

        //set up people service
        peopleService = new PeopleService.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("CS2103T - Doc")
                .build();

        //Conversion calls
        List<ReadOnlyPerson> docPersonList = model.getAddressBook().getPersonList();
        List<com.google.api.services.people.v1.model.Person> googlePersonList =
                GooglePersonConverterUtil.listDocToGooglePersonConversion(docPersonList);

        //Set contactGroupId for exporting to a specific contact group: `Imported From DoC`
        String contactGroupId = retrieveExistingContractGroupResourceName();

        //Create a new contact group titled `Imported From DoC`, if still not set
        if (contactGroupId == null) {
            contactGroupId = createNewContactGroup();
        }

        //HTTP calls - exporting
        for (com.google.api.services.people.v1.model.Person p : googlePersonList) {
            try {
                //create Contact
                String newPersonId;
                newPersonId = peopleService.people().createContact(p).execute().getResourceName();

                //set Contact's group
                peopleService.contactGroups().members().modify(contactGroupId,
                        new ModifyContactGroupMembersRequest()
                                .setResourceNamesToAdd(GooglePersonConverterUtil
                                        .makeListFromOne(newPersonId)))
                                .execute();
            } catch (IOException E) {
                System.out.println(E);
            }
        }
        EventsCenter.getInstance().post(new GoogleCommandCompleteEvent(
                googleContactsGroupView + contactGroupId.split("/")[1], commandType));
    }

    /**
     * Helper Method, executed when no existing Contact Group with the name: "ImportedFromGoogle" is found
     * @return the ResourceName String of the newly created Contact Group ID
     */
    private String createNewContactGroup() {
        String contactGroupId = null;
        try {
            contactGroupId = peopleService.contactGroups().create(
                    new CreateContactGroupRequest()
                            .setContactGroup(new ContactGroup().setName("Imported From DoC")))
                    .execute().getResourceName();
        } catch (IOException e7) {
            System.out.println(e7);
            assert true : "google server error";
        }
        return contactGroupId;
    }

    /**
     * Helper Method, tries to fetch the ResourceName String of an existing Contact Group named: "ImportedFromGoogle"
     * @return the ResourceName String of the existing Contact Group
     */
    private String retrieveExistingContractGroupResourceName() {
        String contactGroupId = null;
        //Fetch all Contact Groups
        List<ContactGroup> contactGroupList = new ArrayList<>();
        try {
            contactGroupList = peopleService.contactGroups().list().execute().getContactGroups();
        } catch (IOException e4) {
            System.out.println(e4);
        }
        for (ContactGroup c : contactGroupList) {
            if (c.getFormattedName().equals("Imported From DoC")) {
                contactGroupId = c.getResourceName();
                break;
            }
        }
        return contactGroupId;
    }

    @Override
    public String getAuthenticationUrl() {
        return new GoogleBrowserClientRequestUrl(CLIENT_ID, getRedirectUrl(), Arrays.asList(getAccessScope())).build();
    }

    private boolean commandTypeCheck(String inputCommandType) {
        return inputCommandType.equals("GOOGLE_export");
    }

    public String getAccessScope() {
        return accessScope;
    }

}
