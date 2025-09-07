//
// Created by Raul on 21.05.2025.
//

#ifndef APARTAMENTE_MAIN_WINDOW_H
#define APARTAMENTE_MAIN_WINDOW_H

#include "../service/apartamente_service.h"
#include <QWidget>
#include <QListWidget>
#include <QLineEdit>
#include <QPushButton>

class MainWindow : public QWidget {
    Q_OBJECT

private:
    ApartamentService& service;

    QListWidget* listWidget;

    QLineEdit* minSuprafataEdit;
    QLineEdit* maxSuprafataEdit;
    QPushButton* filterSuprafataBtn;

    QLineEdit* minPretEdit;
    QLineEdit* maxPretEdit;
    QPushButton* filterPretBtn;

    QPushButton* deleteBtn;

    void initGUI();
    void loadList(const std::vector<Apartament>& apartments);
    void connectSignalsSlots();

public:
    MainWindow(ApartamentService& service, QWidget* parent = nullptr);
};

#endif //APARTAMENTE_MAIN_WINDOW_H
