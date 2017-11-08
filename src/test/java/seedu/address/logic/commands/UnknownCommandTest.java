package seedu.address.logic.commands;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Created by Philemon1 on 9/11/2017.
 */
public class UnknownCommandTest {

    //Mistyped Add command with invalid (empty) parameters. Negative test.
    @Test
    public void mistypedAddTest() throws ParseException {
        UnknownCommand mistypedAddInvalidParameters = new UnknownCommand("ad", "");
        try {
            mistypedAddInvalidParameters.suggestionFound();
        } catch (ParseException E) {
            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE), E.getMessage());
        }
    }





}
