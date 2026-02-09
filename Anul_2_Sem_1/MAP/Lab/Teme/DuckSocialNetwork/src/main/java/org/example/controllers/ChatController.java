package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.AudioBubble;
import org.example.model.Message;
import org.example.model.User;
import org.example.observer.Observer;
import org.example.observer.Signal;
import org.example.service.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller pentru fereastra de chat intre doi utilizatori.
 */
public class ChatController implements Observer {
    @FXML private Label chatLabel;
    @FXML private VBox messagesBox;
    @FXML private TextField messageField;
    @FXML private ScrollPane scrollPane;
    @FXML private Label replyLabel;
    @FXML private VBox replyBox;
    @FXML private Label typingLabel;
    @FXML private Button recordButton;

    private MessageService messageService;
    private UserService userService;
    private FriendshipService friendshipService;
    private CardService cardService;
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private EventService eventService;

    private User loggedUser;
    private List<User> recipients;
    private User otherUser;
    private Stage stage;

    private TargetDataLine microphone;
    private AudioFormat format = new AudioFormat(16000, 16, 1, true, true);
    private File currentRecordingFile;

    private Message selectedMessageForReply = null;

    private Set<Long> usersTyping = new HashSet<>();
    private final Map<Long, Clip> audioClips = new HashMap<>();
    private final Map<Long, Long> audioPositions = new HashMap<>();

