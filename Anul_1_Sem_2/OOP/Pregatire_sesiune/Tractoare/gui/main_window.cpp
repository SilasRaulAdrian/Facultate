#include "main_window.h"
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QPushButton>
#include <QMessageBox>

MainWindow::MainWindow(ServiceTractor& service, QWidget* parent)
    : QMainWindow(parent), service(service) {
    initGUI();
    connectSignals();
}

void MainWindow::initGUI() {
    auto central = new QWidget;
    auto vLayout = new QVBoxLayout(central);

    model = new TractorModel(service, this);
    table = new QTableView;
    table->setModel(model);
    vLayout->addWidget(table);

    auto hLayout = new QHBoxLayout;
    idEdit = new QLineEdit; idEdit->setPlaceholderText("ID");
    denEdit = new QLineEdit; denEdit->setPlaceholderText("Denumire");
    tipEdit = new QLineEdit; tipEdit->setPlaceholderText("Tip");
    rotiEdit = new QLineEdit; rotiEdit->setPlaceholderText("Nr roti");
    btnAdd = new QPushButton("Adaugă");
    hLayout->addWidget(idEdit);
    hLayout->addWidget(denEdit);
    hLayout->addWidget(tipEdit);
    hLayout->addWidget(rotiEdit);
    hLayout->addWidget(btnAdd);
    vLayout->addLayout(hLayout);

    tipFilter = new QComboBox;
    tipFilter->addItem("");
    for (auto& t : service.getDistinctTypes())
        tipFilter->addItem(t);
    vLayout->addWidget(tipFilter);

    wheels = new WheelsWidget;
    wheels->setFixedHeight(100);
    vLayout->addWidget(wheels);

    setCentralWidget(central);
}

void MainWindow::connectSignals() {
    connect(btnAdd, &QPushButton::clicked, this, [&]() {
        try {
            int id = idEdit->text().toInt();
            QString denumire = denEdit->text();
            QString tip = tipEdit->text();
            int numarRoti = rotiEdit->text().toInt();
            service.adaugaTractor(id, denumire, tip, numarRoti);
            model->reload();
            idEdit->clear(); denEdit->clear(); tipEdit->clear(); rotiEdit->clear();
        } catch (const std::runtime_error& e) {
            QMessageBox::warning(this, "Eroare", e.what());
        }
    });

    connect(tipFilter, &QComboBox::currentTextChanged, this, [&](const QString& tip) {
        model->setHighlightedTip(tip);
    });

    connect(table->selectionModel(), &QItemSelectionModel::currentChanged,
            this, [&](const QModelIndex& current, const QModelIndex&) {
                if (!current.isValid()) return;
                Tractor t = model->getTractorAt(current.row());
                wheels->setRoti(t.getNumarRoti(), t.getId());
            });

    connect(wheels, &WheelsWidget::clickedOnWheel, this, [&](int id) {
        service.decrementRoti(id);
        model->reload();

        auto tractor = service.getById(id);
        wheels->setRoti(tractor.getNumarRoti(), id);
    });
}