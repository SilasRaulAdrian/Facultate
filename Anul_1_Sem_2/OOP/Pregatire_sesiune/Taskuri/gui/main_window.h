//
// Created by Raul on 24.06.2025.
//

#ifndef TASKURI_MAIN_WINDOW_H
#define TASKURI_MAIN_WINDOW_H

#include <QWidget>
#include <QTableWidget>
#include <QLineEdit>
#include <QPushButton>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QLabel>
#include <QMainWindow>
#include "../service/task_service.h"
#include "status_window.h"

class MainWindow : public QMainWindow, public Observer {
    Q_OBJECT
private:
    ServiceTask& service;
    QTableWidget* table;
    QLineEdit* idEdit, *descEdit, *progEdit, *stateEdit, *searchEdit;
    QPushButton* addBtn;

    StatusWindow* winOpen;
    StatusWindow* winInProgress;
    StatusWindow* winClosed;

    void initGUI();
    void loadTable(const std::vector<Task>& tasks);
    void connectSignals();
    void createStatusWindows();

public:
    MainWindow(ServiceTask& service, QWidget* parent = nullptr);
    ~MainWindow();
    void update() override;
};

#endif //TASKURI_MAIN_WINDOW_H
