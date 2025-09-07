//
// Created by Raul on 24.06.2025.
//

#ifndef TASKURI_TASK_SERVICE_H
#define TASKURI_TASK_SERVICE_H

#include "../repository/task_repo.h"
#include "../validator/validator.h"
#include "../observer/observable.h"
#include <vector>
#include <algorithm>

class ServiceTask : public Observable {
private:
    RepoTask& repo;
    Validator val;

public:
    ServiceTask(RepoTask& repo, Validator& val);
    const std::vector<Task> getAllSorted();
    void adaugaTask(int id, const QString& descriere, const QString& stare, const std::vector<QString>& programatori);
    void modificaStare(int id, const QString& newStare);
    std::vector<Task> filterByProgramatori(const QString& substr);
};

#endif //TASKURI_TASK_SERVICE_H
