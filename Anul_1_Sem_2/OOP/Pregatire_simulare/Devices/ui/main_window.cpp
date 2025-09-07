#include "main_window.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QColor>

MainWindow::MainWindow(DeviceService &service, QWidget *parent) : QWidget(parent), service(service) {
    initGUI();
    loadList(service.getAll());
    connectSignalsSlots();
}

void MainWindow::initGUI() {
    QVBoxLayout *mainLayout = new QVBoxLayout(this);

    listWidget = new QListWidget;
    mainLayout->addWidget(listWidget);

    QHBoxLayout *detailLayout = new QHBoxLayout;
    modelLineEdit = new QLineEdit;
    anLineEdit = new QLineEdit;
    modelLineEdit->setReadOnly(true);
    anLineEdit->setReadOnly(true);
    detailLayout->addWidget(new QLabel("Model:"));
    detailLayout->addWidget(modelLineEdit);
    detailLayout->addWidget(new QLabel("An:"));
    detailLayout->addWidget(anLineEdit);
    mainLayout->addLayout(detailLayout);

    QHBoxLayout *buttonLayout = new QHBoxLayout;
    sortModelBtn = new QPushButton("Sortare Model");
    sortPretBtn = new QPushButton("Sortare Pret");
    sortResetBtn = new QPushButton("Nesortat");
    buttonLayout->addWidget(sortModelBtn);
    buttonLayout->addWidget(sortPretBtn);
    buttonLayout->addWidget(sortResetBtn);
    mainLayout->addLayout(buttonLayout);
}

void MainWindow::loadList(const std::vector<Device> &devices) {
    listWidget->clear();
    for (const auto& d : devices) {
        QString text = d.getModel() + " (" + QString::number(d.getPret()) + ")";
        QListWidgetItem *item = new QListWidgetItem(text);

        QString c = d.getCuloare().toLower();
        if (c == "rosu") item->setForeground(Qt::red);
        else if (c == "albastru") item->setForeground(Qt::blue);
        else if (c == "negru") item->setForeground(Qt::black);
        else if (c == "galben") item->setForeground(Qt::yellow);

        listWidget->addItem(item);
    }
}

void MainWindow::connectSignalsSlots() {
    QObject::connect(listWidget, &QListWidget::itemSelectionChanged, [&]() {
        int index = listWidget->currentRow();
        if (index >= 0 && index < static_cast<int>(service.getAll().size())) {
            const Device& d = service.getAll()[index];
            modelLineEdit->setText(d.getModel());
            anLineEdit->setText(QString::number(d.getAn()));
        }
    });

    QObject::connect(sortModelBtn, &QPushButton::clicked, [&]() {
        loadList(service.sortByModel());
    });

    QObject::connect(sortPretBtn, &QPushButton::clicked, [&]() {
        loadList(service.sortByPret());
    });

    QObject::connect(sortResetBtn, &QPushButton::clicked, [&]() {
        loadList(service.getAll());
    });
}