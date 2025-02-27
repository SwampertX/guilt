//package seedu.address.logic.commands;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
//import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
//import static seedu.address.testutil.TypicalEntries.CARL;
//import static seedu.address.testutil.TypicalEntries.ELLE;
//import static seedu.address.testutil.TypicalEntries.FIONA;
//import static seedu.address.testutil.TypicalEntries.getTypicalAddressBook;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//import org.junit.jupiter.api.Test;
//
//import seedu.address.model.Model;
//import seedu.address.model.ModelManager;
//import seedu.address.model.UserPrefs;
//import seedu.address.model.person.DescriptionContainsKeywordsPredicate;
//
///**
// * Contains integration tests (interaction with the Model) for {@code FindCommand}.
// */
//public class FindCommandTest {
//    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
//    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
//
//    @Test
//    public void equals() {
//        DescriptionContainsKeywordsPredicate firstPredicate =
//                new DescriptionContainsKeywordsPredicate(Collections.singletonList("first"));
//        DescriptionContainsKeywordsPredicate secondPredicate =
//                new DescriptionContainsKeywordsPredicate(Collections.singletonList("second"));
//
//        FindCommand findFirstCommand = new FindCommand(firstPredicate);
//        FindCommand findSecondCommand = new FindCommand(secondPredicate);
//
//        // same object -> returns true
//        assertTrue(findFirstCommand.equals(findFirstCommand));
//
//        // same values -> returns true
//        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
//        assertTrue(findFirstCommand.equals(findFirstCommandCopy));
//
//        // different types -> returns false
//        assertFalse(findFirstCommand.equals(1));
//
//        // null -> returns false
//        assertFalse(findFirstCommand.equals(null));
//
//        // different person -> returns false
//        assertFalse(findFirstCommand.equals(findSecondCommand));
//    }
//
//    @Test
//    public void execute_zeroKeywords_noPersonFound() {
//        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
//        DescriptionContainsKeywordsPredicate predicate = preparePredicate(" ");
//        FindCommand command = new FindCommand(predicate);
//        expectedModel.updateFilteredPersonList(predicate);
//        assertCommandSuccess(command, model, expectedMessage, expectedModel);
//        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
//    }
//
//    @Test
//    public void execute_multipleKeywords_multiplePersonsFound() {
//        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
//        DescriptionContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
//        FindCommand command = new FindCommand(predicate);
//        expectedModel.updateFilteredPersonList(predicate);
//        assertCommandSuccess(command, model, expectedMessage, expectedModel);
//        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
//    }
//
//    /**
//     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
//     */
//    private DescriptionContainsKeywordsPredicate preparePredicate(String userInput) {
//        return new DescriptionContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
//    }
//}
