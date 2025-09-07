//
// Created by Raul on 24.06.2025.
//
#include "main_window.h"
#include <QMessageBox>

MainWindow::MainWindow(ServiceTask &service, QWidget *parent) : QMainWindow(parent), service(service) {
    initGUI();
    connectSignals();
    service.addObserver(this);
    createStatusWindows();
    update();
}

void MainWindow::initGUI() {
    QWidget* central = new QWidget(this);
    setCentralWidget(central);

    auto layout = new QVBoxLayout();

    table = new QTableWidget;
    table->setColumnCount(4);
    table->setHorizontalHeaderLabels({"Id", "Descriere", "Stare", "Programatori"});
    layout->addWidget(table);

    auto formLayout = new QHBoxLayout;
    idEdit = new QLineEdit;
    descEdit = new QLineEdit;
    stateEdit = new QLineEdit;
    progEdit = new QLineEdit;
    formLayout->addWidget(new QLabel("Id"));
    formLayout->addWidget(idEdit);
    formLayout->addWidget(new QLabel("Descriere"));
    formLayout->addWidget(descEdit);
    formLayout->addWidget(new QLabel("Stare"));
    formLayout->addWidget(stateEdit);
    formLayout->addWidget(new QLabel("Programatori"));
    formLayout->addWidget(progEdit);
    layout->addLayout(formLayout);

    addBtn = new QPushButton("Adauga task");
    layout->addWidget(addBtn);

    searchEdit = new QLineEdit;
    searchEdit->setPlaceholderText("Cauta dupa programator");
    layout->addWidget(searchEdit);

    central->setLayout(layout);
}

void MainWindow::connectSignals() {
    QObject::connect(addBtn, &QPushButton::clicked, this, [this]() {
        bool ok;
        int id = idEdit->text().toInt(&ok);
        if (!ok) {
            QMessageBox::warning(this, "Eroare", "ID invalid!");
            return;
        }
        QString descriere = descEdit->text();
        QString stare = stateEdit->text();
        QStringList progList = progEdit->text().split(';', Qt::SkipEmptyParts);
        std::vector<QString> programatori(progList.begin(), progList.end());

        try {
            service.adaugaTask(id, descriere, stare, programatori);
            idEdit->clear();
            descEdit->clear();
            stateEdit->clear();
            progEdit->clear();
        } catch (const std::exception &e) {
            QMessageBox::critical(this, "Eroare", e.what());
        }
    });

    QObject::connect(searchEdit, &QLineEdit::textChanged, this, [this]() {
        QString searchText = searchEdit->text();
        auto tasks = service.filterByProgramatori(searchText);
        loadTable(tasks);
    });
}

void MainWindow::update() {
    loadTable(service.getAllSorted());
}

void MainWindow::loadTable(const std::vector<Task> &tasks) {
    table->clearContents();
    table->setRowCount((int)tasks.size());
    int row = 0;
    for (const auto& t : tasks) {
        table->setItem(row, 0, new QTableWidgetItem(QString::number(t.getId())));
        table->setItem(row, 1, new QTableWidgetItem(t.getDescriere()));
        table->setItem(row, 2, new QTableWidgetItem(t.getStare()));
        QStringList progList;
        for (const auto& prog : t.getProgramatori()) {
            progList.append(prog);
        }
        table->setItem(row, 3, new QTableWidgetItem(progList.join("; ")));
        row++;
    }
}

void MainWindow::createStatusWindows() {
    winOpen = new StatusWindow(service, "open");
    winInProgress = new StatusWindow(service, "inprogress");
    winClosed = new StatusWindow(service, "closed");

    winOpen->show();
    winInProgress->show();
    winClosed->show();
}

MainWindow::~MainWindow() {
    delete winOpen;
    delete winInProgress;
    delete winClosed;
}