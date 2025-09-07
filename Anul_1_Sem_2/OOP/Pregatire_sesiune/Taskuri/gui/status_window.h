//
// Created by Raul on 24.06.2025.
//

#ifndef TASKURI_STATUS_WINDOW_H
#define TASKURI_STATUS_WINDOW_H

#include <QWidget>
#include <QListWidget>
#include <QVBoxLayout>
#include "../service/task_service.h"

class StatusWindow : public QWidget, public Observer {
Q_OBJECT
private:
    ServiceTask& service;
    QString status;
    QListWidget* list;

    void initGUI();
    void updateList();

public:
    StatusWindow(ServiceTask& service, const QString& status);
    void update() override;
};

#endif //TASKURI_STATUS_WINDOW_H
