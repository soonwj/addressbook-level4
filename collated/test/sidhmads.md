# sidhmads
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
        @Override
        public void removeTag(ObservableList<ReadOnlyPerson> persons, Set<Tag> tag)
                throws PersonNotFoundException, DuplicatePersonException, CommandException {
            fail("This method should not be called");
        }
```
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
        @Override
        public void findLocation(ReadOnlyPerson person) {
            fail("This method should not be called.");
        }
```
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
        @Override
        public void sortPersons() {
            fail("This method should not be called.");
        }
```
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
        @Override
        public String updateEmailRecipient(Predicate<ReadOnlyPerson> predicate) {
            fail("This method should not be called.");
            return null;
        }
```
###### \java\seedu\address\logic\commands\AddEventCommandTest.java
``` java
        @Override
        public void removeTag(ObservableList<ReadOnlyPerson> persons, Set<Tag> tag)
                throws PersonNotFoundException, DuplicatePersonException, CommandException {
            fail("This method should not be called.");
        }
```
###### \java\seedu\address\logic\commands\AddEventCommandTest.java
``` java
        @Override
        public String updateEmailRecipient(Predicate<ReadOnlyPerson> predicate) {
            fail("This method should not be called.");
            return null;
        }
```
###### \java\seedu\address\logic\commands\AddEventCommandTest.java
``` java
        @Override
        public void findLocation(ReadOnlyPerson person) {
            fail("This method should not be called.");
        }
```
###### \java\seedu\address\logic\commands\AddEventCommandTest.java
``` java
        @Override
        public void sortPersons() {
            fail("This method should not be called.");
        }
```
###### \java\seedu\address\logic\commands\EmailCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code EmailCommand}.
 */
public class EmailCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        EmailCommand findFirstCommand = new EmailCommand(firstPredicate, "", "");
        EmailCommand findSecondCommand = new EmailCommand(secondPredicate, "", "");

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        EmailCommand findFirstCommandCopy = new EmailCommand(firstPredicate, "", "");
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_NOT_SENT);
        EmailCommand command = prepareCommand(" ");
        assertCommandSuccess(command, model, expectedMessage, model);
    }
    /**
     * The execution of EmailCommand makes the Travis CI fail.
     * Travis CI is unable to open the desktop mail application.
     * When running locally, use the below test. Comment it otherwise.
     */
    //    @Test
    //    public void execute_multipleKeywords_multiplePersonsFound() {
    //        String expectedMessage = String.format(MESSAGE_EMAIL_APP);
    //        EmailCommand command = prepareCommand("email to/Kurz Elle Kunz");
    //        assertCommandSuccess(command, model, expectedMessage, model);
    //    }

    /**
     * Parses {@code userInput} into a {@code EmailCommand}.
     */
    private EmailCommand prepareCommand(String userInput) {
        EmailCommand command =
                new EmailCommand(new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))), "", "");
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\LocationCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code LocationCommand}.
 */
