package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

import java.util.List;

/**
 * Deletes a person identified using it's last displayed index from the address book.
 */
public class RemoveTagCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removeTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the tag identified from all person.\n"
            + "Example: " + COMMAND_WORD + " friends";

    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Deleted Tag: %1$s";

    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Tag targetName;

    public RemoveTagCommand(Tag targetName) {
        this.targetName = targetName;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.deleteTag(targetName);
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target Tag cannot be missing";
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, targetName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RemoveTagCommand // instanceof handles nulls
                && this.targetName.equals(((RemoveTagCommand) other).targetName)); // state check
    }
}
