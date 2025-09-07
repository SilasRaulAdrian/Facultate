#include "main_window.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QMessageBox>

MainWindow::MainWindow(ConcertService& service, QWidget* parent) : QMainWindow(parent), service(service) {
    setupUI();
    incarcaLista();
}

void MainWindow::setupUI() {
    QWidget* central = new QWidget;
    QVBoxLayout* layoutPrincipal = new QVBoxLayout;

    listaConcerte = new QListWidget;
    layoutPrincipal->addWidget(listaConcerte);

    QHBoxLayout* layoutModificare = new QHBoxLayout;
    spinMultiplicator = new QSpinBox;
    spinMultiplicator->setRange(1, 10);
    btnModifica = new QPushButton("Modifica bilete");
    layoutModificare->addWidget(spinMultiplicator);
    layoutModificare->addWidget(btnModifica);
    layoutPrincipal->addLayout(layoutModificare);

    btnCumpara = new QPushButton("Cumpara bilete");
    layoutPrincipal->addWidget(btnCumpara);

    btnSorteaza = new QPushButton("Sorteaza cronologic");
    layoutPrincipal->addWidget(btnSorteaza);

    connect(btnModifica, &QPushButton::clicked, this, &MainWindow::onModificaClicked);
    connect(btnCumpara, &QPushButton::clicked, this, &MainWindow::onCumparaClicked);
    connect(btnSorteaza, &QPushButton::clicked, this, &MainWindow::onSorteazaClicked);

    central->setLayout(layoutPrincipal);
    setCentralWidget(central);
    resize(600, 400);
}

void MainWindow::incarcaLista() {
    listaConcerte->clear();
    const auto& concerte = service.getAll();
    for (const auto& c : concerte) {
        QString text = QString("%1 | %2 | %3 bilete")
                .arg(c.getNumeFormatie())
                .arg(c.getDataString())
                .arg(c.getNrBilete());
        listaConcerte->addItem(text);
    }
}

void MainWindow::onModificaClicked() {
    int randSelectat = listaConcerte->currentRow();
    if (randSelectat == -1) {
        QMessageBox::warning(this, "Eroare", "Niciun concert selectat!");
        return;
    }

    int factor = spinMultiplicator->value();
    auto concert = service.getAll()[randSelectat];

    int bileteNoi = concert.getNrBilete() * factor;
    if (bileteNoi > 500) {
        QMessageBox::warning(this, "Eroare", "Nu se pot depasi 500 de bilete!");
        return;
    }

    service.modificaBilete(randSelectat, factor);
    actualizeazaAfisareConcert(randSelectat);
}

void MainWindow::onCumparaClicked() {
    int randSelectat = listaConcerte->currentRow();
    if (randSelectat == -1) {
        QMessageBox::warning(this, "Eroare", "Niciun concert selectat!");
        return;
    }

    auto concert = service.getAll()[randSelectat];
    if (concert.getNrBilete() < 1) {
        QMessageBox::warning(this, "Eroare", "Nu sunt suficiente bilete disponibile!");
        return;
    }

    service.cumparaBilete(randSelectat);
    actualizeazaAfisareConcert(randSelectat);

    if (service.getAll()[randSelectat].getNrBilete() == 0)
        listaConcerte->item(randSelectat)->setForeground(Qt::red);
}

void MainWindow::onSorteazaClicked() {
    service.sorteazaCronologic();
    incarcaLista();
}

void MainWindow::actualizeazaAfisareConcert(int index) {
    const auto& concert = service.getAll()[index];
    QString text = QString("%1 | %2 | %3 bilete")
            .arg(concert.getNumeFormatie())
            .arg(concert.getDataString())
            .arg(concert.getNrBilete());

    listaConcerte->item(index)->setText(text);

    listaConcerte->item(index)->setForeground(Qt::white);

    if (concert.getNrBilete() == 0) {
        listaConcerte->item(index)->setForeground(Qt::red);
    }
}