public class LocationCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyPerson personToFind = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocationCommand locationCommand = prepareCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(LocationCommand.MESSAGE_FIND_LOCATION_SUCCESS,
                personToFind.getName().fullName, personToFind.getAddress());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.findLocation(personToFind);

        assertCommandSuccess(locationCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LocationCommand locationCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(locationCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showFirstPersonOnly(model);

        ReadOnlyPerson personToFind = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocationCommand locationCommand = prepareCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(LocationCommand.MESSAGE_FIND_LOCATION_SUCCESS,
                personToFind.getName().fullName, personToFind.getAddress());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showFirstPersonOnly(expectedModel);
        expectedModel.findLocation(personToFind);

        assertCommandSuccess(locationCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showFirstPersonOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        LocationCommand locationCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(locationCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        LocationCommand findFirstCommand = new LocationCommand(INDEX_FIRST_PERSON);
        LocationCommand findSecondCommand = new LocationCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        LocationCommand findFirstCommandCopy = new LocationCommand(INDEX_FIRST_PERSON);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    /**
     * Returns a {@code LocationCommand} with the parameter {@code index}.
     */
    private LocationCommand prepareCommand(Index index) {
        LocationCommand locationCommand = new LocationCommand(index);
        locationCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return locationCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assert model.getFilteredPersonList().isEmpty();
    }
}
```
###### \java\seedu\address\logic\commands\RemoveTagCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code RemoveTagCommand}.
 */
public class RemoveTagCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() throws IllegalValueException {
        Set<Tag> tagFriend = new HashSet<>();
        tagFriend.add(new Tag(VALID_TAG_FRIEND));
        RemoveTagCommand findFirstCommand = new RemoveTagCommand(new ArrayList<Index>(), tagFriend);
        Set<Tag> tagHusband = new HashSet<>();
        tagHusband.add(new Tag(VALID_TAG_HUSBAND));
        RemoveTagCommand findSecondCommand = new RemoveTagCommand(new ArrayList<Index>(), tagHusband);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        RemoveTagCommand findFirstCommandCopy = new RemoveTagCommand(new ArrayList<Index>(), tagFriend);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords() throws Exception {
        String expectedMessage = String.format("The Tag is invalid!");
        RemoveTagCommand command = prepareCommand(new ArrayList<Index>(), new HashSet<Tag>());
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() throws Exception {
        ObservableList<ReadOnlyPerson> personToRemoveTag = FXCollections.observableArrayList();
        personToRemoveTag.add(getTypicalAddressBook().getPersonList().get(0));
        personToRemoveTag.add(getTypicalAddressBook().getPersonList().get(1));
        ArrayList<Index> indexes = new ArrayList<>();
        indexes.add(ParserUtil.parseIndex("1"));
        indexes.add(ParserUtil.parseIndex("2"));
        Set<Tag> tagFriend = new HashSet<>();
        tagFriend.add(new Tag("friends"));
        String expectedMessage = String.format(MESSAGE_DELETE_TAG_SUCCESS,
                tagFriend.toString()) + String.format("from %s", personToRemoveTag.toString());
        RemoveTagCommand command = prepareCommand(indexes, tagFriend);
        Model mod = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        mod.removeTag(personToRemoveTag, tagFriend);
        assertCommandSuccess(command, model, expectedMessage, mod);
    }

    /**
     * Parses {@code userInput} into a {@code RemoveTagCommand}.
     */
    private RemoveTagCommand prepareCommand(ArrayList<Index> indexes, Set<Tag> tags) {
        RemoveTagCommand command =
                new RemoveTagCommand(indexes, tags);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
```
###### \java\seedu\address\logic\commands\SortCommandTest.java
``` java
public class SortCommandTest {

    @Test
    public void executeSortOnEmptyAddressBookSuccess() {
        Model model = new ModelManager();
        assertCommandSuccess(prepareCommand(model), model, SortCommand.COMMAND_SUCCESS, model);
    }

    @Test
    public void executeSortOnNonEmptyAddressBookSuccess() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertCommandSuccess(prepareCommand(model), model, SortCommand.COMMAND_SUCCESS, model);
    }

    /**
     * Generates a new {@code SortCommand} which upon execution, sorts the contents in {@code model}.
     */
    private SortCommand prepareCommand(Model model) {
        SortCommand command = new SortCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\parser\EmailCommandParserTest.java
``` java
/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the EmailCommand code. For example, inputs "to/adam" and "to/adam sam subject/hi body/cool" take the
 * same path through the EmailCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class EmailCommandParserTest {

    private EmailCommandParser parser = new EmailCommandParser();

    @Test
    public void parse_validArgs_returnsEmailCommand() {
        String[] val = new String[2];
        val[0] = "Adam";
        val[1] = "Sam";
        assertParseSuccess(parser, "email to/Adam Sam subject/hi body/cool",
                new EmailCommand((new NameContainsKeywordsPredicate(Arrays.asList(val))), "hi", "cool"));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
    }
}

```
###### \java\seedu\address\logic\parser\LocationCommandParserTest.java
``` java
/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the LocationCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the LocationCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class LocationCommandParserTest {

    private LocationCommandParser parser = new LocationCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new LocationCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, LocationCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\logic\parser\RemoveTagCommandParserTest.java
``` java
/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the RemoveTagCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the RemoveTagCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class RemoveTagCommandParserTest {

    private RemoveTagCommandParser parser = new RemoveTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsRemoveTagCommand() {
        try {
            Set<Tag> tag = new HashSet<>();
            tag.add(new Tag("friend"));
            RemoveTagCommand expectedRemoveTagCommand =
                    new RemoveTagCommand(new ArrayList<Index>(), tag);
            assertParseSuccess(parser, "removeTag t/friend", expectedRemoveTagCommand);
        } catch (IllegalValueException e) {
            throw new AssertionError("Default tag's value is invalid.");
        }
    }
}
```
