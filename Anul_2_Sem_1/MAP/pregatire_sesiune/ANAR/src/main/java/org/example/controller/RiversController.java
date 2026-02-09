package org.example.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.domain.River;
import org.example.service.AnarService;

import static javafx.collections.FXCollections.observableArrayList;

public class RiversController {
    private final AnarService service;
    private final Stage stage = new Stage();
    private final TableView<River> tv = new TableView<>();
    private final Label err = new Label();

    private final int limit = 10;
    private int offset = 0;

    private final Label pageLbl = new Label();

    public RiversController(AnarService service) {
        this.service = service;
        stage.setTitle("Rauri");

        setupTable();
        reload();

        TextField tfNewLevel = new TextField();
        tfNewLevel.setPrefWidth(120);

        Button btnUpdate = new Button("Update cota");
        btnUpdate.setOnAction(e -> {
            err.setText("");
            River sel = tv.getSelectionModel().getSelectedItem();
            if (sel == null) {
                err.setText("Selecteaza un rau");
                return;
            }

            try {
                double lvl = Double.parseDouble(tfNewLevel.getText().trim());
                service.updateRiverLevel(sel.getName(), lvl);
                tfNewLevel.clear();
                reload();
            } catch (Exception ex) {
                err.setText(ex.getMessage());
            }
        });

        Button btnPrev = new Button("< Prev");
        Button btnNext = new Button("Next >");

        btnPrev.setOnAction(e -> {
            offset = Math.max(0, offset - limit);
            reload();
        });

        btnNext.setOnAction(e -> {
            int oldOffset = offset;
            offset += limit;
            int size = reloadPageAndReturnSize();
            if (size == 0) {
                offset = oldOffset;
                reload();
            }
        });

        pageLbl.setStyle("-fx-font-weight: bold;");
        err.setStyle("-fx-text-fill: red");

        HBox pagingBar = new HBox(10, btnPrev, pageLbl, btnNext);
        pagingBar.setAlignment(Pos.CENTER_LEFT);

        HBox editBar = new HBox(10, new Label("Cota noua:"), tfNewLevel, btnUpdate);
        editBar.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(10, tv, pagingBar, editBar, err);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 750, 500));

        reload();
    }

    private void setupTable() {
        TableColumn<River, String> cName = new TableColumn<>("Nume");
        cName.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getName()));
        cName.setPrefWidth(250);

        TableColumn<River, Number> cLevel = new TableColumn<>("Cota medie");
        cLevel.setCellValueFactory(x -> new SimpleDoubleProperty(x.getValue().getAvgLevel()));
        cLevel.setPrefWidth(150);

        tv.getColumns().addAll(cName, cLevel);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void reload() {
        var page = service.getRiversPage(limit, offset);
        tv.setItems(observableArrayList(page));
        updatePageLabel(page.size());
    }

    private int reloadPageAndReturnSize() {
        var page = service.getRiversPage(limit, offset);
        tv.setItems(observableArrayList(page));
        updatePageLabel(page.size());
        return page.size();
    }

    private void updatePageLabel(int pageSize) {
        int pageIndex = (offset / limit) + 1;
        pageLbl.setText("Page " + pageIndex + " (items: " + pageSize + ")");
    }

    public void show() {
        stage.show();
    }
}
