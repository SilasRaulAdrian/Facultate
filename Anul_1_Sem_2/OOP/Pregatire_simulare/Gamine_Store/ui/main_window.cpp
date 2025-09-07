#include "main_window.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QColor>

MainWindow::MainWindow(JocService &service, QWidget *parent) : service(service), QWidget(parent) {
    initGUI();
    loadJocuri(service.getAll());
    connectSignalsSlots();
}

void MainWindow::initGUI() {
    QVBoxLayout *mainLayout = new QVBoxLayout(this);

    tabelJocuri = new QTableWidget;
    tabelJocuri->setColumnCount(2);
    tabelJocuri->setHorizontalHeaderLabels({"Titlu", "Pret"});
    mainLayout->addWidget(tabelJocuri);

    QHBoxLayout *detailLayout = new QHBoxLayout;
    lblPegi = new QLabel("Varsta minima recomandata: ");
    detailLayout->addWidget(lblPegi);
    btnSortByPret = new QPushButton("Sortare Pret");
    btnJocuriCopii = new QPushButton("Jocuri recomandate pentru copii");
    btnRefresh = new QPushButton("Refresh");
    detailLayout->addWidget(btnSortByPret);
    detailLayout->addWidget(btnJocuriCopii);
    detailLayout->addWidget(btnRefresh);
    mainLayout->addLayout(detailLayout);
}

void MainWindow::loadJocuri(const std::vector<Joc> &jocuri) {
    tabelJocuri->clearContents();
    tabelJocuri->setColumnCount(2);
    tabelJocuri->setHorizontalHeaderLabels({"Titlu", "Pret"});
    tabelJocuri->setRowCount(0);

    for (const auto &j: jocuri) {
        int row = tabelJocuri->rowCount();
        tabelJocuri->insertRow(row);

        QTableWidgetItem *itemTitlu = new QTableWidgetItem(j.getTitlu());
        QTableWidgetItem *itemPret = new QTableWidgetItem(QString::number(j.getPret()));
        QTableWidgetItem *itemPlatforma = new QTableWidgetItem(j.getPlatforma());

        QColor color;
        QString p = j.getPlatforma().toLower();
        if (p == "pc") color = Qt::black;
        else if (p == "xbox") color = Qt::green;
        else if (p == "playstation") color = Qt::blue;
        else if (p == "nintendo") color = Qt::red;

        itemTitlu->setBackground(color);
        itemPret->setBackground(color);
        itemPlatforma->setBackground(color);

        tabelJocuri->setItem(row, 0, itemTitlu);
        tabelJocuri->setItem(row, 1, itemPret);
    }
}

void MainWindow::connectSignalsSlots() {
    QObject::connect(tabelJocuri, &QTableWidget::itemSelectionChanged, [&]() {
        int index = tabelJocuri->currentRow();
        if (index >= 0 && index < static_cast<int>(service.getAll().size())) {
            const Joc& j = service.getAll()[index];
            lblPegi->setText("Varsta minima recomandata: " + QString::number(j.getPegi()));
        }
    });

    QObject::connect(btnSortByPret, &QPushButton::clicked, [&]() {
        loadJocuri(service.sortByPret());
    });

    QObject::connect(btnJocuriCopii, &QPushButton::clicked, [&]() {
        loadJocuri(service.filterByPegi());
    });

    QObject::connect(btnRefresh, &QPushButton::clicked, [&]() {
        loadJocuri(service.getAll());
    });
}