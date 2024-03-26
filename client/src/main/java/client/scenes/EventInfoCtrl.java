package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class EventInfoCtrl {
    private Event event;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label eventTitle;
    @FXML
    private Label description;
    @FXML
    private Label participantsLabel;
    @FXML
    private Label expensesLabel;
    @FXML
    private ComboBox<String> expenseComboBox;
    private List<User> participants = new ArrayList<>();

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Constructor
     * @param server
     * @param mainCtrl
     */
    @Inject
    public EventInfoCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    /**
     * Update label text
     * @param event
     */
    public void updateLabelText(Event event) {
        if (event != null || event.getTitle().length() != 0)
            eventTitle.setText(event.getTitle());
    }

    /**
     * Update label text
     * @param event
     */
    public void updateDesc(Event event) {
        if (event != null || event.getTitle().length() != 0)
            description.setText(event.getDescription());
    }

    /**
     * Set event
     * @param event
     */
    public void setEvent(Event event) {
        this.event = event;
    }


    /**
     * adds a participant when the button is clicked
     * @param actionEvent the clicking of the button
     */
    public void addParticipant(ActionEvent actionEvent) {
        User user = new User("demo", "demo@gmail.com");
        participants.add(user);
        expenseComboBox.getItems().add(user.getUsername());
    }

    /**
     * goes back to the main page
     * @param actionEvent when the button is clicked
     */
    public void back(ActionEvent actionEvent) {
        mainCtrl.showStartPage();
    }

}
