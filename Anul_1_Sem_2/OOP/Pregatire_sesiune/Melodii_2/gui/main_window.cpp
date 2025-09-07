#include <QMessageBox>
#include "main_window.h"

MainWindow::MainWindow(ServiceMelodii &s) : service(s) {
    initGUI();
    connectSignals();
}

void MainWindow::initGUI() {
    auto central = new QWidget;
    auto mainLayout = new QVBoxLayout;

    model = new MelodieModel(service);
    table = new QTableView;
    table->setModel(model);
    model->actualizeazaModel();
    mainLayout->addWidget(table);

    auto form = new QFormLayout;
    txtTitlu = new QLineEdit;
    txtArtist = new QLineEdit;
    txtGen = new QLineEdit;
    form->addRow("Titlu:", txtTitlu);
    form->addRow("Artist:", txtArtist);
    form->addRow("Gen:", txtGen);
    mainLayout->addLayout(form);

    auto btns = new QHBoxLayout;
    btnAdd = new QPushButton("Adauga");
    btnDelete = new QPushButton("Sterge");
    btns->addWidget(btnAdd);
    btns->addWidget(btnDelete);
    mainLayout->addLayout(btns);

    circleWidget = new GenCircleWidget(service);
    mainLayout->addWidget(circleWidget);

    central->setLayout(mainLayout);
    setCentralWidget(central);
    setWindowTitle("Gestiune melodii");
}

void MainWindow::connectSignals() {
    QObject::connect(btnAdd, &QPushButton::clicked, this, [=](){
        try {
            service.adaugaMelodie(txtTitlu->text(), txtArtist->text(), txtGen->text());
            model->actualizeazaModel();
            circleWidget->update();
        } catch (std::exception& ex) {
            QMessageBox::warning(this, "Eroare", ex.what());
        }
    });

    QObject::connect(btnDelete, &QPushButton::clicked, this, [=](){
        auto sel = table->selectionModel()->selectedIndexes();
        if (sel.isEmpty()) return;
        int row = sel.at(0).row();
        int id = model->index(row, 0).data().toInt();
        service.stergeMelodie(id);
        model->actualizeazaModel();
        circleWidget->update();
    });
}