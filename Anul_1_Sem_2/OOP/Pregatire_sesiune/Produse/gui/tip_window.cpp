#include "tip_window.h"

TipWindow::TipWindow(const QString &tip, ServiceProduse &srv, QWidget* parent) : QWidget(parent), tip(tip), service(srv) {
    setWindowTitle(tip);
    label = new QLabel(this);
    label->setAlignment(Qt::AlignCenter);

    QVBoxLayout* layout = new QVBoxLayout(this);
    layout->addWidget(label);
    setLayout(layout);

    service.addObserver(this);
    update();
}

void TipWindow::update() {
    int count = service.countByTip(tip);
    label->setText(QString("Numar produse %1").arg(count));
}