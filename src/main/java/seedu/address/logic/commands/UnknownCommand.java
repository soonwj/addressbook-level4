package seedu.address.logic.commands;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddCommandParser;
import seedu.address.logic.parser.AddEventCommandParser;
import seedu.address.logic.parser.DeleteCommandParser;
import seedu.address.logic.parser.DeleteEventCommandParser;
import seedu.address.logic.parser.DeleteProfilePicCommandParser;
import seedu.address.logic.parser.EditCommandParser;
import seedu.address.logic.parser.EditEventCommandParser;
import seedu.address.logic.parser.EmailCommandParser;
import seedu.address.logic.parser.FindCommandParser;
import seedu.address.logic.parser.LocationCommandParser;
import seedu.address.logic.parser.RemoveTagCommandParser;
import seedu.address.logic.parser.SelectCommandParser;
import seedu.address.logic.parser.UpdateProfilePicCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Created by Philemon1 on 29/10/2017.
 * This class is used to process all unknown commands, i.e user input that does not match any existing COMMAND_WORD
 * this is done by searching for COMMAND_WORDs with a levenshtein distance within the defined acceptable range,
 * in the constant ACCEPTABLE_LEVENSHTEIN_DISTANCE.
 *
 * The levenshtein distance calculator implementation is adapted entirely from:
 * https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
 * Proper credits have been given in the method:{@link #levenshteinDistance(CharSequence, CharSequence)}
 */
public class UnknownCommand extends Command {
    //These constants can be varied to allow wider similarity detection, which will come at the cost of performance
    private static final int ACCEPTABLE_LEVENSHTEIN_DISTANCE = 2;
    private static final int ACCETABLE_MAXIMUM_COMMAND_WORD_LENGTH = 18;

    //A constant String[] of all known COMMAND WORDS; this list should be extended when new Command types are created
    private static final String[] ALL_COMMAND_WORDS = {
        AddCommand.COMMAND_WORD, AddEventCommand.COMMAND_WORD, ClearCommand.COMMAND_WORD, DeleteCommand.COMMAND_WORD,
        DeleteEventCommand.COMMAND_WORD, DeleteProfilePicCommand.COMMAND_WORD, EditCommand.COMMAND_WORD,
        EditEventCommand.COMMAND_WORD, EmailCommand.COMMAND_WORD, ExitCommand.COMMAND_WORD, ExportCommand.COMMAND_WORD,
        FindCommand.COMMAND_WORD, HelpCommand.COMMAND_WORD, HistoryCommand.COMMAND_WORD, ImportCommand.COMMAND_WORD,
        ListCommand.COMMAND_WORD, LocationCommand.COMMAND_WORD, RedoCommand.COMMAND_WORD, RemoveTagCommand.COMMAND_WORD,
        SelectCommand.COMMAND_WORD, SortCommand.COMMAND_WORD, UndoCommand.COMMAND_WORD,
        UpdateProfilePicCommand.COMMAND_WORD
    };

    private String commandWord;
    private String arguments;
    private String promptToUser;
    private Command suggestedCommand;

    public UnknownCommand(String inputCommandWord, String inputArguments) {
        commandWord = inputCommandWord;
        arguments = inputArguments;
    }


    @Override
    public CommandResult execute() throws CommandException {
        return new CommandResult(promptToUser);
    }

    public Command getSuggestedCommand() {
        return suggestedCommand;
    }


    /**
     * This method initiates the checking of levenshtein distance, and updates the PROMPT_TO_USER accordingly.
     * This method will only check for user-entered commandWord < 18 in length (length of the longest command in DoC
     * : deleteProfilePic is 16 characters long)
     * In the case of same equal levenshtein distance, suggestedCommand() will be set to the first found
     * This method should always be executed before execute() method is called
     * @return true if minimum distance found is <= the ACCEPTABLE_LEVENSHTEIN_DISTANCE constant set, else false
     */
    public boolean suggestionFound() throws ParseException {
        int min = ACCEPTABLE_LEVENSHTEIN_DISTANCE + 1;
        int tempDistance;
        String closestCommandWord = null;

        //Checks for invalid commandWord length, i.e 18 and above (To prevent performance issues)
        if (invalidLengthDetected()) {
            return false;
        }

        /**
         * Iterates through all known COMMAND_WORDs, and find the smallest possible levenshtein distance,
         * in the case of equal levenshtein distance, the first encountered (alphabetical order), will be used
         */
        for (String s: ALL_COMMAND_WORDS) {
            tempDistance = levenshteinDistance(s, commandWord);
            if (tempDistance < min) {
                min = tempDistance;
                closestCommandWord = s;
            }
        }
        //An unchanged min indicates that no acceptable substitute has been found
        if (min == ACCEPTABLE_LEVENSHTEIN_DISTANCE + 1) {
            return false;
        } else {
            setSuggestedCommand(closestCommandWord);
            promptToUser = "Did you mean: " + closestCommandWord + arguments + " ?" + "\n" + "Respond: "
                    + "'yes' or 'y' to accept the suggested command." + "\n"
                    + "Suggested command will be discarded otherwise.";
            return true;
        }
    }

    /**
     * This method checks for an invalid commandWord length
     * @return true if commandWord.length is more than 17
     * @throws ParseException if user input command length is 0
     */
    private boolean invalidLengthDetected () {
        if (commandWord.length() > ACCETABLE_MAXIMUM_COMMAND_WORD_LENGTH) {
            return true;
        }
        if (commandWord.length() == 0) {
            assert true : "Unexpected behaviour: Empty input has gone through the AddressBookParser parseCommand()"
                    + "matcher check";
        }
        return false;
    }

    private void setSuggestedCommand(String closestCommandWord) throws ParseException {
        switch (closestCommandWord) {
        case AddCommand.COMMAND_WORD:
            suggestedCommand = new AddCommandParser().parse(arguments);
            break;

        case AddEventCommand.COMMAND_WORD:
            suggestedCommand = new AddEventCommandParser().parse(arguments);
            break;

        case EditCommand.COMMAND_WORD:
            suggestedCommand = new EditCommandParser().parse(arguments);
            break;

        case EditEventCommand.COMMAND_WORD:
            suggestedCommand = new EditEventCommandParser().parse(arguments);
            break;

        case UpdateProfilePicCommand.COMMAND_WORD:
            suggestedCommand = new UpdateProfilePicCommandParser().parse(arguments);
            break;

        case DeleteProfilePicCommand.COMMAND_WORD:
            suggestedCommand = new DeleteProfilePicCommandParser().parse(arguments);
            break;

        case SelectCommand.COMMAND_WORD:
            suggestedCommand = new SelectCommandParser().parse(arguments);
            break;

        case DeleteCommand.COMMAND_WORD:
            suggestedCommand = new DeleteCommandParser().parse(arguments);
            break;

        case DeleteEventCommand.COMMAND_WORD:
            suggestedCommand = new DeleteEventCommandParser().parse(arguments);
            break;

        case ClearCommand.COMMAND_WORD:
            suggestedCommand = new ClearCommand();
            break;

        case FindCommand.COMMAND_WORD:
            suggestedCommand = new FindCommandParser().parse(arguments);
            break;

        case LocationCommand.COMMAND_WORD:
            suggestedCommand = new LocationCommandParser().parse(arguments);
            break;

        case ListCommand.COMMAND_WORD:
            suggestedCommand = new ListCommand();
            break;

        case HistoryCommand.COMMAND_WORD:
            suggestedCommand = new HistoryCommand();
            break;

        case ExitCommand.COMMAND_WORD:
            suggestedCommand = new ExitCommand();
            break;

        case HelpCommand.COMMAND_WORD:
            suggestedCommand = new HelpCommand();
            break;

        case UndoCommand.COMMAND_WORD:
            suggestedCommand = new UndoCommand();
            break;

        case RedoCommand.COMMAND_WORD:
            suggestedCommand = new RedoCommand();
            break;

        case RemoveTagCommand.COMMAND_WORD:
            suggestedCommand = new RemoveTagCommandParser().parse(arguments);
            break;

        case ImportCommand.COMMAND_WORD:
            suggestedCommand = new ImportCommand();
            break;

        case ExportCommand.COMMAND_WORD:
            suggestedCommand = new ExportCommand();
            break;

        case EmailCommand.COMMAND_WORD:
            suggestedCommand = new EmailCommandParser().parse(arguments);
            break;

        case SortCommand.COMMAND_WORD:
            suggestedCommand = new SortCommand();
            break;
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }


    /**
     * Levenshtein distance algorithm implementation taken from:
     * https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java,
     * under the Creative Commons Attribution-ShareAlike License.
     * No logical changes were made. Minor checkstyle formatting changes applied.
     * @param lhs
     * @param rhs
     * @return
     */
    public int levenshteinDistance (CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) {
            cost[i] = i;
        }

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int costReplace = cost[i - 1] + match;
                int costInsert  = cost[i] + 1;
                int costDelete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(costInsert, costDelete), costReplace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }
}
