package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.FriendRequest;
import org.example.model.User;
import org.example.service.FriendRequestService;
import org.example.service.FriendshipService;

import java.util.List;

/**
 * Controller pentru fereastra de cereri de prietenie.
 */
public class FriendRequestsController {
    @FXML private VBox requestsContainer;
    @FXML private Label noRequestsLabel;

    private FriendRequestService service;
    private FriendshipService friendshipService;
    private User loggedUser;

    /**
     * Seteaza serviciile necesare pentru controller.
     *
     * @param service          service-ul de cereri de prietenie
     * @param friendshipService service-ul de prietenii
     */
    public void setServices(FriendRequestService service, FriendshipService friendshipService) {
        this.service = service;
        this.friendshipService = friendshipService;
    }

    /**
     * Seteaza utilizatorul autentificat.
     *
     * @param user utilizatorul autentificat
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        loadRequests();
    }

    /**
     * Incarca cererile de prietenie in container.
     */
    public void loadRequests() {
        requestsContainer.getChildren().clear();

        List<FriendRequest> requests = service.getPending(loggedUser);

        if (requests.isEmpty()) {
            noRequestsLabel.setVisible(true);
            noRequestsLabel.setManaged(true);
        } else {
            noRequestsLabel.setVisible(false);
            noRequestsLabel.setManaged(false);

            for (FriendRequest request : requests) {
                requestsContainer.getChildren().add(createRequestCard(request));
            }
        }
    }

    /**
     * Creează un element VBox stilizat pentru o singură cerere.
     */
    private VBox createRequestCard(FriendRequest request) {
        User from = request.getFrom();
        Long requestId = request.getId();

        Label avatar = new Label(from.getUsername().substring(0, 1).toUpperCase());
        avatar.setStyle("-fx-background-color: #a0aec0; -fx-text-fill: white; -fx-font-size: 16px; " +
                "-fx-min-width: 40px; -fx-min-height: 40px; -fx-max-width: 40px; -fx-max-height: 40px; " +
                "-fx-background-radius: 20px; -fx-alignment: center;");

        Label username = new Label(from.getUsername());
        username.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");

        VBox textBox = new VBox(0, username);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Button accept = new Button("Accept");
        accept.setStyle("-fx-background-color: #48bb78; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 4px; -fx-cursor: hand;");

        Button decline = new Button("Decline");
        decline.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #2d3748; -fx-font-size: 11px; -fx-background-radius: 4px; -fx-cursor: hand;");

        accept.setOnAction(e -> {
            service.accept(requestId);
            loadRequests();
        });

        decline.setOnAction(e -> {
            service.decline(requestId);
            loadRequests();
        });

        HBox buttons = new HBox(5, accept, decline);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        HBox cardContent = new HBox(15, avatar, textBox, buttons);
        cardContent.setAlignment(Pos.CENTER_LEFT);
        cardContent.setPadding(new Insets(10));
        HBox.setHgrow(textBox, javafx.scene.layout.Priority.ALWAYS);

        VBox card = new VBox(cardContent);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-border-width: 1px;");

        return card;
    }

    /**
     * Inchide fereastra curenta.
     */
    @FXML
    private void goBack() {
        Stage stage = (Stage) requestsContainer.getScene().getWindow();
        stage.close();
    }
}