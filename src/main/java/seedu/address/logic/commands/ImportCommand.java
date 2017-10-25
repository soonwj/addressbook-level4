package seedu.address.logic.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.GoogleAuthenticationSuccessEvent;
import seedu.address.commons.events.logic.GoogleCommandCompleteEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.util.GooglePersonConverterUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**Purpose: Imports contacts from Google Contacts, fulfilling Google's OAuth2 protocol.
 * Limit of contacts retrieved set at : 1000
 * Created by Philemon1 on 11/10/2017.
 */
public class ImportCommand extends GoogleCommand {
    public static final String COMMAND_WORD = "import";

    //Scope includes read-only access to a users' Google Contacts
    public static final String ACCESS_SCOPE = "https://www.googleapis.com/auth/contacts.readonly";

    protected PeopleService peopleService;

    public ImportCommand() {
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
        if (!getCommandCompleted()) {
            //Fire event to alert status bar of conversion process
            EventsCenter.getInstance().post(
                    new NewResultAvailableEvent("Successfully authenticated - Conversion in process now"));

            //Incoming Google Person List
            List<com.google.api.services.people.v1.model.Person> googlePersonList = new ArrayList<>();

            //List of converted DoC person
            List<Person> docPersonList = new ArrayList<>();

            if (!commandTypeCheck(event.getCommandType())) {
                return;
            }

            //set up credentials
            setupCredentials(event.getAuthCode());

            //set up people service
            peopleService = new PeopleService.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("CS2103T - Doc")
                    .build();

            //HTTP calls
            try {
                ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                        .setPersonFields("names,emailAddresses,phoneNumbers,addresses")
                        .setPageSize(1000)
                        .execute();
                googlePersonList = response.getConnections();
            } catch (IOException e) {
                System.out.print(e);
            }
            //Conversion call
            docPersonList = GooglePersonConverterUtil.listGoogleToDoCPersonConversion(googlePersonList);

            //Adding to the model
            for (Person p : docPersonList) {
                try {
                    model.addPerson(p);
                } catch (DuplicatePersonException e) {
                    //Duplicate persons shall be ignored
                    continue;
                }
            }
            EventsCenter.getInstance().post(new GoogleCommandCompleteEvent(
                    "https://contacts.google.com/", commandType));
            setCommandCompleted();
        }

    }
    @Override
    public String getAuthenticationUrl() {
        return new GoogleBrowserClientRequestUrl(CLIENT_ID, getRedirectUrl(), Arrays.asList(getAccessScope())).build();
    }
    public String getAccessScope() {
        return accessScope;
    }
    private boolean commandTypeCheck(String inputCommandType) {
        return inputCommandType.equals("GOOGLE_import");
    }
}
