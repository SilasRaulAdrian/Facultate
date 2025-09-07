#include "cosReadOnlyGUI.h"
#include <cstdlib>
#include <QCloseEvent>

CosReadOnlyGUI::CosReadOnlyGUI(CarteService &service, QMainWindow *parent) : service(service), QMainWindow(parent), mainWindow(parent) {
    central = new QWidget(this);
    this->setCentralWidget(central);
    service.addObserver(this);
    resize(800, 600);
}

CosReadOnlyGUI::~CosReadOnlyGUI() {
    service.removeObserver(this);
}

void CosReadOnlyGUI::update() {
    repaint();
}

void CosReadOnlyGUI::paintEvent(QPaintEvent *event) {
    QPainter p(this);
    int count = service.getSizeCos();
    for (int i = 0; i < count; ++i) {
        int x = rand() % width();
        int y = rand() % height();
        p.drawEllipse(x, y, 30, 30);
    }
}

void CosReadOnlyGUI::closeEvent(QCloseEvent *event) {
    if (mainWindow) {
        mainWindow->show();
    }
    event->accept();
}