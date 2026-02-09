package org.example.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.Oras;
import org.example.domain.Persoana;
import org.example.service.AppService;

import static javafx.collections.FXCollections.observableArrayList;

public class LoginRegisterController {
    private final AppService service;
    private final Stage stage = new Stage();

    public LoginRegisterController(AppService service) {
        this.service = service;
        stage.setTitle("Login / Register");
        stage.setScene(new Scene(buildRoot(), 900, 450));
    }

    private Pane buildRoot() {
        // Login (stanga)
        ListView<String> lvUsers = new ListView<>();
        lvUsers.setItems(observableArrayList(service.getAllUsernames()));

        Button btnLogin = new Button("Login");

        VBox left = new VBox(10, new Label("Login - select username:"), lvUsers, btnLogin);
        left.setPadding(new Insets(10));
        left.setPrefWidth(300);

        btnLogin.setOnAction(e -> {
            String u = lvUsers.getSelectionModel().getSelectedItem();
            if (u != null) doLogin(u);
        });

        // Register (dreapta)
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10);

        TextField tfNume = new TextField();
        TextField tfPrenume = new TextField();
        TextField tfUsername = new TextField();
        PasswordField pf1 = new PasswordField();
        PasswordField pf2 = new PasswordField();
        ComboBox<Oras> cbOras = new ComboBox<>(observableArrayList(Oras.values()));
        TextField tfStrada = new TextField();
        TextField tfNr = new TextField();
        TextField tfTel = new TextField();

        form.addRow(0, new Label("Nume:"), tfNume);
        form.addRow(1, new Label("Prenume:"), tfPrenume);
        form.addRow(2, new Label("Username:"), tfUsername);
        form.addRow(3, new Label("Parola:"), pf1);
        form.addRow(4, new Label("Confirma parola:"), pf2);
        form.addRow(5, new Label("Oras:"), cbOras);
        form.addRow(6, new Label("Strada:"), tfStrada);
        form.addRow(7, new Label("Numar:"), tfNr);
        form.addRow(8, new Label("Telefon:"), tfTel);

        for (int i = 0; i <= 8; i++) GridPane.setHgrow(form.getChildren().get(i*2+1), Priority.ALWAYS);

        Label err = new Label();
        err.setStyle("-fx-text-fill:red;");

        Button btnRegister = new Button("Register");
        btnRegister.setOnAction(e -> {
            err.setText("");
            try {
                Persoana p = service.register(
                        tfNume.getText(), tfPrenume.getText(), tfUsername.getText(),
                        pf1.getText(), pf2.getText(),
                        cbOras.getValue(),
                        tfStrada.getText(), tfNr.getText(), tfTel.getText()
                );
                // refresh lista login
                lvUsers.setItems(observableArrayList(service.getAllUsernames()));
                doLogin(p.getUsername());
            } catch (Exception ex) {
                err.setText(ex.getMessage());
            }
        });

        VBox right = new VBox(10, new Label("Register:"), form, btnRegister, err);
        right.setPadding(new Insets(10));

        HBox root = new HBox(10, left, new Separator(), right);
        return root;
    }

    private void doLogin(String username) {
        Persoana user = service.loginByUsername(username);
        new MainController(service, user).show();
    }

    public void show() {
        stage.show();
    }
}
