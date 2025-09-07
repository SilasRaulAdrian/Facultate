//
// Created by Raul on 16.05.2025.
//

#ifndef DEVICES_MAIN_WINDOW_H
#define DEVICES_MAIN_WINDOW_H

#include "../service/device_service.h"
#include <QWidget>
#include <QListWidget>
#include <QLineEdit>
#include <QPushButton>

class MainWindow : public QWidget {
    Q_OBJECT

private:
    DeviceService &service;

    QListWidget* listWidget;
    QLineEdit* modelLineEdit;
    QLineEdit* anLineEdit;

    QPushButton* sortModelBtn;
    QPushButton* sortPretBtn;
    QPushButton* sortResetBtn;

    void initGUI();
    void loadList(const std::vector<Device>& devices);
    void connectSignalsSlots();

public:
    MainWindow(DeviceService& service, QWidget* parent = nullptr);
};

#endif //DEVICES_MAIN_WINDOW_H
