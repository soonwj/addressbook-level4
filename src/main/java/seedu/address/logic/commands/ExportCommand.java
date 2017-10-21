package seedu.address.logic.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.services.people.v1.PeopleService;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.logic.GoogleAuthenticationSuccessEvent;
import seedu.address.commons.util.GooglePersonConverterUtil;
import seedu.address.logic.commands.exceptions.CommandException;

import seedu.address.model.person.ReadOnlyPerson;

/**
 * This class fulfils the exporting of DoC Persons to Google
 */
public class ExportCommand extends GoogleCommand {
    public static final String COMMAND_WORD = "export";

    //Scope includes write access to a users' Google Contacts
    public static final String ACCESS_SCOPE = "https://www.googleapis.com/auth/contacts";

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
     * @param event Should be fired from the BrowserPanel, with an authcode.
     */
    @Override
    @Subscribe
    protected void handleAuthenticationSuccessEvent(GoogleAuthenticationSuccessEvent event) {
        System.out.println("handling");
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
        return inputCommandType.equals("GOOGLE_export");
    }

    public String getAccessScope() {
        return accessScope;
    }

}
