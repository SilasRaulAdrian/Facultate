//
// Created by Raul on 18.05.2025.
//

#ifndef PC_MAIN_WINDOW_H
#define PC_MAIN_WINDOW_H

#include <QMainWindow>
#include <QListWidget>
#include <QLineEdit>
#include <QPushButton>
#include <QLabel>
#include "../service/pc_service.h"

class MainWindow : public QWidget {
    Q_OBJECT

private:
    PCService &service;
    QListWidget* listProcesoare;
    QListWidget* listPlaci;
    QLineEdit* txtNume;
    QLineEdit* txtSoclu;
    QLineEdit* txtPret;
    QPushButton* btnAdauga;
    QLabel* lblPretTotal;

    void initGUI();
    void loadProcesoare(const std::vector<Procesor>& procesoare);
    void loadPlaci(const std::vector<PlacaDeBaza>& placi);
    void connectSignalsSlots();

private slots:
    void handleAdauga();
    void handleSelectProcesor();
    void handleCalculeazaPret();

public:
    MainWindow(PCService& service, QWidget *parent = nullptr);
};

#endif //PC_MAIN_WINDOW_H
