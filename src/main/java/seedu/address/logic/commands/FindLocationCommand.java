package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Terminates the program.
 */
public class FindLocationCommand extends Command {

    public static final String COMMAND_WORD = "location";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds the location of selected person. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_FIND_LOCATION_SUCCESS = "Location of %1$s: %2$s";

    private final Index targetIndex;

    public FindLocationCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        ReadOnlyPerson personToFind = lastShownList.get(targetIndex.getZeroBased());

        try {
            model.findLocation(personToFind);
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_FIND_LOCATION_SUCCESS, personToFind.getName().fullName, personToFind.getAddress()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindLocationCommand // instanceof handles nulls
                && this.targetIndex.equals(((FindLocationCommand) other).targetIndex)); // state check
    }

}
