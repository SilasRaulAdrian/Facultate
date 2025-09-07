#include "main_window.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QColor>

MainWindow::MainWindow(DoctorService &service, QWidget *parent) : QWidget(parent), service(service) {
    initGUI();
    loadList(service.getAll());
    connectSignalsSlots();
}

void MainWindow::initGUI() {
    QVBoxLayout *layout = new QVBoxLayout(this);

    listWidget = new QListWidget;
    layout->addWidget(listWidget);

    QHBoxLayout *cnpLayout = new QHBoxLayout;
    cnpLayout->addWidget(new QLabel("CNP:"));
    cnpLineEdit = new QLineEdit;
    cnpLineEdit->setReadOnly(true);
    cnpLayout->addWidget(cnpLineEdit);
    layout->addLayout(cnpLayout);

    numeLineEdit = new QLineEdit;
    sectieLineEdit = new QLineEdit;
    numeLineEdit->setPlaceholderText("Nume: ");
    layout->addWidget(numeLineEdit);
    sectieLineEdit->setPlaceholderText("Sectie: ");
    layout->addWidget(sectieLineEdit);

    filterNumeBtn = new QPushButton("Filtreaza dupa nume");
    filterSectieBtn = new QPushButton("Filtreaza dupa sectie");
    filterToateBtn = new QPushButton("Toate");
    QHBoxLayout *btnLayout = new QHBoxLayout;
    btnLayout->addWidget(filterNumeBtn);
    btnLayout->addWidget(filterSectieBtn);
    btnLayout->addWidget(filterToateBtn);
    layout->addLayout(btnLayout);
}

void MainWindow::loadList(const std::vector<Doctor> &doctori) {
    listWidget->clear();
    for (const auto& d : doctori) {
        QString text = d.getNume() + " " + d.getPrenume() + " - " + d.getSectie();
        QListWidgetItem *item = new QListWidgetItem(text);

        int c = d.getConcediu();

        if (c == 0)
            item->setForeground(Qt::red);
        else
            item->setForeground(Qt::green);

        listWidget->addItem(item);
    }
}

void MainWindow::connectSignalsSlots() {
    QObject::connect(listWidget, &QListWidget::itemSelectionChanged, [&]() {
        int index = listWidget->currentRow();
        if (index >= 0 && index < static_cast<int>(service.getAll().size())) {
            const Doctor& d = service.getAll()[index];
            cnpLineEdit->setText(d.getCnp());
        }
    });

    QObject::connect(filterSectieBtn, &QPushButton::clicked, [&]() {
        loadList(service.filterBySectie(sectieLineEdit->text()));
    });

    QObject::connect(filterNumeBtn, &QPushButton::clicked, [&]() {
        loadList(service.filterByNume(numeLineEdit->text()));
    });

    QObject::connect(filterToateBtn, &QPushButton::clicked, [&]() {
        loadList(service.getAll());
    });
}