package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Exports all contacts into a new ContactGroup on Google Contacts
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_SUCCESS = "All Contacts Successfully Exported";

    public static final String MESSAGE_EMPTY_ADDRESS_BOOK = "No Contacts Found, Export Not Run";

    @Override
    public CommandResult execute() throws CommandException {
        try{
            model.export();
        }
        catch (PersonNotFoundException pnfe) {
            return new CommandResult(MESSAGE_EMPTY_ADDRESS_BOOK);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
