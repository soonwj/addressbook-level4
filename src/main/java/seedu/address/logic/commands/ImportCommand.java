package seedu.address.logic.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import com.google.common.eventbus.Subscribe;

import seedu.address.commons.auth.GoogleApiAuth;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.GoogleApiAuthServiceCredentialsSetupCompleted;
import seedu.address.commons.events.logic.GoogleAuthRequestEvent;
import seedu.address.commons.events.logic.GoogleAuthenticationSuccessEvent;
import seedu.address.commons.exceptions.InvalidGooglePersonException;
import seedu.address.commons.util.GooglePersonConverterUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.DuplicatePersonException;


/**Purpose: Imports contacts from Google Contacts, fulfilling Google's OAuth2 protocol.
 * Limit of contacts retrieved set at : 1000
 * Each instance of this method will maintain it's own GoogleApiAuth authService. Token re-use is not supported.
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
    @Subscribe
    private void handeAuthenticationSuccessEvent(GoogleAuthenticationSuccessEvent event) {
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

        //Conversion calls
        try {
            docPersonList = GooglePersonConverterUtil.listGoogleToDoCPersonConversion(googlePersonList);
        } catch (InvalidGooglePersonException e) {
            assert true: "Google server returning an unexpected Person";
        }





    }

    private boolean commandTypeCheck(String inputCommandType) {
        return inputCommandType.equals("GOOGLE_import");
    }

    /**
     * Final step of the import procedure, converts all Google Person in the list connections, to a model.person
     * .Person, then adds it to the model.
     * @param connections
     */
    private void convertAndAddAll(List<Person> connections) {
        for (Person p: connections) {
            try {
                seedu.address.model.person.Person temp = GooglePersonConverterUtil.convertPerson(p);
                if (temp != null) {
                    model.addPerson(GooglePersonConverterUtil.convertPerson(p));
                }
            } catch (DuplicatePersonException e) {
                System.out.println(e);
            } catch (InvalidGooglePersonException e) {
                System.out.println(e);
            }
        }
    }

}
