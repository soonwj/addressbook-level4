package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Exports all contacts into a new ContactGroup on Google Contacts
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_SUCCESS = "All Contacts Successfully Exported";

    @Override
    public CommandResult execute() throws CommandException {
        try{
            model.export();
        }
        catch (PersonNotFoundException pnfe) {
            assert false : "No contacts to export.";
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
