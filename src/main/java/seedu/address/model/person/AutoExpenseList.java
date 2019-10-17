package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.DuplicateEntryException;
import seedu.address.model.person.exceptions.EntryNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not
 * allow nulls. A person is considered unique by comparing using
 * {@code Person#isSamePerson(Person)}. As such, adding and updating of persons
 * uses Person#isSamePerson(Person) for equality so as to ensure that the person
 * being added or updated is unique in terms of identity in the
 * UniquePersonList. However, the removal of a person uses Person#equals(Object)
 * so as to ensure that the person with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 */
public class AutoExpenseList implements Iterable<AutoExpense> {

    private final ObservableList<AutoExpense> internalList = FXCollections.observableArrayList();
    private final ObservableList<AutoExpense> internalUnmodifiableList = FXCollections
            .unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(AutoExpense toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameEntry);
    }

    /**
     * Adds a person to the list. The person must not already exist in the list.
     */
    public void add(AutoExpense toAdd) {
        requireNonNull(toAdd);

        internalList.add(toAdd);
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list. The person identity of
     * {@code editedPerson} must not be the same as another existing person in the
     * list.
     */
    public void setAutoExpense(AutoExpense target, AutoExpense editedAutoExpense) {
        requireAllNonNull(target, editedAutoExpense);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new EntryNotFoundException();
        }

        if (!target.equals(editedAutoExpense) && contains(editedAutoExpense)) {
            throw new DuplicateEntryException();
        }

        internalList.set(index, editedAutoExpense);
    }

    /**
     * Removes the equivalent person from the list. The person must exist in the
     * list.
     */
    public void remove(AutoExpense toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new EntryNotFoundException();
        }
    }

    public void setEntries(AutoExpenseList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code persons}. {@code persons} must
     * not contain duplicate persons.
     */
    public void setEntries(List<AutoExpense> entries) {
        requireAllNonNull(entries);

        internalList.setAll(entries);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<AutoExpense> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<AutoExpense> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AutoExpenseList // instanceof handles nulls
                        && internalList.equals(((AutoExpenseList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
