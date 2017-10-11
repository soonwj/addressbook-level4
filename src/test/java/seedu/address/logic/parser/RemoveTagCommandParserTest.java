package seedu.address.logic.parser;

import org.junit.Test;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemoveTagCommand;
import seedu.address.model.tag.Tag;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;


/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class RemoveTagCommandParserTest {

    private RemoveTagCommandParser parser = new RemoveTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsRemoveTagCommand() {
        try {
            RemoveTagCommand expectedRemoveTagCommand =
                    new RemoveTagCommand(new Tag("friend"));
            assertParseSuccess(parser, "friend", expectedRemoveTagCommand);
        } catch (IllegalValueException e) {
            throw new AssertionError("Default tag's value is invalid.");
        }
    }
}
