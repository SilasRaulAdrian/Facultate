package org.example.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.domain.Nevoie;
import org.example.domain.Persoana;
import org.example.observer.AppEvent;
import org.example.observer.Observer;
import org.example.service.AppService;

import java.time.format.DateTimeFormatter;

import static javafx.collections.FXCollections.observableArrayList;

public class MainController implements Observer<AppEvent> {
    private final AppService service;
    private final Persoana user;
    private final Stage stage = new Stage();

    private final TableView<Nevoie> tblNevoi = new TableView<>();
    private final TableView<Nevoie> tblFapte = new TableView<>();
    private final Label errAjut = new Label();
    private final Label errAdd = new Label();

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MainController(AppService service, Persoana user) {
        this.service = service;
        this.user = user;

        service.addObserver(this);

        stage.setTitle("User: " + user.getUsername() + " (" + user.getOras() + ")");
        stage.setScene(new Scene(buildRoot(), 1000, 600));

        reloadAll();
    }

    private Pane buildRoot() {
        TabPane tabs = new TabPane();
        tabs.getTabs().add(tabAjut());
        tabs.getTabs().add(tabFiuAjutat());
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane root = new BorderPane(tabs);
        root.setPadding(new Insets(10));
        return root;
    }

    private Tab tabAjut() {
        setupNevoiTable(tblNevoi, true);
        setupNevoiTable(tblFapte, false);

        Button btnPreia = new Button("Preia nevoia selectata");
        btnPreia.setOnAction(e -> onPreia());

        errAjut.setStyle("-fx-text-fill:red;");

        VBox left = new VBox(10, new Label("Nevoi din orasul tau (fara ale tale):"), tblNevoi, btnPreia, errAjut);
        VBox right = new VBox(10, new Label("Faptele tale bune (se actualizeaza in timp real):"), tblFapte);

        HBox content = new HBox(10, left, right);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        left.setPrefWidth(520);
        right.setPrefWidth(450);

        return new Tab("Doresc sa ajut!", content);
    }

    private void setupNevoiTable(TableView<Nevoie> tv, boolean selectable) {
        tv.getColumns().clear();

        TableColumn<Nevoie, String> cTitlu = new TableColumn<>("Titlu");
        cTitlu.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTitlu()));

        TableColumn<Nevoie, String> cDesc = new TableColumn<>("Descriere");
        cDesc.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDescriere()));

        TableColumn<Nevoie, String> cDeadline = new TableColumn<>("Deadline");
        cDeadline.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDeadline().format(DF)));

        TableColumn<Nevoie, String> cStatus = new TableColumn<>("Status");
        cStatus.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStatus().name()));

        tv.getColumns().addAll(cTitlu, cDesc, cDeadline, cStatus);

        if (selectable) {
            // Cerinta 6: daca EROU_GASIT nu mai poate fi selectata
            tv.setRowFactory(t -> new TableRow<>() {
                @Override
                protected void updateItem(Nevoie item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setDisable(false);
                        setStyle("");
                    } else {
                        boolean disabled = item.getOmSalvator() != null || item.getStatus().name().equals("EROU_GASIT");
                        setDisable(disabled);
                        setStyle(disabled ? "-fx-opacity: 0.5;" : "");
                    }
                }
            });
        }
    }

    private void onPreia() {
        errAjut.setText("");
        Nevoie sel = tblNevoi.getSelectionModel().getSelectedItem();
        if (sel == null) {
            errAjut.setText("Selecteaza o nevoie.");
            return;
        }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmare");
        a.setHeaderText("Vrei sa preiei nevoia: " + sel.getTitlu() + "?");
        a.setContentText("Aceasta nevoie iti va fi atribuita.");
        var res = a.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                service.preiaNevoie(sel.getId(), user);
            } catch (Exception ex) {
                errAjut.setText(ex.getMessage());
            }
        }
    }

    private Tab tabFiuAjutat() {
        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10);

        TextField tfTitlu = new TextField();
        TextArea taDesc = new TextArea();
        taDesc.setPrefRowCount(4);
        DatePicker dp = new DatePicker();

        form.add(new Label("Titlu:"), 0, 0);
        form.add(tfTitlu, 1, 0);

        form.add(new Label("Descriere:"), 0, 1);
        form.add(taDesc, 1, 1);

        form.add(new Label("Deadline:"), 0, 2);
        form.add(dp, 1, 2);

        GridPane.setHgrow(tfTitlu, Priority.ALWAYS);
        GridPane.setHgrow(taDesc, Priority.ALWAYS);

        Button add = new Button("Adauga nevoie");
        add.setOnAction(e -> {
            errAdd.setText("");
            try {
                service.adaugaNevoie(user, tfTitlu.getText(), taDesc.getText(), dp.getValue());
                tfTitlu.clear(); taDesc.clear(); dp.setValue(null);
            } catch (Exception ex) {
                errAdd.setText(ex.getMessage());
            }
        });

        errAdd.setStyle("-fx-text-fill:red;");

        VBox content = new VBox(10,
                new Label("Doresc sa fiu ajutat! (adauga o nevoie)"),
                form,
                add,
                errAdd
        );
        content.setPadding(new Insets(10));
        return new Tab("Doresc sa fiu ajutat!", content);
    }

    private void reloadAll() {
        tblNevoi.setItems(observableArrayList(service.getNevoiPentruOrasAjutor(user)));
        tblFapte.setItems(observableArrayList(service.getFapteBunePentruUser(user)));
    }

    @Override
    public void update(AppEvent e) {
        // Cerinta 7 + 9: update in timp real pentru toti
        Platform.runLater(this::reloadAll);
    }

    public void show() {
        stage.show();
    }
}
