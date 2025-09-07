#ifndef TRACTOARE_MAIN_WINDOW_H
#define TRACTOARE_MAIN_WINDOW_H

#include <QMainWindow>
#include <QTableView>
#include <QPushButton>
#include <QLineEdit>
#include <QComboBox>
#include "wheels_widget.h"
#include "tractor_model.h"
#include "../service/tractor_service.h"

class MainWindow : public QMainWindow {
    Q_OBJECT
private:
    ServiceTractor& service;
    TractorModel* model;
    QTableView* table;
    QPushButton* btnAdd;
    QLineEdit* idEdit;
    QLineEdit* denEdit;
    QLineEdit* tipEdit;
    QLineEdit* rotiEdit;
    QComboBox* tipFilter;
    WheelsWidget* wheels;

public:
    /**
     * Constructorul clasei
     * @param service ServiceTractor
     * @param parent QWidget
     */
    MainWindow(ServiceTractor& service, QWidget* parent = nullptr);

    /**
     * Initializeaza GUI
     */
    void initGUI();

    /**
     * Conecteaza semnalele si sloturile
     */
    void connectSignals();
};

#endif //TRACTOARE_MAIN_WINDOW_H
