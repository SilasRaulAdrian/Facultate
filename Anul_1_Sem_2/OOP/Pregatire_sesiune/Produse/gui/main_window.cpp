#include "main_window.h"

MainWindow::MainWindow(ServiceProduse &srv, QWidget *parent) : QMainWindow(parent), service(srv) {
    initUI();
    connectSignals();

    auto tipuri = service.getTipuri();
    std::sort(tipuri.begin(), tipuri.end());
    tipuri.erase(std::unique(tipuri.begin(), tipuri.end()), tipuri.end());
    for (const auto& tip : tipuri) {
        TipWindow* w = new TipWindow(tip, service);
        tipWindows.push_back(w);
        w->resize(400, 100);
        w->show();
    }

    service.addObserver(this);
}

MainWindow::~MainWindow() {
    for (auto w : tipWindows)
        delete w;
}

void MainWindow::initUI() {
    QWidget* central = new QWidget(this);
    setCentralWidget(central);

    model = new ProdusModel(service);

    tableView = new QTableView(this);
    tableView->setModel(model);

    editId = new QLineEdit(this);
    editNume = new QLineEdit(this);
    editTip = new QLineEdit(this);
    editPret = new QLineEdit(this);

    btnAdd = new QPushButton("Adauga produs", this);

    sliderPret = new QSlider(Qt::Horizontal, this);
    sliderPret->setRange(1, 100);
    sliderPret->setValue(service.getPretFiltru());
    lblFilter = new QLabel(QString("Filtru pret: %1").arg(sliderPret->value()), this);

    QHBoxLayout* inputLayout = new QHBoxLayout();
    inputLayout->addWidget(new QLabel("ID:"));
    inputLayout->addWidget(editId);
    inputLayout->addWidget(new QLabel("Nume:"));
    inputLayout->addWidget(editNume);
    inputLayout->addWidget(new QLabel("Tip:"));
    inputLayout->addWidget(editTip);
    inputLayout->addWidget(new QLabel("Pret:"));
    inputLayout->addWidget(editPret);
    inputLayout->addWidget(btnAdd);

    QHBoxLayout* sliderLayout = new QHBoxLayout();
    sliderLayout->addWidget(lblFilter);
    sliderLayout->addWidget(sliderPret);

    QVBoxLayout* mainLayout = new QVBoxLayout();
    mainLayout->addWidget(tableView);
    mainLayout->addLayout(inputLayout);
    mainLayout->addLayout(sliderLayout);

    central->setLayout(mainLayout);
    setWindowTitle("Gestiune produse");
}

void MainWindow::connectSignals() {
    connect(btnAdd, &QPushButton::clicked, this, &MainWindow::onAddClicked);
    connect(sliderPret, &QSlider::valueChanged, this, &MainWindow::onSliderChanged);
}

void MainWindow::update() {
    model->update();
}

void MainWindow::onAddClicked() {
    bool okId, okPret;
    int id = editId->text().toInt(&okId);
    double pret = editPret->text().toDouble(&okPret);
    QString nume = editNume->text();
    QString tip = editTip->text();

    if (!okId || !okPret) {
        QMessageBox::warning(this, "Eroare validare", "Id sau pret invalid!");
        return;
    }

    try {
        std::vector<int> existingIds;
        for (const auto& p : service.getAllSortat())
            existingIds.push_back(p.getId());

        ValidatorProduse val;
        val.valideaza(id, nume, tip, pret, existingIds);

        service.adaugaProdus(id, nume, tip, pret);

        editId->clear();
        editNume->clear();
        editTip->clear();
        editPret->clear();
    } catch (Validator& v) {
        QMessageBox::warning(this, "Eroare validare", v.what());
    } catch (std::exception& ex) {
        QMessageBox::critical(this, "Eroare", ex.what());
    }
}

void MainWindow::onSliderChanged(int value) {
    lblFilter->setText(QString("Filtru pret: %1").arg(value));
    service.setPretFiltru(value);
}