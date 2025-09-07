#include "main_window.h"
#include <QVBoxLayout>
#include <QFormLayout>
#include <QMessageBox>
#include <QGroupBox>

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
    linePuncte = new QLineEdit;
    form->addRow("Nume jucator:", lineNume);
    form->addRow("Puncte turneu:", linePuncte);
    layout->addLayout(form);

    radioFinala = new QRadioButton("Finala");
    radioSemi = new QRadioButton("Semifinala");
    radioSfert = new QRadioButton("Sferturi");
    radioSfert->setChecked(true);

    QHBoxLayout* radios = new QHBoxLayout;
    radios->addWidget(radioFinala);
    radios->addWidget(radioSemi);
    radios->addWidget(radioSfert);
    layout->addLayout(radios);

    btnAdauga = new QPushButton("Adauga puncte");
    btnRanking = new QPushButton("Recalculeaza ranking");
    layout->addWidget(btnAdauga);
    layout->addWidget(btnRanking);

    slider = new QSlider(Qt::Horizontal);
    slider->setMinimum(0);
    slider->setMaximum(10);
    labelSlider = new QLabel("0 jucatori de eliminat");
    layout->addWidget(labelSlider);
    layout->addWidget(slider);

    btnElimina = new QPushButton("Elimina jucatori");
    layout->addWidget(btnElimina);

    connect(btnAdauga, &QPushButton::clicked, this, &MainWindow::onAdaugaPuncte);
    connect(btnRanking, &QPushButton::clicked, this, &MainWindow::onRecalculeaza);
    connect(btnElimina, &QPushButton::clicked, this, &MainWindow::onElimina);
    connect(slider, &QSlider::valueChanged, this, &MainWindow::onSliderChanged);

    central->setLayout(layout);
    setCentralWidget(central);
}

void MainWindow::populateList() {
    listWidget->clear();
    const auto& jucatori = service.getAll();
    for (const auto& j : jucatori) {
        QString text = QString("%1 (%2) - %3 pct - Rk: %4").arg(j.getNume()).arg(j.getTara()).arg(j.getPuncte()).arg(j.getRanking());
        QListWidgetItem *item = new QListWidgetItem(text);
        listWidget->addItem(item);
    }

    slider->setMaximum(jucatori.size());
    labelSlider->setText(QString("%1 jucatori de eliminat").arg(slider->value()));
}

void MainWindow::onAdaugaPuncte() {
    QString nume = lineNume->text();
    bool ok;
    int puncte = linePuncte->text().toInt(&ok);

    if (!ok) {
        QMessageBox::warning(this, "Eroare", "Introduceti un numar valid de puncte!");
        return;
    }

    QString tip;
    if (radioFinala->isChecked()) tip = "Finala";
    else if (radioSemi->isChecked()) tip = "Semifinala";
    else tip = "Sferturi";

    try {
        service.adaugaPuncte(nume, puncte, tip);
        populateList();
    } catch (std::exception& e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
}

void MainWindow::onRecalculeaza() {
    service.recalculeazaRanking();
    populateList();
}

void MainWindow::onElimina() {
    int m = slider->value();
    try {
        service.eliminaUltimii(m);
        populateList();
    } catch (std::exception& e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
}

void MainWindow::onSliderChanged(int value) {
    labelSlider->setText(QString("%1 jucatori de eliminat").arg(value));
}