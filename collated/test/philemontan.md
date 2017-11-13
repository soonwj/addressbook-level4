# philemontan
###### \java\seedu\address\commons\util\GooglePersonConverterUtilTest.java
``` java
/**
 * Unit testing for the GooglePersonConverterUtil class
 * Both classes and their composed classes in each package have the same name;
 * Handling: seedu.address.model.person.Person will be imported, while Google classes have to be fully qualified.
 */
public class GooglePersonConverterUtilTest {
    /**
     * (Single) Google Person -> DoC Person Conversion Tests (IMPORT)
     */

    //Negative Testing: Required DoC Person parameters set to null
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void convertGooglePersonWithNullNameException() throws InvalidGooglePersonException {
        com.google.api.services.people.v1.model.Person testPerson =
                getGooglePerson(null, "12345678", "blk001 Test Ave",
                "test@gmail.com");
        thrown.expect(InvalidGooglePersonException.class);
        GooglePersonConverterUtil.singleGoogleToDocPersonConversion(testPerson);
    }

    @Test
    public void convertGooglePersonWithNullPhoneException() throws InvalidGooglePersonException {
        com.google.api.services.people.v1.model.Person testPerson =
                getGooglePerson("Testee", null, "blk001 Test Ave", "test@gmail.com");
        thrown.expect(InvalidGooglePersonException.class);
        GooglePersonConverterUtil.singleGoogleToDocPersonConversion(testPerson);
    }

    @Test
    public void convertGooglePersonWithNullEmail() throws InvalidGooglePersonException {
        com.google.api.services.people.v1.model.Person testPerson =
                getGooglePerson("Testee", "12345678", "blk001 Test Ave", null);
        seedu.address.model.person.Person convertedPerson =
                GooglePersonConverterUtil.singleGoogleToDocPersonConversion(testPerson);
        assertEquals(convertedPerson.getEmail().toString(), GooglePersonConverterUtil.DEFAULT_EMAIL);
    }

    @Test
    public void convertGooglePersonWithNullAddress() throws InvalidGooglePersonException {
        com.google.api.services.people.v1.model.Person testPerson =
                getGooglePerson("Testee", "12345678", null, "test@gmail.com");
        seedu.address.model.person.Person convertedPerson =
                GooglePersonConverterUtil.singleGoogleToDocPersonConversion(testPerson);
        assertEquals(convertedPerson.getAddress().toString(), GooglePersonConverterUtil.DEFAULT_ADDRESS);
    }

    /**
     * (Single) DoC Person -> Google Person Conversion Tests
     */

    //Positive Testing: Test for successful conversion
    @Test
    public void convertGenericDocPerson() {
        Person docPerson = new PersonBuilder().withName("John Doe").withAddress("Elm Street")
                .withEmail("jon@gmail.com").withPhone("01234567").withTags("friends").build();
        com.google.api.services.people.v1.model.Person expectedGooglePerson =
                getGooglePerson("John Doe", "01234567", "Elm Street", "jon@gmail.com");

        com.google.api.services.people.v1.model.Person convertedGooglePerson =
                GooglePersonConverterUtil.singleDocToGooglePersonConversion(docPerson);
        assertEquals(expectedGooglePerson, convertedGooglePerson);
    }

    /**
     *  (List) DoC Person -> Google Person Conversion Tests
     */
    @Test
    public void convertListGenericDoCPerson() {
        ReadOnlyPerson docPerson = new PersonBuilder().withName("John Doe").withAddress("Elm Street")
                .withEmail("jon@gmail.com").withPhone("01234567").withTags("friends").build();
        com.google.api.services.people.v1.model.Person expectedGooglePerson =
                getGooglePerson("John Doe", "01234567", "Elm Street", "jon@gmail.com");

        List<com.google.api.services.people.v1.model.Person> expectedGooglePersonList = new ArrayList<>();
        expectedGooglePersonList.add(expectedGooglePerson);

        List<ReadOnlyPerson> docPersons = new ArrayList<>();
        docPersons.add(docPerson);

        List<com.google.api.services.people.v1.model.Person> actualGooglePersonList;
        actualGooglePersonList = GooglePersonConverterUtil.listDocToGooglePersonConversion(docPersons);

        assertEquals(expectedGooglePersonList, actualGooglePersonList);
    }

    /**
     * (List) Google Person -> DoC Person Conversion Tests
     */
    @Test
    public void convertListGooglePerson() {
        //test google person
        com.google.api.services.people.v1.model.Person testGooglePerson =
                getGooglePerson("John Doe", "01234567", "Elm Street", "jon@gmail.com");
        //test doc person
        Person testDocPerson = new PersonBuilder().withName("John Doe").withAddress("Elm Street")
                .withEmail("jon@gmail.com").withPhone("01234567").withTags("friends").build();

        //test google person list
        ArrayList<com.google.api.services.people.v1.model.Person> testGooglePersonList = new ArrayList<>();
        testGooglePersonList.add(testGooglePerson);

        //expected doc person list
        List<Person> expectedDocPersonList = new ArrayList<>();
        expectedDocPersonList.add(testDocPerson);

        //actual doc person list
        List<Person> actualDocPersonList = GooglePersonConverterUtil
                .listGoogleToDoCPersonConversion(testGooglePersonList);

        assertEquals(expectedDocPersonList, actualDocPersonList);
    }

    /**
     * Person Number and Name processing test to fit DoC regex
     */
    @Test
    public void testNameProcessingRegexFitting() {
        String invalidNameString = "?394johnD_oE*&^%#%^*()]";
        String expectedProcessedName = " 394johnD oE           ";

        try {
            assertEquals(expectedProcessedName, GooglePersonConverterUtil.processName(invalidNameString));
        } catch (InvalidGooglePersonException E) {
            assert true : "Unexpected behaviour from GooglePersonConverterUtil";
        }
    }

    @Test
    public void testNumberProcessingRegexFitting() {
        String invalidNumberString = "23k4n&((#???!sfsd+++6590000000";
        String expectedNumberString = "23490000000";

        try {
            assertEquals(expectedNumberString, GooglePersonConverterUtil.processNumber(invalidNumberString));
        } catch (InvalidGooglePersonException E) {
            assert true : "Unexpected behaviour from GooglePersonConverterUtil";
        }
    }

    /**
     * Creates a GooglePerson from input parameters, to support testing
     * Made with a default UserDefined for
     * @param name
     * @param number
     * @param address
     * @param email
     * @return the desired Google Person
     */
    public com.google.api.services.people.v1.model.Person getGooglePerson(String name, String number, String address,
                                                                          String email) {
        ArrayList<com.google.api.services.people.v1.model.Name> names = new ArrayList<>();
        ArrayList<com.google.api.services.people.v1.model.PhoneNumber> phones = new ArrayList<>();
        ArrayList<com.google.api.services.people.v1.model.Address> addresses = new ArrayList<>();
        ArrayList<com.google.api.services.people.v1.model.EmailAddress> emails = new ArrayList<>();
        com.google.api.services.people.v1.model.Person personToReturn =
                new com.google.api.services.people.v1.model.Person();

        ArrayList<com.google.api.services.people.v1.model.UserDefined> tags = new ArrayList<>();

        tags.add(new com.google.api.services.people.v1.model.UserDefined().setKey("tag").setValue("friends"));
        names.add(new com.google.api.services.people.v1.model.Name().setGivenName(name).setDisplayName(name));
        phones.add(new com.google.api.services.people.v1.model.PhoneNumber().setValue(number));
        addresses.add(new com.google.api.services.people.v1.model.Address().setFormattedValue(address));
        emails.add(new com.google.api.services.people.v1.model.EmailAddress().setValue(email));

        personToReturn.setNames(names);
        personToReturn.setAddresses(addresses);
        personToReturn.setEmailAddresses(emails);
        personToReturn.setPhoneNumbers(phones);
        personToReturn.setUserDefined(tags);
        return personToReturn;
    }
}
```
###### \java\seedu\address\logic\commands\UnknownCommandTest.java
``` java
/**
 * Unit testing for {@link seedu.address.logic.commands.UnknownCommand}
 * Integration tests with model not required, as interactions with model happen only on the actual matched Command
 * subclasses
 * Tests are written to check for positive matching by UnknownCommand. To reduce redundancy,
 * test cases use inputs that are at the set maximum acceptable levenshtein distance:
 * @see seedu.address.logic.commands.UnknownCommand#ACCEPTABLE_LEVENSHTEIN_DISTANCE
 */
public class UnknownCommandTest {
    /**
     * This test case tests parsing of all commands that require input parameters.
     * We can test 2 separate functionality simultaneously to maximize efficiency.
     * 1: Correct matching to existing command if Levenshtein distance < current acceptable maximum:
     * @see seedu.address.logic.commands.UnknownCommand#ACCEPTABLE_LEVENSHTEIN_DISTANCE
     * 2: Parse exception thrown on invalid parameter input to command parsers.
     * We do this by instantiating the UnknownCommand with commandWords that are close enough to match known
     * commandWords, but we enter invalid arguments.
     */
    @Test
    public void acceptableDistanceMistypedInputWithInvalidParameters() {
        UnknownCommand tempCommand;

        //AddCommand test
        tempCommand = new UnknownCommand("a", "");
        executeTest(tempCommand, AddCommand.MESSAGE_USAGE);

        //AddECommand test
        tempCommand = new UnknownCommand("addEve", "");
        executeTest(tempCommand, AddEventCommand.MESSAGE_USAGE);

        //DeleteCommand test
        tempCommand  = new UnknownCommand("dele", "");
        executeTest(tempCommand, DeleteCommand.MESSAGE_USAGE);

        //DeleteEventCommand test
        tempCommand = new UnknownCommand("leteE", "");
        executeTest(tempCommand, DeleteEventCommand.MESSAGE_USAGE);

        //DeleteProfilePicCommand test
        tempCommand  = new UnknownCommand("deleteProfileP", "");
        executeTest(tempCommand, DeleteProfilePicCommand.MESSAGE_USAGE);

        //EditCommand test
        tempCommand = new UnknownCommand("edittt", "");
        executeTest(tempCommand, EditCommand.MESSAGE_USAGE);

        //EditEventCommand test
        tempCommand = new UnknownCommand("itE", "");
        executeTest(tempCommand, EditEventCommand.MESSAGE_USAGE);

        //EmailCommand test
        tempCommand = new UnknownCommand("ema", "");
        executeTest(tempCommand, EmailCommand.MESSAGE_USAGE);

        //FindCommand test
        tempCommand = new UnknownCommand("fi", "");
        executeTest(tempCommand, FindCommand.MESSAGE_USAGE);

        //LocationCommand test
        tempCommand = new UnknownCommand("locati", "");
        executeTest(tempCommand, LocationCommand.MESSAGE_USAGE);

        //RemoveTagCommand test
        tempCommand = new UnknownCommand("removet", "");
        executeTest(tempCommand, RemoveTagCommand.MESSAGE_USAGE);

        //SelectCommand test
        tempCommand = new UnknownCommand("sele", "");
        executeTest(tempCommand, SelectCommand.MESSAGE_USAGE);

        //UpdateProfilePicCommand test
        tempCommand = new UnknownCommand("updateProfileP", "");
        executeTest(tempCommand, UpdateProfilePicCommand.MESSAGE_USAGE);
    }

    /**
     * Executes the actual testing for {@link #acceptableDistanceMistypedInputWithInvalidParameters()}
     * @param tempCommand
     * @param expectedCommandMessageUsage
     */
    public void executeTest(UnknownCommand tempCommand, String expectedCommandMessageUsage) {
        try {
            tempCommand.suggestionFound();
        } catch (ParseException E) {
            assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, expectedCommandMessageUsage), E.getMessage());
        }
    }

    /**
     * Similar to {@link #acceptableDistanceMistypedInputWithInvalidParameters}, this test case is written for
     * Commands that do not require parameters.
     * In this test case, we only test for positive matching of mistyped inputs.
     */
    @Test
    public void acceptableDistanceMistypedInputWithNoRequiredParameters() {
        UnknownCommand tempCommand = null;
        String commandBaseClassPath = "seedu.address.logic.commands.";

        //ClearCommand test
        tempCommand = new UnknownCommand("cle", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "ClearCommand");

        //ExitCommand test
        tempCommand = new UnknownCommand("ex", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "ExitCommand");

        //Export test
        tempCommand = new UnknownCommand("exporttt", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "ExportCommand");

        //Help test
        tempCommand = new UnknownCommand("he", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "HelpCommand");

        //History test
        tempCommand = new UnknownCommand("histo", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "HistoryCommand");

        //Import test
        tempCommand = new UnknownCommand("impo", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "ImportCommand");

        //List test
        tempCommand = new UnknownCommand("li", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "ListCommand");

        //Redo test
        tempCommand = new UnknownCommand("re", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "RedoCommand");

        //Sort test
        tempCommand = new UnknownCommand("so", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "SortCommand");

        //Undo test
        tempCommand = new UnknownCommand("un", "");
        executeTestCommandWithoutParameterRequirement(tempCommand, commandBaseClassPath + "UndoCommand");
    }

    /**
     * Executes the actual testing for {@link #acceptableDistanceMistypedInputWithNoRequiredParameters}
     * @param tempCommand
     * @param fullyQualifiedActualClassPath
     */
    public void executeTestCommandWithoutParameterRequirement(UnknownCommand tempCommand,
                                                              String fullyQualifiedActualClassPath) {
        Class actualClass = null;
        try {
            tempCommand.suggestionFound();
        } catch (ParseException E1) {
            assert false : "Unexpected behaviour: Clear should not throw a parse exception with empty parameters";
        }
        try {
            actualClass = Class.forName(fullyQualifiedActualClassPath);
        } catch (ClassNotFoundException E2) {
            assert false : "Unexpected behaviour: ClearCommand class cannot be found";
        }
        assertTrue(tempCommand.getSuggestedCommand().getClass().getSimpleName()
                .equals(actualClass.getSimpleName()));
    }
}
```
