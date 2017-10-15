package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ProfilePic;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;



/**
 * Updates the profile picture of an existing person in the address book.
 */

public class UpdateProfilePicCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "updateProfilePic";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates the profile picture of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing profile picture will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[valid URL of image]\n"
            + "Example of image stored locally: " + COMMAND_WORD + " 1 "
            + "file:///C:/Users/Bobby/Images/picture.jpg\n"
            + "Example of image stored on the internet: " + COMMAND_WORD + " 1 "
            + "http://www.google.com/images/picture2.png\n";

    public static final String MESSAGE_UPDATE_PROFILE_PIC_SUCCESS = "Update profile pic of Person: %1$s";
    public static final String MESSAGE_NOT_UPDATED = "Please enter a valid image URL.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final ProfilePic profilePic;

    /**
     *
     * @param index of the person in the filtered person list to update profile picture
     * @param profilePic of the image to be used as the profile picture
     */
    public UpdateProfilePicCommand(Index index, ProfilePic profilePic) {
        requireNonNull(index);
        requireNonNull(profilePic);

        this.index = index;
        this.profilePic = profilePic;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToUpdateProfilePic = lastShownList.get(index.getZeroBased());
        Person updatedProfilePicPerson = new Person(personToUpdateProfilePic);
        updatedProfilePicPerson.setProfilePic(profilePic);

        try {
            model.updatePerson(personToUpdateProfilePic, updatedProfilePicPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_UPDATE_PROFILE_PIC_SUCCESS, personToUpdateProfilePic));
    }
}
