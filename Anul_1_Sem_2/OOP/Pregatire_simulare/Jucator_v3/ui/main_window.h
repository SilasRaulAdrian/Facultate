#ifndef JUCATOR_V1_MAIN_WINDOW_H
#define JUCATOR_V1_MAIN_WINDOW_H

#include "../service/jucator_service.h"
#include <QMainWindow>
#include <QListWidget>
#include <QLineEdit>
#include <QPushButton>
#include <QSpinBox>
#include <QCheckBox>
#include <QLabel>

class MainWindow : public QMainWindow {
Q_OBJECT
private:
    JucatorService &service;

    QListWidget* listWidget;
    QLineEdit* lineNume;
    QLineEdit* lineTara;
    QSpinBox* spinRank;

    QPushButton* btnAdauga;
    QPushButton* btnSort;
    QCheckBox* checkQualified;

    void setupUI();
    void populateList();
    void colorQualifiedPlayers();

private slots:
    void onAdaugaJucator();
    void onSortPlayers();
    void onQualifiedChanged(int state);

public:
    MainWindow(JucatorService& service, QWidget* parent = nullptr);
};

#endif //JUCATOR_V1_MAIN_WINDOW_H