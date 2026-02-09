package org.example.model;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Clasa pentru afisarea notificarilor popup in aplicatie.
 */
public class PopupNotification {
    private static final Queue<Stage> ACTIVE_POPUPS = new LinkedList<>();
    private static final double POPUP_HEIGHT = 80;
    private static final double SPACING = 10;

    /**
     * Afiseaza o notificare popup.
     * @param ownerStage fereastra parinte
     * @param message mesajul notificarii
     * @param icon iconita notificarii (de exemplu, un emoji)
     */
    public static void show(Stage ownerStage, String message, String icon) {
        Stage popup = new Stage(StageStyle.TRANSPARENT);
        popup.initOwner(ownerStage);
        popup.initModality(javafx.stage.Modality.NONE);

        ACTIVE_POPUPS.add(popup);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #e53e3e;");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2d3748;");

        HBox content = new HBox(10, iconLabel, messageLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(content);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(15));
        root.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 8px;
            -fx-border-color: #e2e8f0;
            -fx-border-width: 1px;
            -fx-min-width: 280px;
        """);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        shadow.setRadius(10);
        shadow.setOffsetY(3);
        root.setEffect(shadow);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        popup.setScene(scene);

        double baseX = ownerStage.getX() + ownerStage.getWidth() - root.minWidth(0) - 20;
        double baseY = ownerStage.getY() + ownerStage.getHeight() - root.minHeight(0) - 20;

        int index = new LinkedList<>(ACTIVE_POPUPS).indexOf(popup);

        double newY = baseY - (index * POPUP_HEIGHT) - (index * SPACING);

        popup.setX(baseX);
        popup.setY(newY);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        popup.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> closeWithAnimation(popup));
        delay.play();
    }

    /**
     * Inchide popup-ul cu o animatie de fade out.
     * @param popup fereastra popup de inchis
     */
    private static void closeWithAnimation(Stage popup) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), popup.getScene().getRoot());
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> {
            popup.close();
            ACTIVE_POPUPS.remove(popup);
            repositionActivePopups((Stage) popup.getOwner());
        });

        fadeOut.play();
    }

    /**
     * Repozitioneaza toate popup-urile ramase dupa ce unul s-a inchis.
     * @param owner fereastra parinte a popup-urilor
     */
    private static void repositionActivePopups(Stage owner) {
        if (owner == null) return;

        double rootWidth = 280; // Valoare fixă din CSS
        double rootHeight = POPUP_HEIGHT;
        double baseX = owner.getX() + owner.getWidth() - rootWidth - 20;
        double baseY = owner.getY() + owner.getHeight() - rootHeight - 20;

        int index = 0;
        for (Stage popup : ACTIVE_POPUPS) {
            double newY = baseY - (index * rootHeight) - (index * SPACING);

            javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(Duration.millis(200), popup.getScene().getRoot());
            tt.setToY(newY - popup.getY());
            tt.play();

            index++;
        }
    }
}
