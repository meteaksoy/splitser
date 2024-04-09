package client.scenes;

import client.services.AddOrEditExpenseService;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.ExpenseTag;
import commons.User;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import javax.inject.Inject;

import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddOrEditExpenseCtrl implements Initializable {
    private final AddOrEditExpenseService service;
    private Event event;
    private Expense expense;

    @FXML
    private ComboBox<ExpenseTag> expenseTag;
    @FXML
    private TextField howMuch;
    @FXML
    private ComboBox currency;

    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<User> payer;
    @FXML
    private TextField whatFor;
    @FXML
    private DatePicker when;
    @FXML
    private CheckBox allParticipants;
    @FXML
    private CheckBox someParticipants;
    @FXML
    private VBox someParticipantsSelector;

    /**
     * Constructor
     *
     * @param service service
     * @param event    event of expense
     */
    @Inject
    public AddOrEditExpenseCtrl(AddOrEditExpenseService service, Event event) {
        this.service = service;
        this.event = event;
    }

    /**
     * removes a participant from the vbox options
     * @param payer the removed participant
     */
    private void excludePayerFromVBox(User payer) {
        for (Node n : someParticipantsSelector.getChildren()) {
            if (n instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) n;
                String text = checkBox.getText();
                int index = text.indexOf("(id: ");
                long id = Long.parseLong(text.substring(index + 5, text.length() - 1));
                if (id == payer.getUserID()) {
                    someParticipantsSelector.getChildren().remove(checkBox);
                    break;
                }
            }
        }

    }
    /**
     * during each page load, makes sure
     * the participant combobox display only usernames
     */
    public void initialize() {
        updatePayingParticipants();
        if (!expenseTag.getItems().isEmpty()) {
            expenseTag.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<>() {
                @Override
                public String toString(ExpenseTag tag) {
                    return tag.getName();
                }

                @Override
                public ExpenseTag fromString(String string) {
                    return null;
                }
            }));
        }

        payer.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user.getUsername();
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });
        expenseTag.setConverter(new StringConverter<ExpenseTag>() {
            @Override
            public String toString(ExpenseTag tag) {
                return tag.getName();
            }

            @Override
            public ExpenseTag fromString(String string) {
                return null;
            }
        });
    }

    /**
     * checks if a participant is in the vbox
     * @param payer the checked user
     * @return true if the user is present
     */
    private boolean isPayerInVBox(User payer) {
        for (Node n : someParticipantsSelector.getChildren()) {
            if (n instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) n;
                String text = checkBox.getText();
                int index = text.indexOf("(id: ");
                long id = Long.parseLong(text.substring(index + 5, text.length() - 1));
                if (id == payer.getUserID()) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Clears fields
     */
    public void clearFields() {
        expenseTag.setValue(event.getExpenseTags().get(0));
        howMuch.clear();
        currency.setValue(null);
        payer.setValue(null);
        whatFor.clear();
        when.setValue(null);
        allParticipants.setSelected(false);
        someParticipants.setSelected(false);
        someParticipantsSelector.setVisible(false);
        someParticipantsSelector.getChildren().clear();

    }

    /**
     * Checkbox method for allParticipants
     * if-clause is there to check only when the checkbox is
     * selected and not when it is de-selected.
     */
    @FXML
    private void allParticipantsPay() {
        if (allParticipants.isSelected()) {
            someParticipants.setSelected(false);
            someParticipantsSelector.setVisible(false);
        }
    }

    /**
     * Checkbox method for some participant
     * if-clause is there to check only when the checkbox is
     * selected anc not when it is de-selected.
     */
    @FXML
    private void someParticipantsPay() {
        if (someParticipants.isSelected()) {
            allParticipants.setSelected(false);
            someParticipantsSelector.setVisible(true);
        }
    }

    /**
     * Set expense
     *
     * @param expense to set
     */
    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    /**
     * Cancel add/edit
     */
    public void cancel() {
        clearFields();
        service.showEventInfo(event);
    }

    /**
     * Confirm add/edit
     */
    public void ok() {
        if (expense == null) {
            expense = getExpense();
            try {
                service.addExpense(getExpense());
            } catch (WebApplicationException e) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
            clearFields();
            service.updateAndShow(event);
        } else {
            selectedExpense();
        }

    }

    /**
     * edits the existing expense
     */
    private void selectedExpense() {
        try {
            List<Expense> expenses = new ArrayList<>(event.getExpenses());
            expenses.remove(expense);
            expense.setExpenseTag(expenseTag.getValue());
            expense.setAmount(Double.parseDouble(howMuch.getText()));
            expense.setName(whatFor.getText());
            expense.setPayer(payer.getValue());
            expense.setPayingParticipants(selectedParticipants());
            expense.setExpenseDate(Date.from(when.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            service.updateExpense(expense);
            expenses.add(expense);
            event.setExpenses(expenses);
            service.updateAndShow(event);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Get expense for adding a new expense
     *
     * @return expense
     */
    private Expense getExpense() {
        var p = new Expense(event);
        p.setPayer(payer.getValue());
        p.setName(whatFor.getText());
        p.setAmount(Double.parseDouble(howMuch.getText()));
        p.setExpenseTag(expenseTag.getValue());
        p.setExpenseDate(Date.from(when.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        List<User> payingParticipants = new ArrayList<>();
        payingParticipants.addAll(selectedParticipants());
        p.setPayingParticipants(payingParticipants);
        return p;
    }

    /**
     * checks if the all participants button is selected,
     * if it is selected, it returns all participants
     * if not, checks for the selected checkboxes under some participants
     *
     * @return paying participants
     */
    private List<User> selectedParticipants() {
        if (allParticipants.isSelected()) {
            List<User> selected = new ArrayList<>(event.getParticipants());
            selected.remove(payer.getValue());
            return selected;
        }
        else {
            List <User> selected = new ArrayList<>();
            for(Node n : someParticipantsSelector.getChildren()) {
                if(((CheckBox) n).isSelected()) {
                    String text = ((CheckBox) n).getText();
                    int index = text.indexOf("(id: ");
                    long id = Long.parseLong(text.substring(index + 5, text.length() - 1));
                    selected.add(service.getUserById(id));
                }
            }
            return selected;

        }
    }


    /**
     * @param e key event
     */
    @FXML
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                if (payer.isFocused()) {
                    payer.show();
                    break;
                } else if (expenseTag.isFocused()) {
                    expenseTag.show();
                    break;
                } else if (currency.isFocused()) {
                    currency.show();
                    break;
                } else {
                    ok();

                }
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Setup method
     * @param event   event where the expense exists
     * @param expense expense to add or edit
     */
    public void setup(Event event, Expense expense) {
        this.event = event;
        this.expense = expense;
        setFields();
        expenseTag.getItems().setAll(event.getExpenseTags());
        for (User u : event.getParticipants()) {
            someParticipantsSelector.getChildren()
                    .add(new CheckBox(u.getUsername() + "(id: " + u.getUserID() + ")"));
        }
        if (expense != null) {
            List<Long> ids = expense.getPayingParticipants()
                    .stream().map(q -> q.getUserID()).toList();
            for (Node n : someParticipantsSelector.getChildren()) {
                String text = ((CheckBox) n).getText();
                int index = text.indexOf("(id: ");
                long id = Long.parseLong(text.substring(index + 5, text.length() - 1));
                if (ids.contains(id)) {
                    ((CheckBox) n).setSelected(true);
                }
            }
        }
        payer.setItems(FXCollections.observableList(event.getParticipants()));
        payer.setValue(event.getParticipants().get(0));
        expenseTag.setValue(event.getExpenseTags().get(0));
        if (expense == null) {
            okButton.setText(service.getString("add"));
        } else {
            okButton.setText("Edit");
        }
    }



    /**
     * fills the field with the edited expense's info
     * or with blank if a new expense is added
     */
    public void setFields() {
        if (expense == null)
            clearFields();
        else {
            expenseTag.setValue(expense.getExpenseTag());
            howMuch.setText(String.valueOf(expense.getAmount()));
            currency.setValue(null);
            payer.setValue(expense.getPayer());
            whatFor.setText(expense.getName());
            when.setValue(Instant.ofEpochMilli(expense.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
            expenseTag.getSelectionModel().select(expense.getExpenseTag());
            if (expense.getPayingParticipants().size() == event.getParticipants().size() - 1) {
                allParticipants.setSelected(true);
                someParticipants.setSelected(false);
            } else {
                someParticipants.setSelected(true);
                someParticipantsPay();

            }
        }
    }

    /**
     * every time new payer is selected, the list is changed
     * @param actionEvent changing of the payer
     */
    public void handlePayerSelection(ActionEvent actionEvent) {
        updatePayingParticipants();
    }

    /**
     * removes the payer from the checkboxes whenever the payer in the combobox changes
     */
    private void updatePayingParticipants() {
        payer.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (oldValue != null && !isPayerInVBox(oldValue)) {
                        someParticipantsSelector.getChildren().add
                            (new CheckBox(oldValue.getUsername() +
                                "(id: " + oldValue.getUserID() + ")"));
                    }
                    if (newValue != null && (oldValue == null || !newValue.equals(oldValue))) {
                        excludePayerFromVBox(newValue);
                    }
                });
    }
}

