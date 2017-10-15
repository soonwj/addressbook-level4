package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IMAGE_URL;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.UpdateProfilePicCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.util.SampleDataUtil;

/**
 * Parses input arguments and creates a new UpdateProfilePicCommand object
 */
public class UpdateProfilePicCommandParser implements Parser<UpdateProfilePicCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the UpdateProfilePicCommand
     * and returns an UpdateProfilePicCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    @Override
    public UpdateProfilePicCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_IMAGE_URL);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UpdateProfilePicCommand.MESSAGE_USAGE));
        }

        Person updatedPerson = new Person(SampleDataUtil.getSamplePersons()[0]);

        try {
            ParserUtil.parseImageURL(argMultimap.getValue(PREFIX_IMAGE_URL)).ifPresent(updatedPerson::setProfilePic);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (updatedPerson.getProfilePic().toString().equals("file:///D:/D_/Users/Assassin Ranger/Documents/SWJ/NUS/Yr 2 Sem 1/CS2103T/addressbook-level4/src/main/resources/images/fail.png")) {
            throw new ParseException(UpdateProfilePicCommand.MESSAGE_NOT_UPDATED);
        }

        return new UpdateProfilePicCommand(index, updatedPerson.getProfilePic());
    }
}
