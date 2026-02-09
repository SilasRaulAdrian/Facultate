package org.example.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.hash.PasswordHasher;
import org.example.model.User;
import org.example.observer.Observer;
import org.example.observer.Signal;
import org.example.service.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

/**
 * Controller pentru fereastra de profil a utilizatorului.
 */
public class UserProfileController implements Observer {
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label idLabel;
    @FXML private ImageView profileImageView;
    @FXML private Label friendCountLabel;
    @FXML private Button editImageBtn;
    @FXML private Label typeLabel;

    private User loggedUser;
    private User viewedUser;
    private UserService userService;
    private FriendshipService friendshipService;
    private CardService cardService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private EventService eventService;
    private Stage stage;

    private static final String PROFILE_IMAGES_DIR = "profile_images";

    /**
     * Seteaza service-urile necesare pentru controller.
     *
     * @param us  service-ul utilizatori
     * @param fs  service-ul prietenii
     * @param cs  service-ul carduri
     * @param ms  service-ul mesaje
     * @param frs service-ul cereri de prietenie
     * @param ns  service-ul notificari
     * @param es  service-ul evenimente
     */
    public void setServices(UserService us, FriendshipService fs, CardService cs, MessageService ms, FriendRequestService frs, NotificationService ns, EventService es) {
        this.userService = us;
        this.friendshipService = fs;
        this.cardService = cs;
        this.messageService = ms;
        this.friendRequestService = frs;
        this.notificationService = ns;
        this.eventService = es;

        this.userService.addObserver(this);
    }

    /**
     * Seteaza utilizatorul autentificat.
     *
     * @param user utilizatorul autentificat
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        populateData();
    }

    /**
     * Seteaza utilizatorul al carui profil este vizualizat.
     *
     * @param user utilizatorul vizualizat
     */
    public void setViewedUser(User user) {
        this.viewedUser = user;
        populateData();

        if (editImageBtn != null) {
            boolean isOwner = loggedUser != null && loggedUser.getId().equals(viewedUser.getId());
            editImageBtn.setVisible(isOwner);
        }
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
     * Initializeaza directorul pentru imagini de profil.
     */
    private void initializeImageDirectory() {
        try {
            Path path = Paths.get(PROFILE_IMAGES_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Eroare la crearea directorului de imagini: " + e.getMessage());
        }
    }

    /**
     * Populeaza datele utilizatorului in interfata.
     */
    private void populateData() {
        if (viewedUser == null) return;

        usernameLabel.setText(viewedUser.getUsername());
        emailLabel.setText(
                viewedUser.getEmail() != null
                        ? viewedUser.getEmail()
                        : "No email provided"
        );
        idLabel.setText("User ID: " + viewedUser.getId());

        if (viewedUser instanceof org.example.model.Person p) {
            typeLabel.setText("Type: Person (" + p.getOcupatie() + ") 👤");
        } else if (viewedUser instanceof org.example.model.Duck d) {
            typeLabel.setText("Type: " + d.getTip() + " Duck 🦆");
        }

        refreshFriendCount();
        loadProfileImage();
    }

    /**
     * Incarca imaginea de profil a utilizatorului.
     */
    private void loadProfileImage() {
        try {
            Image image = null;

            if (viewedUser.getProfileImagePath() != null && !viewedUser.getProfileImagePath().isEmpty()) {
                File imageFile = new File(viewedUser.getProfileImagePath());
                if (imageFile.exists()) {
                    image = new Image(imageFile.toURI().toString());
                }
            }

            if (image == null) {
                image = new Image(getClass().getResourceAsStream("/images/default-profile.jpg"));
            }

            profileImageView.setImage(image);

            Circle clip = new Circle(
                    profileImageView.getFitWidth() / 2,
                    profileImageView.getFitHeight() / 2,
                    profileImageView.getFitWidth() / 2
            );
            profileImageView.setClip(clip);

        } catch (Exception e) {
            System.err.println("Eroare la incarcarea imaginii: " + e.getMessage());
        }
    }

    /**
     * Actualizeaza numarul de prieteni afisat.
     */
    private void refreshFriendCount() {
        int friends = friendshipService.getFriendsOf(viewedUser.getId()).size();
        friendCountLabel.setText("👥 " + friends + " Friends");
    }

    /**
     * Metoda apelata cand se primeste un semnal de la un observable.
     *
     * @param signal semnalul primit
     */
    @Override
    public void update(Signal signal) {
        if ("FRIEND_CHANGED".equals(signal.getType())) {
            if (signal.getFrom() != null &&
                    signal.getFrom().getId().equals(viewedUser.getId())) {

                refreshFriendCount();
            }
        }
    }

    /**
     * Handler pentru editarea profilului utilizatorului.
     */
    @FXML
    private void handleEditProfile() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");

        dialog.getDialogPane().setPrefWidth(450);

        TextField usernameField = new TextField(viewedUser.getUsername());
        TextField emailField = new TextField(viewedUser.getEmail());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Introduceti parola noua (sau lasati gol)");

        Platform.runLater(() -> usernameField.requestFocus());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(new ColumnConstraints(), column2);

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                viewedUser.setUsername(usernameField.getText());
                viewedUser.setEmail(emailField.getText());

                String newPassword = passwordField.getText();
                if (newPassword != null && !newPassword.trim().isEmpty()) {
                    String hashedPassword = PasswordHasher.hashPassword(newPassword);
                    viewedUser.setPassword(hashedPassword);
                }

                userService.modifyUser(viewedUser.getId(), viewedUser);
                populateData();
                dialog.close();
            }
        });
    }

    /**
     * Handler pentru schimbarea imaginii de profil.
     */
    @FXML
    private void handleChangeProfileImage() {
        if (!loggedUser.getId().equals(viewedUser.getId())) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "You can only change your own profile picture!");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                initializeImageDirectory();

                String extension = getFileExtension(selectedFile.getName());
                String newFileName = "user_" + viewedUser.getId() + extension;
                Path destinationPath = Paths.get(PROFILE_IMAGES_DIR, newFileName);

                Files.copy(selectedFile.toPath(), destinationPath,
                        StandardCopyOption.REPLACE_EXISTING);

                viewedUser.setProfileImagePath(destinationPath.toString());
                userService.modifyUser(viewedUser.getId(), viewedUser);

                loadProfileImage();

                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Profile picture updated successfully!");
                alert.showAndWait();

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Error saving profile image: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtine extensia unui fisier.
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex);
        }
        return ".jpg";
    }

    /**
     * Handler pentru intoarcerea la fereastra principala.
     */
    @FXML
    private void handleBack() {
        stage.close();
    }
}
