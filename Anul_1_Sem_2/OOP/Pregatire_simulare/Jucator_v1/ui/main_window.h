//
// Created by Raul on 19.05.2025.
//

#ifndef JUCATOR_V1_MAIN_WINDOW_H
#define JUCATOR_V1_MAIN_WINDOW_H

#include "../service/jucator_service.h"
#include <QMainWindow>
#include <QListWidget>
#include <QLineEdit>
#include <QPushButton>
#include <QRadioButton>
#include <QSlider>
#include <QLabel>

class MainWindow : public QMainWindow {
    Q_OBJECT
private:
    JucatorService &service;

    QListWidget* listWidget;
    QLineEdit* lineNume;
    QLineEdit* linePuncte;

    QRadioButton* radioFinala;
    QRadioButton* radioSemi;
    QRadioButton* radioSfert;

    QPushButton* btnAdauga;
    QPushButton* btnRanking;
    QPushButton* btnElimina;

    QSlider* slider;
    QLabel* labelSlider;

    void setupUI();
    void populateList();

private slots:
    void onAdaugaPuncte();
    void onRecalculeaza();
    void onElimina();
    void onSliderChanged(int value);

public:
    MainWindow(JucatorService& service, QWidget* parent = nullptr);
};

#endif //JUCATOR_V1_MAIN_WINDOW_H
