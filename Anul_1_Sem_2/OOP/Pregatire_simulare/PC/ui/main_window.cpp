#include "main_window.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>

MainWindow::MainWindow(PCService& service, QWidget *parent) : service(service), QWidget(parent) {
    initGUI();
    loadProcesoare(service.getAllProcesoare());
    loadPlaci(service.getAllPlaciDeBaza());
    connectSignalsSlots();
}

void MainWindow::initGUI() {
    QVBoxLayout *mainLayout = new QVBoxLayout(this);

    listProcesoare = new QListWidget;
    listPlaci = new QListWidget;
    mainLayout->addWidget(listProcesoare);
    mainLayout->addWidget(listPlaci);

    txtNume = new QLineEdit;
    txtSoclu = new QLineEdit;
    txtPret = new QLineEdit;
    btnAdauga = new QPushButton("Adauga placa de baza");
    lblPretTotal = new QLabel("Pret total: ");

    auto *detailLayout = new QHBoxLayout;
    detailLayout->addWidget(new QLabel("Nume placa:"));
    detailLayout->addWidget(txtNume);
    detailLayout->addWidget(new QLabel("Soclu placa"));
    detailLayout->addWidget(txtSoclu);
    detailLayout->addWidget(new QLabel("Pret placa"));
    detailLayout->addWidget(txtPret);
    detailLayout->addWidget(btnAdauga);
    detailLayout->addWidget(lblPretTotal);

    mainLayout->addLayout(detailLayout);
}

void MainWindow::loadProcesoare(const std::vector<Procesor> &procesoare) {
    listProcesoare->clear();
    for (const auto& p : procesoare)
        listProcesoare->addItem(p.getNume() + " - " + QString::number(p.getNumarThreaduri()) + " threaduri");
}

void MainWindow::loadPlaci(const std::vector<PlacaDeBaza> &placi) {
    listPlaci->clear();
    for (const auto& p : placi)
        listPlaci->addItem(p.getNume());
}

void MainWindow::handleAdauga() {
    QString nume = txtNume->text();
    QString soclu = txtSoclu->text();
    int pret = txtPret->text().toInt();

    service.adaugaPlaca(nume, soclu, pret);
    std::vector<PlacaDeBaza> placi = service.getAllPlaciDeBaza();
    loadPlaci(placi);
}

void MainWindow::handleSelectProcesor() {
    int index = listProcesoare->currentRow();
    if (index < 0) return;
    auto soclu = service.getAllProcesoare()[index].getSocluProcesor();
    auto placiFiltrate = service.filterBySoclu(soclu);
    listPlaci->clear();
    for (const auto& pb : placiFiltrate)
        listPlaci->addItem(pb.getNume());
}

void MainWindow::handleCalculeazaPret() {
    int indexP = listProcesoare->currentRow();
    int indexPB = listPlaci->currentRow();
    if (indexP < 0 || indexPB < 0) return;

    auto p = service.getAllProcesoare()[indexP];
    auto soclu = p.getSocluProcesor();
    auto placiFiltrate = service.filterBySoclu(soclu);
    if (indexPB >= placiFiltrate.size()) return;

    auto pb = placiFiltrate[indexPB];
    int total = service.pretTotal(p, pb);
    lblPretTotal->setText("Pret total: " + QString::number(total));
}

void MainWindow::connectSignalsSlots() {
    connect(listProcesoare, &QListWidget::itemClicked, this, &MainWindow::handleSelectProcesor);
    connect(btnAdauga, &QPushButton::clicked, this, &MainWindow::handleAdauga);
    connect(listPlaci, &QListWidget::itemClicked, this, &MainWindow::handleCalculeazaPret);
}