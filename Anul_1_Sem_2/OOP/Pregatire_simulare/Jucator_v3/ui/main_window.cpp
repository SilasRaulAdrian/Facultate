#include "main_window.h"
#include <QVBoxLayout>
#include <QFormLayout>
#include <QMessageBox>
#include <QGroupBox>
#include <QBrush>

MainWindow::MainWindow(JucatorService &service, QWidget *parent) : service(service), QMainWindow(parent) {
    setupUI();
    populateList();
}

void MainWindow::setupUI() {
    QWidget* central = new QWidget;
    QVBoxLayout* layout = new QVBoxLayout;

    listWidget = new QListWidget;
    layout->addWidget(listWidget);

    QFormLayout* form = new QFormLayout;
    lineNume = new QLineEdit;
    lineTara = new QLineEdit;
    spinRank = new QSpinBox;
    spinRank->setMinimum(1);
    spinRank->setMaximum(100);

    form->addRow("Nume jucator:", lineNume);
    form->addRow("Tara de origine:", lineTara);
    form->addRow("Rank:", spinRank);
    layout->addLayout(form);

    btnAdauga = new QPushButton("Adauga jucator");
    btnSort = new QPushButton("Sorteaza jucatori");
    layout->addWidget(btnAdauga);
    layout->addWidget(btnSort);

    checkQualified = new QCheckBox("Qualified");
    layout->addWidget(checkQualified);

    connect(btnAdauga, &QPushButton::clicked, this, &MainWindow::onAdaugaJucator);
    connect(btnSort, &QPushButton::clicked, this, &MainWindow::onSortPlayers);
    connect(checkQualified, &QCheckBox::stateChanged, this, &MainWindow::onQualifiedChanged);

    central->setLayout(layout);
    setCentralWidget(central);
}

void MainWindow::populateList() {
    listWidget->clear();
    const auto& jucatori = service.getAll();
    for (const auto& j : jucatori) {
        QString text = QString("%1 (%2) - %3 pct - Rk: %4")
                .arg(j.getNume())
                .arg(j.getTara())
                .arg(j.getPuncte())
                .arg(j.getRanking());
        QListWidgetItem *item = new QListWidgetItem(text);
        listWidget->addItem(item);
    }
    colorQualifiedPlayers();
}

void MainWindow::colorQualifiedPlayers() {
    if (!checkQualified->isChecked()) return;

    const auto& jucatori = service.getAll();
    for (int i = 0; i < jucatori.size(); ++i)
        if (jucatori[i].getPuncte() > 250)
            listWidget->item(i)->setBackground(Qt::green);
}

void MainWindow::onAdaugaJucator() {
    QString nume = lineNume->text().trimmed();
    QString tara = lineTara->text().trimmed();
    int rank = spinRank->value();

    if (nume.isEmpty() || tara.isEmpty()) {
        QMessageBox::warning(this, "Eroare", "Numele si tara nu pot fi goale!");
        return;
    }

    try {
        service.adaugaJucator(nume, tara, rank);
        populateList();

        lineNume->clear();
        lineTara->clear();
    } catch (std::exception &e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
}

void MainWindow::onSortPlayers() {
    service.sortByRank();
    populateList();
}

void MainWindow::onQualifiedChanged(int state) {
    Q_UNUSED(state);
    populateList();
}