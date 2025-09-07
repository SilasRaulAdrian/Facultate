#include "main_window.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>

MainWindow::MainWindow(ApartamentService& service, QWidget* parent)
        : QWidget(parent), service(service) {
    initGUI();
    loadList(service.getAll());
    connectSignalsSlots();
}

void MainWindow::initGUI() {
    QVBoxLayout* mainLayout = new QVBoxLayout(this);

    listWidget = new QListWidget;
    mainLayout->addWidget(listWidget);

    QHBoxLayout* filterSurfaceLayout = new QHBoxLayout;
    minSuprafataEdit = new QLineEdit;
    maxSuprafataEdit = new QLineEdit;
    filterSuprafataBtn = new QPushButton("Filtrare Suprafata");
    filterSurfaceLayout->addWidget(new QLabel("Min Suprafata:"));
    filterSurfaceLayout->addWidget(minSuprafataEdit);
    filterSurfaceLayout->addWidget(new QLabel("Max Suprafata:"));
    filterSurfaceLayout->addWidget(maxSuprafataEdit);
    filterSurfaceLayout->addWidget(filterSuprafataBtn);
    mainLayout->addLayout(filterSurfaceLayout);

    QHBoxLayout* filterPretLayout = new QHBoxLayout;
    minPretEdit = new QLineEdit;
    maxPretEdit = new QLineEdit;
    filterPretBtn = new QPushButton("Filtrare Pret");
    filterPretLayout->addWidget(new QLabel("Min Pret:"));
    filterPretLayout->addWidget(minPretEdit);
    filterPretLayout->addWidget(new QLabel("Max Pret:"));
    filterPretLayout->addWidget(maxPretEdit);
    filterPretLayout->addWidget(filterPretBtn);
    mainLayout->addLayout(filterPretLayout);

    QHBoxLayout* buttonLayout = new QHBoxLayout;
    deleteBtn = new QPushButton("Sterge");
    buttonLayout->addWidget(deleteBtn);
    mainLayout->addLayout(buttonLayout);
}

void MainWindow::loadList(const std::vector<Apartament>& apartments) {
    listWidget->clear();
    for (const auto& ap : apartments) {
        QString text = ap.getStrada() + " | " +
                       QString::number(ap.getSuprafata()) + " m^2 | " +
                       QString::number(ap.getPret()) + " euro";
        listWidget->addItem(text);
    }
}

void MainWindow::connectSignalsSlots() {
    QObject::connect(deleteBtn, &QPushButton::clicked, [&]() {
        int index = listWidget->currentRow();
        if (index >= 0 && index < static_cast<int>(service.getAll().size())) {
            service.stergeApartament(index);
            loadList(service.getAll());
        }
    });

    QObject::connect(filterSuprafataBtn, &QPushButton::clicked, [&]() {
        int min = minSuprafataEdit->text().toInt();
        int max = maxSuprafataEdit->text().toInt();
        loadList(service.filterBySuprafata(min, max));
    });

    QObject::connect(filterPretBtn, &QPushButton::clicked, [&]() {
        int min = minPretEdit->text().toInt();
        int max = maxPretEdit->text().toInt();
        loadList(service.filterByPret(min, max));

        if (minSuprafataEdit->text() != "" && maxSuprafataEdit->text() != "") {
            int min = minSuprafataEdit->text().toInt();
            int max = maxSuprafataEdit->text().toInt();
            loadList(service.filterBySuprafata(min, max));
        }
    });
}