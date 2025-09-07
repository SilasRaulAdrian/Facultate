//
// Created by Raul on 24.06.2025.
//

#ifndef STUDENTI_211_MAIN_WINDOW_H
#define STUDENTI_211_MAIN_WINDOW_H

#include <QMainWindow>
#include <QPushButton>
#include <QVBoxLayout>
#include <QTableView>
#include <QItemSelectionModel>
#include "student_model.h"
#include "../service/student_service.h"

class MainWindow : public QMainWindow {
Q_OBJECT
private:
    ServiceStudent& service;
    StudentModel* model;

    QTableView* tableView;
    QPushButton* btnIntinerire;
    QPushButton* btnImbatranire;
    QPushButton* btnSterge;
    QPushButton* btnUndo;
    QPushButton* btnRedo;

    void initGUI();
    void connectSignals();

    void refreshModel();

public:
    MainWindow(ServiceStudent& srv, QWidget* parent = nullptr);
};

#endif //STUDENTI_211_MAIN_WINDOW_H
