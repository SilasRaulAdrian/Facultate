//
// Created by Raul on 16.05.2025.
//

#ifndef DOCTORI_MAIN_WINDOW_H
#define DOCTORI_MAIN_WINDOW_H

#include "../service/doctor_service.h"
#include <QWidget>
#include <QListWidget>
#include <QLineEdit>
#include <QPushButton>

class MainWindow : public QWidget {
    Q_OBJECT

private:
    DoctorService &service;

    QListWidget *listWidget;
    QLineEdit *cnpLineEdit;
    QLineEdit *numeLineEdit;
    QLineEdit *sectieLineEdit;

    QPushButton *filterNumeBtn;
    QPushButton *filterSectieBtn;
    QPushButton *filterToateBtn;

    void initGUI();
    void loadList(const std::vector<Doctor>& doctori);
    void connectSignalsSlots();

public:
    MainWindow(DoctorService& service, QWidget* parent = nullptr);
};

#endif //DOCTORI_MAIN_WINDOW_H
