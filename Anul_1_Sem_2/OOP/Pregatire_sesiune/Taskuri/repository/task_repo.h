//
// Created by Raul on 24.06.2025.
//

#ifndef TASKURI_TASK_REPO_H
#define TASKURI_TASK_REPO_H

#include <QString>
#include <vector>
#include "../domain/task.h"

class RepoTask {
private:
    std::vector<Task> taskuri;
    QString filename;

    void loadFromFile();
    void saveToFile();

public:
    RepoTask(const QString& filename);
    const std::vector<Task>& getAll() const;
    void adauga(const Task& task);
    void updateStare(int id, const QString& newStare);
};

#endif //TASKURI_TASK_REPO_H
