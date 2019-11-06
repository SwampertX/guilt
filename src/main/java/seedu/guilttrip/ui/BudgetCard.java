package seedu.guilttrip.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.guilttrip.model.entry.Budget;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class BudgetCard extends UiPart<Region> {

    private static final String FXML = "BudgetListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on GuiltTrip level 4</a>
     */

    public final Budget budget;

    @FXML
    private HBox cardPane;
    @FXML
    private Label desc;
    @FXML
    private Label id;
    @FXML
    private Label date;
    @FXML
    private Label left;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label total;
    @FXML
    private Label category;
    @FXML
    private FlowPane tags;

    public BudgetCard(Budget budget, int displayedIndex) {
        super(FXML);
        this.budget = budget;
        id.setText(displayedIndex + ". ");

        String fullDesc = budget.getDesc().fullDesc;
        desc.setText(fullDesc);
        date.setText(budget.getDate().toString() + " - " + budget.getEndDate().toString());
        double leftAmount = budget.getAmount().value - budget.getSpent().value;
        double totalAmount = budget.getAmount().value;
        //left.setText("left: $" + leftAmount + " out of: $" + budget.getAmount().value);
        left.setText("$" + leftAmount);
        total.setText("$" + totalAmount);

        progressBar.setProgress(leftAmount / totalAmount);

        category.setText(budget.getCategory().toString());

        budget.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BudgetCard)) {
            return false;
        }

        // state check
        BudgetCard card = (BudgetCard) other;
        return id.getText().equals(card.id.getText())
                && budget.equals(card.budget);
    }
}

