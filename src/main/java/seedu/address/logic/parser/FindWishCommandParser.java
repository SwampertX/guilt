package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindWishCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.WishDescriptionContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindWishCommandParser implements Parser<FindWishCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindWishCommand
     * and returns a FindWishCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindWishCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindWishCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindWishCommand(new WishDescriptionContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
