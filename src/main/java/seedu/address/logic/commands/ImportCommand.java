package seedu.address.logic.commands;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import com.google.common.eventbus.Subscribe;
import seedu.address.commons.events.logic.GoogleApiAuthServiceCredentialsSetupCompleted;
import seedu.address.commons.events.logic.GoogleAuthRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.auth.GoogleApiAuth;

import java.io.IOException;
import java.util.List;


/**
 * Created by Philemon1 on 11/10/2017.
 */
public class ImportCommand extends Command{

    public static final String COMMAND_WORD = "import";
    public String MESSAGE_SUCCESS = "Import Success";
    public String MESSAGE_FAIL = "Import Failed";
    private GoogleApiAuth authService;
    private PeopleService peopleService;
    private HttpTransport httpTransport;
    private JacksonFactory jsonFactory;

    public ImportCommand(){
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
    private void handleGoogleApiAuthServiceCredentialsSetupComplete(GoogleApiAuthServiceCredentialsSetupCompleted event)
    {
        System.out.println("Credentials set");
        peopleService = new PeopleService.Builder(httpTransport, jsonFactory, authService.getCredential())
                .setApplicationName("CS2103T - Doc")
                .build();
        try {
            ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                    .setPersonFields("names,emailAddresses,phoneNumbers,addresses")
                    .execute();
            List<Person> connections = response.getConnections();
            for(Person p: connections) {
                System.out.println(p);
            }
        } catch (IOException e) {
            System.out.print(e);
        }

    }

}
