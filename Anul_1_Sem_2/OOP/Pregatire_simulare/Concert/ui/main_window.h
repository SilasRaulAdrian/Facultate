//
// Created by Raul on 20.05.2025.
//

#ifndef CONCERT_MAIN_WINDOW_H
#define CONCERT_MAIN_WINDOW_H

#include "../service/concert_service.h"
#include <QMainWindow>
#include <QListWidget>
#include <QSpinBox>
#include <QPushButton>

class MainWindow : public QMainWindow {
Q_OBJECT
public:
    MainWindow(ConcertService& service, QWidget* parent = nullptr);

private slots:
    void onModificaClicked();
    void onCumparaClicked();
    void onSorteazaClicked();

private:
    ConcertService& service;
    QListWidget* listaConcerte;
    QSpinBox* spinMultiplicator;
    QPushButton* btnModifica;
    QPushButton* btnCumpara;
    QPushButton* btnSorteaza;

    void setupUI();
    void incarcaLista();
    void actualizeazaAfisareConcert(int index);
};

#endif //CONCERT_MAIN_WINDOW_H
