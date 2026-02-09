package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.Message;
import org.example.model.User;
import org.example.observer.Observer;
import org.example.observer.Signal;
import org.example.service.CardService;
import org.example.service.FriendshipService;
import org.example.service.MessageService;
import org.example.service.UserService;

import java.io.IOException;
import java.util.List;
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

    private MessageService messageService;
    private UserService userService;
    private FriendshipService friendshipService;
    private CardService cardService;

    private User loggedUser;
    private List<User> recipients;
    private User otherUser;
    private Stage stage;

    private Message selectedMessageForReply = null;

    /**
     * Seteaza service-urile necesare pentru controller.
     *
     * @param us service-ul utilizator
     * @param ms service-ul mesaje
     * @param fs service-ul prietenii
     * @param cs service-ul carduri
     */
    public void setServices(UserService us, MessageService ms, FriendshipService fs, CardService cs) {
        this.messageService = ms;
        this.userService = us;
        this.friendshipService = fs;
        this.cardService = cs;

        this.messageService.addObserver(this);
        this.userService.addObserver(this);
    }

    /**
     * Seteaza fereastra curenta.
     *
     * @param stage fereastra curenta
     */
    public void setStage(Stage stage) {
        this.stage = stage;
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
        messagesBox.getChildren().clear();

        List<Message> msgs = messageService.getMultiConversation(loggedUser, recipients);

        for (Message m : msgs) {
            String senderName = m.getFrom().getId().equals(loggedUser.getId())
                    ? "You"
                    : m.getFrom().getUsername();

            String text;
            if (m.getReply() != null) {
                text = senderName + " (reply to: \"" + m.getReply().getMessage() + "\"):\n" + m.getMessage();
            } else {
                text = senderName + ": " + m.getMessage();
            }

            Label lbl = new Label(text);
            lbl.setStyle("-fx-background-color: #efefef; -fx-padding: 6; -fx-background-radius: 6;");
            lbl.setWrapText(true);

            lbl.setOnMouseClicked(ev -> {
                selectedMessageForReply = m;
                replyLabel.setText(m.getMessage());
                replyBox.setVisible(true);
                ev.consume();
            });

            messagesBox.getChildren().add(lbl);
        }

        messagesBox.setOnMouseClicked(ev -> {
            cancelReply();
        });

        scrollPane.setOnMouseClicked(ev -> {
            cancelReply();
        });

        scrollPane.layout();
        scrollPane.setVvalue(1.0);
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
     * Metoda apelata atunci cand se primeste un semnal de actualizare.
     *
     * @param signal semnalul de actualizare
     */
    @Override
    public void update(Signal signal) {
        if (!signal.getType().equals("NEW_MESSAGE"))
            return;

        if (messagesBox.getScene() == null)
            return;

        loadMessages();
    }

    /**
     * Revine la fereastra principala de mesaje.
     */
    @FXML
    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/messages-window.fxml"));
        Stage stage = new Stage();

        double width = 800;
        double height = 600;

        stage.setScene(new Scene(loader.load(), width, height));

        MessagesController ctrl = loader.getController();
        ctrl.setServices(userService, messageService, friendshipService, cardService);
        ctrl.setLoggedUser(loggedUser);
        ctrl.setStage(stage);

        stage.setTitle("Messages");
        stage.show();

        Stage currentStage = (Stage) replyBox.getScene().getWindow();
        currentStage.close();
    }
}
