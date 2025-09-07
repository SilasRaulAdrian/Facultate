#include <QInputDialog>
#include <QCloseEvent>
#include "cosCRUDGUI.h"

CosCRUDGUI::CosCRUDGUI(CarteService &service, QMainWindow *parent) : service(service), QMainWindow(parent), mainWindow(parent) {
    setUp();
}

void CosCRUDGUI::setUp() {
    central = new QWidget(this);
    this->setCentralWidget(central);
    resize(800, 600);
    model = new CosModel(service);
    lista = new QListView;
    lista->setModel(model);
    QFont font;
    font.setPointSizeF(12);
    lista->setFont(font);
    QVBoxLayout *layout = new QVBoxLayout;
    layout->addWidget(lista);

    QPushButton *btnGenereaza = new QPushButton("Genereaza Cos");
    layout->addWidget(btnGenereaza);
    QPushButton *btnGoleste = new QPushButton("Goleste Cos");
    layout->addWidget(btnGoleste);
    central->setLayout(layout);

    connect(btnGenereaza, &QPushButton::clicked, this, [&]() {
        bool ok;
        int numarCarti = QInputDialog::getInt(this, "Genereaza Cos", "Numar de carti:", 5, 1, 100, 1, &ok);
        if (ok) {
            service.genereazaCos(numarCarti, service.getCarti());
            model->update();
            emit cosModificat();
        }
    });

    connect(btnGoleste, &QPushButton::clicked, this, [&]() {
        service.golesteCos();
        model->update();
        emit cosModificat();
    });
}

CosCRUDGUI::~CosCRUDGUI() {
    service.removeObserver(this);
    delete model;
}

void CosCRUDGUI::update() {
    model->update();
}

void CosCRUDGUI::closeEvent(QCloseEvent *event) {
    if (mainWindow) {
        mainWindow->show();
    }
    event->accept();
}
