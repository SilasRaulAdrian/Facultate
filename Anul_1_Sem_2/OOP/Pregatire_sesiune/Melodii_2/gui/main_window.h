#ifndef MELODII_2_MAIN_WINDOW_H
#define MELODII_2_MAIN_WINDOW_H

#include <QMainWindow>
#include <QTableView>
#include <QPushButton>
#include <QLineEdit>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QFormLayout>
#include "melodie_model.h"
#include "gen_circle_widget.h"

class MainWindow : public QMainWindow {
    Q_OBJECT
private:
    ServiceMelodii& service;
    MelodieModel* model;
    QTableView* table;
    QLineEdit *txtTitlu, *txtArtist, *txtGen;
    QPushButton *btnAdd, *btnDelete;
    GenCircleWidget* circleWidget;

public:
    /**
     * Constructorul clasei
     * @param s ServiceMelodii
     */
    MainWindow(ServiceMelodii& s);

    /**
     * Functie care initializeaza GUI-ul
     */
    void initGUI();

    /**
     * Functie care conecteaza semnalele si sloturile
     */
    void connectSignals();
};


#endif //MELODII_2_MAIN_WINDOW_H
