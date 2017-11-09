package seedu.address.logic.commands;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import seedu.address.logic.commands.ClearCommand;

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

    //Mistyped AddE command with invalid (empty) parameters. Negative test.
    @Test
    public void mistypedAddETest() throws ParseException {
        UnknownCommand mistypedAddEInvalidParameters = new UnknownCommand("adddE", "");
        try {
            mistypedAddEInvalidParameters.suggestionFound();
        } catch (ParseException E) {
            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE), E.getMessage());
        }
    }

    //Mistyped Clear command. Negative test.
    @Test
    public void mistypedClearTest() throws ParseException {
        UnknownCommand mistypedClearCommand = new UnknownCommand("cler", "");
        Class clearCommandClass = null;
        try {
            mistypedClearCommand.suggestionFound();
        } catch (ParseException E1) {
            assert false: "Unexpected behaviour: Clear should not throw a parse exception with empty parameters";
        }
        try {
            clearCommandClass = Class.forName("seedu.address.logic.commands.ClearCommand");
        } catch (ClassNotFoundException E2) {
            assert false: "Unexpected behaviour: ClearCommand class cannot be found";
        }
        assertTrue(mistypedClearCommand.getSuggestedCommand().getClass().getSimpleName()
                .equals(clearCommandClass.getSimpleName()));
    }
    //Mistyped Delete command with invalid (empty) parameters. Negative test.
    @Test
    public void mistypedDeleteTest() throws ParseException {
        UnknownCommand mistypedAddInvalidParameters = new UnknownCommand("dele", "");
        try {
            mistypedAddInvalidParameters.suggestionFound();
        } catch (ParseException E) {
            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), E.getMessage());
        }
    }

    //Mistyped DeleteE command with invalid (empty) parameters. Negative test.
    @Test
    public void mistypedDeleteETest() throws ParseException {
        UnknownCommand mistypedAddInvalidParameters = new UnknownCommand("leteE", "");
        try {
            mistypedAddInvalidParameters.suggestionFound();
        } catch (ParseException E) {
            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEventCommand.MESSAGE_USAGE),
                    E.getMessage());
        }
    }

    //Mistyped Delete command with invalid (empty) parameters. Negative test.
    @Test
    public void mistypedDeleteProfilePicTest() throws ParseException {
        UnknownCommand mistypedAddInvalidParameters = new UnknownCommand("deleteProfileP",
                "");
        try {
            mistypedAddInvalidParameters.suggestionFound();
        } catch (ParseException E) {
            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteProfilePicCommand.MESSAGE_USAGE),
                    E.getMessage());
        }
    }









// Mistyped Add command with invalid (empty) parameters. Negative test.
//    @Test
//    public void mistypedAddTest() throws ParseException {
//        UnknownCommand mistypedAddInvalidParameters = new UnknownCommand("ad", "");
//        try {
//            mistypedAddInvalidParameters.suggestionFound();
//        } catch (ParseException E) {
//            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE), E.getMessage());
//        }
//    }    //Mistyped Add command with invalid (empty) parameters. Negative test.
//    @Test
//    public void mistypedAddTest() throws ParseException {
//        UnknownCommand mistypedAddInvalidParameters = new UnknownCommand("ad", "");
//        try {
//            mistypedAddInvalidParameters.suggestionFound();
//        } catch (ParseException E) {
//            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE), E.getMessage());
//        }
//    }    //Mistyped Add command with invalid (empty) parameters. Negative test.
//    @Test
//    public void mistypedAddTest() throws ParseException {
//        UnknownCommand mistypedAddInvalidParameters = new UnknownCommand("ad", "");
//        try {
//            mistypedAddInvalidParameters.suggestionFound();
//        } catch (ParseException E) {
//            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE), E.getMessage());
//        }
//    }    //Mistyped Add command with invalid (empty) parameters. Negative test.
//    @Test
//    public void mistypedAddTest() throws ParseException {
//        UnknownCommand mistypedAddInvalidParameters = new UnknownCommand("ad", "");
//        try {
//            mistypedAddInvalidParameters.suggestionFound();
//        } catch (ParseException E) {
//            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE), E.getMessage());
//        }
//    }




}
