//
// Created by Raul on 18.05.2025.
//

#ifndef GAMINE_STORE_MAIN_WINDOW_H
#define GAMINE_STORE_MAIN_WINDOW_H

#include "../service/joc_service.h"
#include <QWidget>
#include <QTableWidget>
#include <QLabel>
#include <QPushButton>

class MainWindow : public QWidget {
    Q_OBJECT

private:
    JocService &service;

    QTableWidget *tabelJocuri;
    QLabel *lblPegi;
    QPushButton *btnSortByPret;
    QPushButton *btnJocuriCopii;
    QPushButton *btnRefresh;

    void initGUI();
    void loadJocuri(const std::vector<Joc>& jocuri);
    void connectSignalsSlots();

public:
    MainWindow(JocService &service, QWidget *parent = nullptr);
};

#endif //GAMINE_STORE_MAIN_WINDOW_H
