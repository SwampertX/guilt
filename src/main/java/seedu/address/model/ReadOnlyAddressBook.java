package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.person.AutoExpense;
import seedu.address.model.person.Entry;
import seedu.address.model.person.Expense;
import seedu.address.model.person.ExpenseReminder;
import seedu.address.model.person.ExpenseTracker;
import seedu.address.model.person.Income;
import seedu.address.model.person.Wish;
import seedu.address.model.person.WishReminder;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list. This list will not contain
     * any duplicate persons.
     */
    ObservableList<Entry> getEntryList();

    ObservableList<Expense> getExpenseList();

    ObservableList<Income> getIncomeList();

    ObservableList<Wish> getWishList();

    ObservableList<AutoExpense> getAutoExpenseList();

    ObservableList<ExpenseReminder> getExpenseReminderList();

    ObservableList<ExpenseTracker> getExpenseTrackerList();

    ObservableList<WishReminder> getWishReminderList();
}
