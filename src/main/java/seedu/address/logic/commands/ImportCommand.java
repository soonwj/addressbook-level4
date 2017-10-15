package seedu.address.logic.commands;

import java.io.IOException;
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
import seedu.address.commons.exceptions.InvalidGooglePersonException;
import seedu.address.commons.util.GooglePersonConverterUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.DuplicatePersonException;


/**Purpose: Imports contacts from Google Contacts, fulfilling Google's OAuth2 protocol.
 * Limit of contacts retrieved set at : 1000
 * Each instance of this method will maintain it's own GoogleApiAuth authService. Token re-use is not supported.
 * Created by Philemon1 on 11/10/2017.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_SUCCESS = "Please proceed to login";
    private GoogleApiAuth authService;
    private PeopleService peopleService;
    private HttpTransport httpTransport;
    private JacksonFactory jsonFactory;

    public ImportCommand() {
        authService = new GoogleApiAuth();
        EventsCenter.getInstance().registerHandler(this);
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
    }


    @Override
    public CommandResult execute() throws CommandException {
        EventsCenter.getInstance().post(new GoogleAuthRequestEvent(authService));
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Event listener for successful setup of authService's credentials
     * @param event
     */
    @Subscribe
    private void handleGoogleApiAuthServiceCredentialsSetupComplete(GoogleApiAuthServiceCredentialsSetupCompleted
                                                                                event) {
        peopleService = new PeopleService.Builder(httpTransport, jsonFactory, authService.getCredential())
                .setApplicationName("CS2103T - Doc")
                .build();
        try {
            ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                    .setPersonFields("names,emailAddresses,phoneNumbers,addresses")
                    .setPageSize(1000)
                    .execute();
            List<Person> connections = response.getConnections();
            convertAndAddAll(connections);
        } catch (IOException e) {
            System.out.print(e);
        }

    }

    /**
     * Final step of the import procedure, converts all Google Person the list connections, to a model.person
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
