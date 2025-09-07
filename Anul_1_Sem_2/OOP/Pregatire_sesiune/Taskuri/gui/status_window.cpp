//
// Created by Raul on 24.06.2025.
//
#include "status_window.h"

StatusWindow::StatusWindow(ServiceTask& srv, const QString& stat) : service(srv), status(stat) {
    initGUI();
    service.addObserver(this);
    update();
}

void StatusWindow::initGUI() {
    auto layout = new QVBoxLayout{this};
    list = new QListWidget;
    layout->addWidget(list);
    setWindowTitle("Status: " + status);
}

void StatusWindow::updateList() {
    list->clear();
    for (const auto& task : service.getAllSorted()) {
        if (task.getStare() == status) {
            QString itemText = QString::number(task.getId()) + ": " + task.getDescriere();
            list->addItem(itemText);
        }
    }
}

void StatusWindow::update() {
    updateList();
}