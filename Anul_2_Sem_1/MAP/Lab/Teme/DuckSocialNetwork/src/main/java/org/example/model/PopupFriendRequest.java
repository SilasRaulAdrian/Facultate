package org.example.model;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.service.FriendRequestService;

/**
 * Clasa pentru afisarea unui popup de cerere de prietenie.
 */
public class PopupFriendRequest {
    /**
     * Afiseaza un popup de cerere de prietenie.
     *
     * @param ownerStage fereastra parinte
     * @param from    utilizatorul care a trimis cererea
     * @param service service-ul pentru gestionarea cererilor de prietenie
     * @param requestId ID-ul cererii de prietenie
     */
    public static void show(
            Stage ownerStage,
            User from,
            FriendRequestService service,
            Long requestId
    ) {

        Stage popup = new Stage(StageStyle.TRANSPARENT);
        popup.initOwner(ownerStage);
        popup.initModality(javafx.stage.Modality.NONE);

        Label avatar = new Label(from.getUsername().substring(0, 1).toUpperCase());
        avatar.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);
            -fx-text-fill: white;
            -fx-font-size: 24px;
            -fx-font-weight: bold;
            -fx-min-width: 50px;
            -fx-min-height: 50px;
            -fx-max-width: 50px;
            -fx-max-height: 50px;
            -fx-background-radius: 25px;
            -fx-alignment: center;
        """);

        Label title = new Label("Friend Request");
        title.setStyle("""
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-text-fill: #2d3748;
        """);

        Label message = new Label(from.getUsername() + " wants to be your friend");
        message.setStyle("""
            -fx-font-size: 12px;
            -fx-text-fill: #718096;
        """);

        VBox textBox = new VBox(3, title, message);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(12, avatar, textBox);
        header.setAlignment(Pos.CENTER_LEFT);

        Button accept = new Button("Accept");
        accept.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #48bb78, #38a169);
            -fx-text-fill: white;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
            -fx-padding: 8 20;
            -fx-background-radius: 6px;
            -fx-cursor: hand;
            -fx-border-width: 0;
        """);

        accept.setOnMouseEntered(e -> accept.setStyle(accept.getStyle() +
                "-fx-background-color: linear-gradient(to bottom, #38a169, #2f855a);"));
        accept.setOnMouseExited(e -> accept.setStyle(accept.getStyle().replace(
                "-fx-background-color: linear-gradient(to bottom, #38a169, #2f855a);",
                "-fx-background-color: linear-gradient(to bottom, #48bb78, #38a169);")));

        Button decline = new Button("Decline");
        decline.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: #718096;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
            -fx-padding: 8 20;
            -fx-background-radius: 6px;
            -fx-cursor: hand;
            -fx-border-color: #e2e8f0;
            -fx-border-width: 1px;
            -fx-border-radius: 6px;
        """);

        decline.setOnMouseEntered(e -> decline.setStyle(decline.getStyle() +
                "-fx-background-color: #f7fafc;"));
        decline.setOnMouseExited(e -> decline.setStyle(decline.getStyle().replace(
                "-fx-background-color: #f7fafc;", "-fx-background-color: transparent;")));

        accept.setOnAction(e -> {
            service.accept(requestId);
            closeWithAnimation(popup);
        });

        decline.setOnAction(e -> {
            service.decline(requestId);
            closeWithAnimation(popup);
        });

        HBox buttons = new HBox(10, accept, decline);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(15, header, buttons);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(20));
        root.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12px;
            -fx-min-width: 350px;
        """);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        shadow.setRadius(20);
        shadow.setOffsetY(5);
        root.setEffect(shadow);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        popup.setScene(scene);

        popup.show();

        popup.setX(
                ownerStage.getX() + ownerStage.getWidth() / 2 - popup.getWidth() / 2
        );
        popup.setY(
                ownerStage.getY() + ownerStage.getHeight() / 2 - popup.getHeight() / 2
        );

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), root);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1);
        scaleIn.setToY(1);

        fadeIn.play();
        scaleIn.play();

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> closeWithAnimation(popup));
        delay.play();
    }

    /**
     * Inchide popup-ul cu o animatie de fade out.
     *
     * @param popup fereastra popup de inchis
     */
    private static void closeWithAnimation(Stage popup) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), popup.getScene().getRoot());
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> popup.close());
        fadeOut.play();
    }
}