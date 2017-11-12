package seedu.address.logic.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.services.people.v1.PeopleService;

import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.CreateContactGroupRequest;
import com.google.api.services.people.v1.model.ModifyContactGroupMembersRequest;
import com.google.common.eventbus.Subscribe;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.GoogleAuthenticationSuccessEvent;
import seedu.address.commons.events.logic.GoogleCommandCompleteEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.util.GooglePersonConverterUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

//@@author philemontan
/**
 * Purpose: Exports DoC's contacts to Google Contacts, with the OAuth2 protocol against the Google People API.
 * Inherits from GoogleCommand & Oauth2Command
 */
public class ExportCommand extends GoogleCommand {
    public static final String COMMAND_WORD = "export";

    //This scope includes write access to a users' Google Contacts
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
            throw new CommandException("Failed to trigger the authentication process with the built-in browser.");
        }
        return new CommandResult("Authentication process initiated. Please login on the built-in " +
                "browser.");
    }

    /**
     * Event listener for a successful authentication
     * @param event Should be fired from the BrowserPanel, with an authcode
     */
    @Override
    @Subscribe
    protected void handleAuthenticationSuccessEvent(GoogleAuthenticationSuccessEvent event) {
        if (!getCommandCompleted()) {
            //Fire event to alert status bar of conversion process
            EventsCenter.getInstance().post(
                    new NewResultAvailableEvent("Successfully authenticated - Conversion in process now",
                            false));

            if (!commandTypeCheck(event.getCommandType())) {
                return;
            }

            setupCredentials(event.getAuthCode());

            //set up people service
            peopleService = new PeopleService.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("CS2103T - Doc")
                    .build();

            String contactGroupId = setContactGroupId();

            startBackgroundConversionAndHttpCalls(contactGroupId);

            EventsCenter.getInstance().post(new GoogleCommandCompleteEvent(
                    googleContactsGroupView + contactGroupId.split("/")[1], getCommandType()));

            setCommandCompleted();
        }
    }

    private String setContactGroupId() {
        //Set contactGroupId for exporting to a specific contact group: `Imported From DoC`
        String contactGroupId = retrieveExistingContractGroupResourceName();

        //Create a new contact group titled `Imported From DoC`, if still not set
        if (contactGroupId == null) {
            contactGroupId = createNewContactGroup();
        }
        return contactGroupId;
    }

    private void startBackgroundConversionAndHttpCalls(String contactGroupId) {
        ExportBackgroundService exportBackgroundService = new ExportBackgroundService(contactGroupId);
        exportBackgroundService.start();
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
        } catch (IOException E) {
            EventsCenter.getInstance().post(new NewResultAvailableEvent(FAILED_CONNECTION_MESSAGE, true));
            setCommandCompleted();
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
        } catch (IOException E) {
            EventsCenter.getInstance().post(new NewResultAvailableEvent(FAILED_CONNECTION_MESSAGE, true));
            setCommandCompleted();
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
        return new GoogleBrowserClientRequestUrl(getClientId(), getRedirectUrl(),
                Arrays.asList(ACCESS_SCOPE)).build();
    }

    private boolean commandTypeCheck(String inputCommandType) {
        return inputCommandType.equals("GOOGLE_export");
    }

    public String getAccessScope() {
        return accessScope;
    }

    /**
     * This class extends the javafx.concurrent.Service class, to run the intensive conversion and http calls in
     * a separate background thread.
     */
    class ExportBackgroundService extends Service {
        private String contactGroupId;

        public ExportBackgroundService(String inputContactGroupId) {
            contactGroupId = inputContactGroupId;
        }

        @Override
        protected Task createTask() {
            return new Task() {
                @Override
                protected Object call() throws Exception {
                    //Conversion calls
                    List<ReadOnlyPerson> docPersonList = model.getAddressBook().getPersonList();
                    List<com.google.api.services.people.v1.model.Person> googlePersonList =
                            GooglePersonConverterUtil.listDocToGooglePersonConversion(docPersonList);

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
                    return null;
                }
            };
        }
    }

}


