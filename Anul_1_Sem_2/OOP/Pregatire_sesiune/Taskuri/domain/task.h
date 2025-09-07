//
// Created by Raul on 24.06.2025.
//

#ifndef TASKURI_TASK_H
#define TASKURI_TASK_H

#include <vector>
#include <QString>

class Task {
private:
    int id;
    QString descriere;
    QString stare;
    std::vector<QString> programatori;
public:
    Task(int id, const QString& descriere, const QString& stare, const std::vector<QString>& programatori);
    int getId() const;
    QString getDescriere() const;
    QString getStare() const;
    std::vector<QString> getProgramatori() const;
    void setStare(const QString& newStare);
};

#endif //TASKURI_TASK_H
