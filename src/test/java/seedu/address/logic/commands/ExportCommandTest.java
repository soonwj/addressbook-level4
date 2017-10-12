package seedu.address.logic.commands;

import org.junit.Test;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

public class ExportCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void exceptionWhenAddressBookIsEmpty() {
        assertCommandFailure(new ExportCommand(), new ModelManager(), ExportCommand.MESSAGE_EMPTY_ADDRESS_BOOK);
    }

    @Test
    public void exportSuccess() {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new ExportCommand(), model, ExportCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