    /**
     * Seteaza service-urile necesare pentru controller.
     *
     * @param us service-ul utilizator
     * @param ms service-ul mesaje
     * @param fs service-ul prietenii
     * @param cs service-ul carduri
     * @param frs service-ul cereri de prietenie
     * @param ns service-ul notificari
     * @param es service-ul evenimente
     */
    public void setServices(UserService us, MessageService ms, FriendshipService fs, CardService cs, FriendRequestService frs, NotificationService ns, EventService es) {
        this.messageService = ms;
        this.userService = us;
        this.friendshipService = fs;
        this.cardService = cs;
        this.friendRequestService = frs;
        this.notificationService = ns;
        this.eventService = es;

        this.messageService.addObserver(this);
        this.userService.addObserver(this);

        messageField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                messageService.sendTypingSignal(loggedUser, recipients);
            } else {
                messageService.sendStopTypingSignal(loggedUser, recipients);
            }
        });

        messageField.requestFocus();
    }

    @FXML
    public void initialize() {
        setupRecordButton();
    }

    private void setupRecordButton() {
        recordButton.setOnMousePressed(e -> startRecording());
        recordButton.setOnMouseReleased(e -> stopRecording());

        recordButton.setStyle("""
            -fx-background-color: #ff4d4d; 
            -fx-text-fill: white; 
            -fx-font-size: 20;
            -fx-background-radius: 40;
            -fx-cursor: hand;
            -fx-padding: 5;
    """);

        recordButton.setOnMouseEntered(e ->
                recordButton.setStyle("""
                    -fx-background-color: #ff6666;
                    -fx-text-fill: white;
                    -fx-font-size: 20;
                    -fx-background-radius: 40;
                    -fx-cursor: hand;
                    -fx-padding: 5;
            """)
        );

        recordButton.setOnMouseExited(e ->
                recordButton.setStyle("""
                    -fx-background-color: #ff4d4d; 
                    -fx-text-fill: white; 
                    -fx-font-size: 20;
                    -fx-background-radius: 40;
                    -fx-cursor: hand;
                    -fx-padding: 5;
            """)
        );
    }

    /**
     * Seteaza fereastra curenta.
     *
     * @param stage fereastra curenta
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(event -> {
            stopAllAudio();
        });
    }

    /**
     * Incarca chat-ul dintre doi utilizatori.
     *
     * @param from utilizatorul care trimite mesajele
     * @param to   utilizatorul care primeste mesajele
     */
    public void loadChat(User from, List<User> to) {
        this.loggedUser = from;

        this.recipients = to.stream()
                .filter(u -> !u.getId().equals(from.getId()))
                .toList();

        if (recipients.size() == 1)
            chatLabel.setText("Chat with " + recipients.get(0).getUsername());
        else
            chatLabel.setText("Group chat: " +
                    recipients.stream().map(User::getUsername).collect(Collectors.joining(", ")));

        replyBox.setVisible(false);

        loadMessages();
    }

    /**
     * Incarca mesajele din conversatie intre cei doi utilizatori.
     */
    private void loadMessages() {
        stopAllAudio();
        messagesBox.getChildren().clear();

        List<Message> msgs = messageService.getMultiConversation(loggedUser, recipients);

        msgs.sort(Comparator.comparing(Message::getDate));

        LocalDate currentDate = null;

        for (Message m : msgs) {

            LocalDate msgDate = m.getDate().toLocalDate();

            if (!msgDate.equals(currentDate)) {
                currentDate = msgDate;

                Label dateHeader = new Label(currentDate.toString());
                dateHeader.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
                dateHeader.setAlignment(Pos.CENTER);
                dateHeader.setMaxWidth(Double.MAX_VALUE);

                messagesBox.getChildren().add(dateHeader);
            }

            boolean isMe = m.getFrom().getId().equals(loggedUser.getId());

            String myColor = """
                -fx-background-color: #b3d9ff;
                -fx-padding: 8;
                -fx-background-radius: 10;
                -fx-text-fill: black;
                """;

            String otherColor = """
                -fx-background-color: #e6e6e6;
                -fx-padding: 8;
                -fx-background-radius: 10;
                -fx-text-fill: black;
                """;

            HBox row = new HBox(8);
            row.setAlignment(isMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            if (m.getAudioPath() != null) {
                AudioBubble audioBubble = new AudioBubble(m.getAudioPath(), isMe);

                audioBubble.setOnMouseClicked(ev -> {
                    messageField.requestFocus();
                    ev.consume();
                });

                VBox bubbleWithTime = new VBox(3);
                bubbleWithTime.getChildren().add(audioBubble);

                Label timeLabel = new Label(m.getDate().toLocalTime().toString().substring(0,5));
                timeLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 10;");
                timeLabel.setAlignment(isMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

                bubbleWithTime.getChildren().add(timeLabel);

                row.getChildren().add(bubbleWithTime);
                messagesBox.getChildren().add(row);
                continue;
            }

            String text;
            if (m.getReply() != null &&
                    m.getReply().getMessage() != null &&
                    !m.getReply().getMessage().isEmpty()) {

                text = "(reply to: \"" + m.getReply().getMessage() + "\")\n" +
                        (m.getMessage() == null ? "" : m.getMessage());
            } else {
                text = (m.getMessage() == null ? "" : m.getMessage());
            }

            Label bubble = new Label(text);
            bubble.setWrapText(true);
            bubble.setStyle(isMe ? myColor : otherColor);

            bubble.setOnMouseClicked(ev -> {
                selectedMessageForReply = m;
                replyLabel.setText(m.getMessage());
                replyBox.setVisible(true);
                ev.consume();
            });

            Label timeLabel = new Label(m.getDate().toLocalTime().toString().substring(0,5));
            timeLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 10;");
            timeLabel.setAlignment(isMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            VBox bubbleWithTime = new VBox(3);
            bubbleWithTime.getChildren().addAll(bubble, timeLabel);

            row.getChildren().add(bubbleWithTime);
            messagesBox.getChildren().add(row);
        }

        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    /**
     * Comuta redarea unui fisier audio.
     * @param msgId ID-ul mesajului
     * @param path Calea catre fisierul audio
     * @param btn Butonul de play/pause
     * @param isMe Daca mesajul este al utilizatorului logat
     */
    private void toggleAudio(Long msgId, String path, Button btn, boolean isMe) {
        try {
            Clip clip = audioClips.get(msgId);

            if (clip == null) {
                File audioFile = new File(path);
                AudioInputStream audio = AudioSystem.getAudioInputStream(audioFile);

                clip = AudioSystem.getClip();
                clip.open(audio);
                audioClips.put(msgId, clip);
                audioPositions.put(msgId, 0L);
            }

            if (clip.isRunning()) {
                audioPositions.put(msgId, clip.getMicrosecondPosition());
                clip.stop();
                btn.setText("▶ PLAY");
                return;
            }

            Long pos = audioPositions.getOrDefault(msgId, 0L);
            clip.setMicrosecondPosition(pos);
            clip.start();
            btn.setText("⏸ PAUSE");

            Clip finalClip = clip;
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && !finalClip.isRunning()) {
                    if (finalClip.getMicrosecondPosition() >= finalClip.getMicrosecondLength()) {
                        btn.setText("▶ PLAY");
                        audioPositions.put(msgId, 0L);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Anuleaza raspunsul la un mesaj.
     */
    @FXML
    private void cancelReply() {
        selectedMessageForReply = null;
        replyBox.setVisible(false);
    }

    /**
     * Trimite un mesaj catre celalalt utilizator.
     */
    @FXML
    public void sendMessage() {
        String text = messageField.getText().trim();
        if (text.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertisment");
            alert.setHeaderText("Mesaj gol");
            alert.setContentText("Nu puteti trimite un mesaj gol.");
            alert.showAndWait();

            cancelReply();
            return;
        }

        messageService.sendStopTypingSignal(loggedUser, recipients);
        messageService.sendMessageWithReply(
                loggedUser,
                recipients,
                text,
                selectedMessageForReply
        );

        messageField.clear();
        cancelReply();

        loadMessages();
    }

    /**
     * Actualizeaza afisarea utilizatorilor care tasteaza
     */
    private void updateTypingLabel() {
        if (usersTyping.isEmpty()) {
            typingLabel.setVisible(false);
            typingLabel.setText("");
            return;
        }

        List<String> typingNames = recipients.stream()
                .filter(u -> usersTyping.contains(u.getId()))
                .map(User::getUsername)
                .collect(Collectors.toList());

        if (typingNames.isEmpty()) {
            typingLabel.setVisible(false);
            typingLabel.setText("");
            return;
        }

        String text;
        if (typingNames.size() == 1) {
            text = typingNames.get(0) + " is typing...";
        } else if (typingNames.size() == 2) {
            text = typingNames.get(0) + " and " + typingNames.get(1) + " are typing...";
        } else {
            String allButLast = String.join(", ", typingNames.subList(0, typingNames.size() - 1));
            text = allButLast + " and " + typingNames.get(typingNames.size() - 1) + " are typing...";
        }

        typingLabel.setText(text);
        typingLabel.setVisible(true);
    }

    /**
     * Metoda apelata atunci cand se primeste un semnal de actualizare.
     *
     * @param signal semnalul de actualizare
     */
    @Override
    public void update(Signal signal) {
        if ("TYPING".equals(signal.getType())) {

            if (!signal.getFrom().getId().equals(loggedUser.getId())
                    && recipients.stream().anyMatch(u -> u.getId().equals(signal.getFrom().getId()))) {

                usersTyping.add(signal.getFrom().getId());
                updateTypingLabel();
            }

            return;
        }

        if ("STOP_TYPING".equals(signal.getType())) {

            if (!signal.getFrom().getId().equals(loggedUser.getId())
                    && recipients.stream().anyMatch(u -> u.getId().equals(signal.getFrom().getId()))) {

                usersTyping.remove(signal.getFrom().getId());
                updateTypingLabel();
            }

            return;
        }

        if (signal.getType().equals("NEW_MESSAGE")) {
            if (messagesBox.getScene() == null) return;

            if (signal.getFrom() != null) {
                usersTyping.remove(signal.getFrom().getId());
            }

            typingLabel.setVisible(false);
            loadMessages();
        }
    }


    /**
     * Revine la fereastra principala de mesaje.
     */
    @FXML
    public void goBack() throws IOException {
        stopAllAudio();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/messages-window.fxml"));
        Stage stage = new Stage();

        double width = 800;
        double height = 600;

        stage.setScene(new Scene(loader.load(), width, height));

        MessagesController ctrl = loader.getController();
        ctrl.setServices(userService, messageService, friendshipService, cardService, friendRequestService, notificationService, eventService);
        ctrl.setLoggedUser(loggedUser);
        ctrl.setStage(stage);

        stage.setTitle("Messages");
        stage.show();

        Stage currentStage = (Stage) replyBox.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Incepe inregistrarea unui mesaj audio.
     */
    @FXML
    public void startRecording() {
        try {
            currentRecordingFile = new File("audio_" + System.currentTimeMillis() + ".wav");
            messageService.sendTypingSignal(loggedUser, recipients);
            recordButton.setText("⏺");

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            Thread thread = new Thread(() -> {
                try {
                    AudioInputStream ais = new AudioInputStream(microphone);
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, currentRecordingFile);
                } catch (Exception ignored) {}
            });
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opreste inregistrarea unui mesaj audio si trimite mesajul.
     */
    @FXML
    public void stopRecording() {
        try {
            if (microphone != null) {
                microphone.stop();
                microphone.close();
            }

            if (currentRecordingFile != null && currentRecordingFile.exists()) {
                messageService.sendStopTypingSignal(loggedUser, recipients);
                messageService.sendAudioMessage(
                        loggedUser,
                        recipients,
                        currentRecordingFile.getAbsolutePath(),
                        selectedMessageForReply
                );
            }

            recordButton.setText("🎤");
            loadMessages();
            cancelReply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reda un fisier audio dat ca si cale.
     *
     * @param path calea catre fisierul audio
     */
    public void playAudio(String path) {
        try {
            File audioFile = new File(path);
            AudioInputStream audio = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opreste toate fisierele audio care se redau.
     */
    private void stopAllAudio() {
        for (var node : messagesBox.getChildren()) {
            if (node instanceof HBox row) {
                for (var n : row.getChildren()) {
                    if (n instanceof AudioBubble audioBubble) {
                        audioBubble.dispose();
                    }
                }
            }
        }
    }
}