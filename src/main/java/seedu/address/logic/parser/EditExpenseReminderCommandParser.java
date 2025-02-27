package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditExpenseReminderCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditExpenseCommand object
 */
public class EditExpenseReminderCommandParser implements Parser<EditExpenseReminderCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditExpenseCommand
     * and returns an EditExpenseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditExpenseReminderCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DESC, PREFIX_AMOUNT, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format
                    (MESSAGE_INVALID_COMMAND_FORMAT, EditExpenseReminderCommand.MESSAGE_USAGE), pe);
        }

        EditExpenseReminderCommand.EditReminderDescriptor editReminderDescriptor =
                new EditExpenseReminderCommand.EditReminderDescriptor();
        if (argMultimap.getValue(PREFIX_DESC).isPresent()) {
            editReminderDescriptor.setDesc(
                    ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESC).get()).toString());
        }

        if (argMultimap.getValue(PREFIX_AMOUNT).isPresent()) {
            editReminderDescriptor.setAmount(
                    (long) ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT).get()).value);
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editReminderDescriptor::setTags);

        if (!editReminderDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditExpenseReminderCommand.MESSAGE_NOT_EDITED);
        }

        return new EditExpenseReminderCommand(index, editReminderDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
