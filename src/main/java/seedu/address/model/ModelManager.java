package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.AutoExpense;
import seedu.address.model.person.Entry;
import seedu.address.model.person.Expense;
import seedu.address.model.person.ExpenseReminder;
import seedu.address.model.person.ExpenseTrackerManager;
import seedu.address.model.person.Income;
import seedu.address.model.person.SortSequence;
import seedu.address.model.person.SortType;
import seedu.address.model.person.Wish;
import seedu.address.model.person.WishReminder;
import seedu.address.model.util.EntryComparator;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private final SortType sortByDescription = new SortType("description");
    private final SortSequence sortByAsc = new SortSequence("ascending");
    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Entry> filteredEntries;
    private final FilteredList<Expense> filteredExpenses;
    private final FilteredList<Income> filteredIncomes;
    private final FilteredList<Wish> filteredWishes;
    private final FilteredList<AutoExpense> filteredAutoExpenses;
    private final SortedList<Entry> sortedEntryList;
    private final FilteredList<ExpenseReminder> filteredExpenseReminders;
    private final ExpenseTrackerManager expenseTrackers;
    private final FilteredList<WishReminder> filteredWishReminders;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredExpenses = new FilteredList<>(this.addressBook.getExpenseList());
        filteredIncomes = new FilteredList<>(this.addressBook.getIncomeList());
        filteredWishes = new FilteredList<>(this.addressBook.getWishList());
        filteredAutoExpenses = new FilteredList<>(this.addressBook.getAutoExpenseList());
        sortedEntryList = new SortedList<>(this.addressBook.getEntryList());
        sortedEntryList.setComparator(new EntryComparator(sortByDescription, sortByAsc));
        filteredEntries = new FilteredList<>(sortedEntryList);
        filteredExpenseReminders = new FilteredList<>(this.addressBook.getExpenseReminderList());
        filteredWishReminders = new FilteredList<>(this.addressBook.getWishReminderList());
        expenseTrackers = new ExpenseTrackerManager(this.addressBook.getExpenseTrackerList());
        expenseTrackers.track(filteredExpenses);
        this.addressBook.updateExpenseReminders();
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    // =========== UserPrefs
    // ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    // =========== AddressBook
    // ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasEntry(Entry entry) {
        requireNonNull(entry);
        return addressBook.hasEntry(entry);
    }

    @Override
    public boolean hasExpenseReminder(ExpenseReminder reminder) {
        requireNonNull(reminder);
        return addressBook.hasExpenseReminder(reminder);
    }

    @Override
    public void deleteEntry(Entry target) {
        addressBook.removeEntry(target);
        if (target instanceof Expense) {
            addressBook.removeExpense((Expense) target);
            expenseTrackers.track(filteredExpenses);
            addressBook.updateExpenseReminders();
        } else if (target instanceof Income) {
            addressBook.removeIncome((Income) target);
        } else if (target instanceof Wish) {
            addressBook.removeWish((Wish) target);
        }
    }

    @Override
    public void deleteExpense(Expense target) {
        addressBook.removeEntry(target);
        addressBook.removeExpense(target);
        expenseTrackers.track(filteredExpenses);
        addressBook.updateExpenseReminders();
    }

    @Override
    public void deleteIncome(Income target) {
        addressBook.removeEntry(target);
        addressBook.removeIncome(target);
    }

    @Override
    public void deleteWish(Wish target) {
        addressBook.removeEntry(target);
        addressBook.removeWish(target);
    }

    @Override
    public void deleteAutoExpense(AutoExpense target) {
        addressBook.removeEntry(target);
        addressBook.removeAutoExpense(target);
    }

    public void deleteExpenseReminder(ExpenseReminder target) {
        addressBook.removeExpenseReminder(target);
    }

    @Override
    public void addEntry(Entry entry) {
        addressBook.addEntry(entry);
        if (entry instanceof Expense) {
            addressBook.addExpense((Expense) entry);
            expenseTrackers.track(filteredExpenses);
            addressBook.updateExpenseReminders();
        } else if (entry instanceof Income) {
            addressBook.addIncome((Income) entry);
        } else if (entry instanceof Wish) {
            addressBook.addWish((Wish) entry);
        }
        sortFilteredEntry(sortByDescription, sortByAsc);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void addExpense(Expense expense) {
        addressBook.addExpense(expense);
        sortFilteredEntry(sortByDescription, sortByAsc);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        expenseTrackers.track(filteredExpenses);
        addressBook.updateExpenseReminders();
    }

    @Override
    public void addIncome(Income income) {
        addressBook.addIncome(income);
        sortFilteredEntry(sortByDescription, sortByAsc);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void addWish(Wish wish) {
        addressBook.addWish(wish);
        sortFilteredEntry(sortByDescription, sortByAsc);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void addAutoExpense(AutoExpense autoExpense) {
        addressBook.addAutoExpense(autoExpense);
        updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
    }

    @Override
    public void addExpenseReminder(ExpenseReminder expenseReminder) {
        addressBook.addExpenseReminder(expenseReminder);
        expenseTrackers.track(filteredExpenses);
        addressBook.updateExpenseReminders();
    }

    @Override
    public void setEntry(Entry target, Entry editedEntry) {
        requireAllNonNull(target, editedEntry);
        addressBook.setEntry(target, editedEntry);
        if (target instanceof Expense) {
            addressBook.setExpense((Expense) target, (Expense) editedEntry);
            expenseTrackers.track(filteredExpenses);
            addressBook.updateExpenseReminders();
        } else if (target instanceof Income) {
            addressBook.setIncome((Income) target, (Income) editedEntry);
        } else if (target instanceof Wish) {
            addressBook.setWish((Wish) target, (Wish) editedEntry);
        }
    }

    @Override
    public void setExpenseReminder(ExpenseReminder target, ExpenseReminder editedEntry) {
        requireAllNonNull(target, editedEntry);
        addressBook.setExpenseReminder(target, editedEntry);
        expenseTrackers.track(filteredExpenses);
        addressBook.updateExpenseReminders();
    }


    // =========== Filtered Person List Accessors

    /**
     * Returns an unmodifiable view of the list of {@code Entry} backed by the
     * internal list of {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Entry> getFilteredEntryList() {
        return filteredEntries;
    }

    @Override
    public ObservableList<Expense> getFilteredExpenses() {
        return filteredExpenses;
    }

    @Override
    public ObservableList<Income> getFilteredIncomes() {
        return filteredIncomes;
    }

    @Override
    public ObservableList<Wish> getFilteredWishes() {
        return filteredWishes;
    }

    @Override
    public ObservableList<AutoExpense> getFilteredAutoExpenses() {
        return filteredAutoExpenses;
    }

    public ObservableList<ExpenseReminder> getFilteredExpenseReminders() {
        return filteredExpenseReminders;
    }


    public ObservableList<WishReminder> getFiltereWishReminders() {
        return filteredWishReminders;
    }

    /**
     * return List of Entries matching condition.
     * @param predicate predicate to filter
     */
    public void updateFilteredEntryList(Predicate<Entry> predicate) {
        requireNonNull(predicate);
        filteredEntries.setPredicate(predicate);
    }

    @Override
    public void sortFilteredEntry(SortType c, SortSequence sequence) {
        sortedEntryList.setComparator(new EntryComparator(c, sequence));
    }

    @Override
    public void updateFilteredExpenses(Predicate<Expense> predicate) {
        requireNonNull(predicate);
        filteredExpenses.setPredicate(predicate);
    }

    @Override
    public void updateFilteredIncomes(Predicate<Income> predicate) {
        requireNonNull(predicate);
        filteredIncomes.setPredicate(predicate);
    }

    @Override
    public void updateFilteredWishes(Predicate<Wish> predicate) {
        requireNonNull(predicate);
        filteredWishes.setPredicate(predicate);
    }

    @Override
    public void updateFilteredAutoExpenses(Predicate<AutoExpense> predicate) {
        requireNonNull(predicate);
        filteredAutoExpenses.setPredicate(predicate);
    }

    /**
     * return list of reminders matching this condition.
     * @param predicate condition to be matched.
     */
    public void updateFilteredExpenseReminders(Predicate<ExpenseReminder> predicate) {
        requireNonNull(predicate);
        filteredExpenseReminders.setPredicate(predicate);
    }

    @Override
    public void updateFilteredWishReminders(Predicate<WishReminder> predicate) {
        requireNonNull(predicate);
        filteredWishReminders.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook) && userPrefs.equals(other.userPrefs)
                && filteredEntries.equals(other.filteredEntries);
    }

}
