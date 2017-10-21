package seedu.address.logic.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Address;
import com.google.api.services.people.v1.model.ContactGroup;
import com.google.api.services.people.v1.model.CreateContactGroupRequest;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.api.services.people.v1.model.Photo;
import com.google.api.services.people.v1.model.UserDefined;
import com.google.common.eventbus.Subscribe;

import seedu.address.commons.auth.GoogleApiAuth;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.GoogleApiAuthServiceCredentialsSetupCompleted;
import seedu.address.commons.events.logic.GoogleAuthRequestEvent;
import seedu.address.commons.events.logic.GoogleAuthenticationSuccessEvent;
import seedu.address.commons.util.GooglePersonConverterUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

public class ExportCommand extends GoogleCommand {
    public static final String COMMAND_WORD = "export";

    //Scope includes write access to a users' Google Contacts
    public static final String ACCESS_SCOPE = "https://www.googleapis.com/auth/contacts";

    private PeopleService peopleService;

    public ExportCommand() throws IOException{
        super(COMMAND_WORD, ACCESS_SCOPE);
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
     * @param event Should be fired from the BrowserPanel, with an authcode.
     */
    @Override
    @Subscribe
    protected void handleAuthenticationSuccessEvent(GoogleAuthenticationSuccessEvent event) {
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

        //HTTP calls
        for (com.google.api.services.people.v1.model.Person p : googlePersonList) {
            try {
                peopleService.people().createContact(p).execute();
            } catch (IOException E) {
                System.out.println(E);
            }
        }
    }

    @Override
    public String getAuthenticationUrl() {
        return new GoogleBrowserClientRequestUrl(CLIENT_ID, getRedirectUrl(), Arrays.asList(getAccessScope())).build();
    }


    private boolean commandTypeCheck(String inputCommandType) {
        return commandType.equals("GOOGLE_export");
    }
    public String getCommandType() {
        return commandType;
    }
    public String getAccessScope() {
        return accessScope;
    }


    /**
     * Event listener for successful setup of authService's credentials
     *
     */
    @Subscribe
    private void handleGoogleApiAuthServiceCredentialsSetupComplete(GoogleApiAuthServiceCredentialsSetupCompleted event) {
        peopleService = new PeopleService.Builder(httpTransport, jsonFactory, authService.getCredential())
                .setApplicationName("CS2103T - Doc")
                .build();

        List<ReadOnlyPerson> docPersonList = model.getAddressBook().getPersonList();
        List<com.google.api.services.people.v1.model.Person> googlePersonList = new ArrayList<>();
//        listConvertDocToGooglePerson(docPersonList, googlePersonList);

//        CreateContactGroupRequest newRequest = new CreateContactGroupRequest();
//        ContactGroup newGroup = new ContactGroup();
//        newGroup.setName("DoC Contacts");
//        newRequest.setContactGroup(new ContactGroup());


        for (com.google.api.services.people.v1.model.Person p : googlePersonList) {
            try {
                peopleService.people().createContact(p).execute();
            } catch (IOException E) {
                System.out.println(E);
            }
        }
    }

}
