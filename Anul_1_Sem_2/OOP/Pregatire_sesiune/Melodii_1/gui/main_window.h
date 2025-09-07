//
// Created by Raul on 23.06.2025.
//

#ifndef MELODII_1_MAIN_WINDOW_H
#define MELODII_1_MAIN_WINDOW_H

#include <QMainWindow>
#include <QTableView>
#include <QLineEdit>
#include <QSlider>
#include <QPushButton>
#include <QLabel>
#include <QVBoxLayout>
#include <QPainter>
#include "../service/melodie_service.h"
#include "melodie_model.h"

class MainWindow : public QMainWindow {
    Q_OBJECT

private:
    ServiceMelodii &service;
    MelodiiModel *model;

    QWidget* centralWidget;
    QTableView* table;
    QLineEdit* editTitlu;
    QSlider* sliderRank;
    QPushButton* btnUpdate;
    QPushButton* btnDelete;
    QWidget* rankBars;
    QLabel* labelRankValue;

    void initGUI();
    void connectSignalsSlots();
    void updateFieldsFromSelection();
    void updateBarChart();

public:
    explicit MainWindow(ServiceMelodii& srv, QWidget* parent = nullptr);
};

#endif //MELODII_1_MAIN_WINDOW_H
