//
// Created by Raul on 24.06.2025.
//
#include "main_window.h"
#include <QHBoxLayout>
#include <QMessageBox>

MainWindow::MainWindow(ServiceStudent& srv, QWidget* parent)
        : QMainWindow(parent), service(srv)
{
    initGUI();
    connectSignals();
    refreshModel();
}

void MainWindow::initGUI() {
    QWidget* central = new QWidget;
    setCentralWidget(central);

    auto layout = new QVBoxLayout(central);

    model = new StudentModel(service);
    tableView = new QTableView;
    tableView->setModel(model);
    tableView->setSelectionBehavior(QAbstractItemView::SelectRows);
    tableView->setSelectionMode(QAbstractItemView::MultiSelection);
    layout->addWidget(tableView);

    btnIntinerire = new QPushButton("Întinerire");
    btnImbatranire = new QPushButton("Îmbătrânire");
    btnSterge = new QPushButton("Șterge");
    btnUndo = new QPushButton("Undo");
    btnRedo = new QPushButton("Redo");

    auto btnLayout = new QHBoxLayout;
    btnLayout->addWidget(btnIntinerire);
    btnLayout->addWidget(btnImbatranire);
    btnLayout->addWidget(btnSterge);
    btnLayout->addWidget(btnUndo);
    btnLayout->addWidget(btnRedo);

    layout->addLayout(btnLayout);
}

void MainWindow::connectSignals() {
    connect(btnIntinerire, &QPushButton::clicked, [this](){
        service.intinerire();
        refreshModel();
    });

    connect(btnImbatranire, &QPushButton::clicked, [this](){
        service.imbatranire();
        refreshModel();
    });

    connect(btnUndo, &QPushButton::clicked, [this](){
        service.undo();
        refreshModel();
    });

    connect(btnRedo, &QPushButton::clicked, [this](){
        service.redo();
        refreshModel();
    });

    connect(btnSterge, &QPushButton::clicked, [this](){
        auto selected = tableView->selectionModel()->selectedRows();
        if (selected.empty()) return;

        std::vector<int> ids;
        for (const auto& index : selected) {
            int row = index.row();
            Student s = model->getStudent(row);
            ids.push_back(s.getNrMatricol());
        }

        service.stergeStudenti(ids);
        refreshModel();
    });
}

void MainWindow::refreshModel() {
    model->actualizeazaModel();
}