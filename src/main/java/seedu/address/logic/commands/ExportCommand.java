package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.common.eventbus.Subscribe;

import seedu.address.commons.auth.GoogleApiAuth;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.GoogleApiAuthServiceCredentialsSetupCompleted;
import seedu.address.commons.events.logic.GoogleAuthRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;

public class ExportCommand extends ImportCommand {
    public static final String COMMAND_WORD = "export";
    public ExportCommand() {}

    @Override
    public CommandResult execute() throws CommandException {
        EventsCenter.getInstance().post(new GoogleAuthRequestEvent(authService));
        return null;
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
        
    }



}
