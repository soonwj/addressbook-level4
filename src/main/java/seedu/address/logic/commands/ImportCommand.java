package seedu.address.logic.commands;

import seedu.address.commons.events.logic.GoogleAuthRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.commons.core.EventsCenter;

/**
 * Created by Philemon1 on 11/10/2017.
 */
public class ImportCommand extends Command{

    public static final String COMMAND_WORD = "import";
    public String MESSAGE_SUCCESS = "Import Success";

    public ImportCommand(){}


    @Override
    public CommandResult execute() throws CommandException {
        EventsCenter.getInstance().post(new GoogleAuthRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
