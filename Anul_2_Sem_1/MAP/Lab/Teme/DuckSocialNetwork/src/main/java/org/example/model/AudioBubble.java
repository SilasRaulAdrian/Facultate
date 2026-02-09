package org.example.model;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * Clasa care reprezinta o bula audio in interfata de chat.
 */
public class AudioBubble extends HBox {
    private final Button playPauseBtn = new Button("▶ Play");
    private final Slider progress = new Slider(0, 1, 0);
    private final Label timeLabel = new Label("00:00");
    private final HBox waveform = new HBox(3);
    private final MediaPlayer player;
    private final Timeline uiUpdater;
    private boolean userSeeking = false;

    /**
     * Constructor pentru clasa AudioBubble.
     * @param audioPath Calea catre fisierul audio.
     * @param isMine Indica daca bula este a utilizatorului curent.
     */
    public AudioBubble(String audioPath, boolean isMine) {
        setSpacing(8);
        setPadding(new Insets(6));
        setAlignment(isMine ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        String bubbleBg = isMine ? "-fx-background-color: transparent;" : "-fx-background-color: transparent;";
        setStyle(bubbleBg);

        File f = new File(audioPath);
        Media media = new Media(f.toURI().toString());
        player = new MediaPlayer(media);

        playPauseBtn.setCursor(Cursor.HAND);
        playPauseBtn.setStyle("-fx-background-radius: 6; -fx-padding: 6 8;");
        playPauseBtn.setOnMouseEntered(e -> playPauseBtn.setStyle("-fx-background-radius: 6; -fx-padding: 6 8; -fx-opacity: 0.9;"));
        playPauseBtn.setOnMouseExited(e -> playPauseBtn.setStyle("-fx-background-radius: 6; -fx-padding: 6 8; -fx-opacity: 1;"));

        waveform.setAlignment(Pos.CENTER);

        progress.setPrefWidth(180);
        progress.setMinWidth(100);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.setCursor(Cursor.HAND);

        progress.setOnMousePressed(e -> userSeeking = true);
        progress.setOnMouseReleased(e -> {
            userSeeking = false;
            if (player.getTotalDuration() != null) {
                Duration t = player.getTotalDuration().multiply(progress.getValue());
                player.seek(t);
            }
        });

        timeLabel.setText("00:00");

        playPauseBtn.setOnAction(e -> {
            switchPlayPause();
        });

        uiUpdater = new Timeline(new KeyFrame(Duration.millis(200), ev -> {
            if (!userSeeking && player.getTotalDuration() != null && player.getCurrentTime() != null) {
                Duration total = player.getTotalDuration();
                Duration cur = player.getCurrentTime();
                if (!total.isUnknown() && total.toMillis() > 0) {
                    double frac = cur.toMillis() / total.toMillis();
                    progress.setValue(frac);
                }
                timeLabel.setText(formatTime((long) player.getCurrentTime().toSeconds()) + " / " + formatTime((long) player.getTotalDuration().toSeconds()));
            }
        }));
        uiUpdater.setCycleCount(Animation.INDEFINITE);

        player.setOnReady(() -> {
            long totalSec = (long) player.getTotalDuration().toSeconds();
            timeLabel.setText(formatTime(totalSec));
            progress.setValue(0);
        });

        player.currentTimeProperty().addListener((obs, oldT, newT) -> {
            if (!userSeeking && player.getTotalDuration() != null && !player.getTotalDuration().isUnknown()) {
                double frac = newT.toMillis() / player.getTotalDuration().toMillis();
                progress.setValue(frac);
                timeLabel.setText(formatTime((long)newT.toSeconds()) + " / " + formatTime((long)player.getTotalDuration().toSeconds()));
            }
        });

        player.setOnEndOfMedia(() -> {
            player.stop();
            progress.setValue(0);
            playPauseBtn.setText("▶ Play");
            uiUpdater.stop();
            if (player.getTotalDuration() != null && !player.getTotalDuration().isUnknown()) {
                timeLabel.setText(formatTime((long)player.getTotalDuration().toSeconds()));
            }
        });

        VBox centerBox = new VBox(4);
        HBox.setHgrow(centerBox, Priority.ALWAYS);
        centerBox.setAlignment(Pos.CENTER_LEFT);

        HBox progRow = new HBox(6);
        progRow.setAlignment(Pos.CENTER_LEFT);
        progRow.getChildren().addAll(progress, timeLabel);

        centerBox.getChildren().addAll(waveform, progRow);

        if (isMine) {
            getChildren().addAll(centerBox, playPauseBtn);
        } else {
            getChildren().addAll(playPauseBtn, centerBox);
        }

        focusedProperty().addListener((o, oldV, newV) -> {
            if (!newV) {
                dispose();
            }
        });
    }

    /**
     * Schimba starea de redare intre play si pause.
     */
    private void switchPlayPause() {
        MediaPlayer.Status s = player.getStatus();
        if (s == MediaPlayer.Status.PLAYING) {
            player.pause();
            playPauseBtn.setText("▶ Play");
            uiUpdater.stop();
        } else {
            player.play();
            playPauseBtn.setText("❚❚ Pause");
            uiUpdater.play();
        }
    }

    /**
     * Formateaza timpul in format MM:SS.
     * @param sec Timpul in secunde.
     * @return Timpul formatat ca String.
     */
    private String formatTime(long sec) {
        long m = sec / 60;
        long s = sec % 60;
        return String.format("%02d:%02d", m, s);
    }

    /**
     * Elibereaza resursele utilizate de bula audio.
     */
    public void dispose() {
        uiUpdater.stop();
        player.stop();
        player.dispose();
    }
}
