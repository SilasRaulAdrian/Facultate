package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.service.FriendshipService;

/**
 * Clasa controller pentru fereastra de adaugare a unei prietenii.
 */
public class AddFriendshipController {
    @FXML private TextField user1Field;
    @FXML private TextField user2Field;

    private FriendshipService service;
    private Stage stage;

    /**
     * Seteaza serviciile necesare si etapa curenta.
     *
     * @param fs serviciul de prietenii
     * @param st etapa curenta
     */
    public void setServices(FriendshipService fs, Stage st) {
        this.service = fs;
        this.stage = st;
    }

    /**
     * Gestioneaza evenimentul de adaugare a unei prietenii.
     */
    @FXML
    private void handleAdd() {
        try {
            long id1 = Long.parseLong(user1Field.getText());
            long id2 = Long.parseLong(user2Field.getText());

            service.addFriendship(id1, id2);

            stage.close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    /**
     * Inchide fereastra curenta.
     */
    @FXML
    private void goBack() {
        stage.close();
    }
}
