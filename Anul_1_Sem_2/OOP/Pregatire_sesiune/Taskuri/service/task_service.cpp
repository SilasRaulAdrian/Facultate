//
// Created by Raul on 24.06.2025.
//
#include "task_service.h"

ServiceTask::ServiceTask(RepoTask &repo, Validator &val) : repo(repo), val(val) {}

const std::vector<Task> ServiceTask::getAllSorted() {
    auto tasks = repo.getAll();
    std::sort(tasks.begin(), tasks.end(), [](const Task &a, const Task &b){
        return a.getStare() < b.getStare();
    });
    return tasks;
}

void ServiceTask::adaugaTask(int id, const QString &descriere, const QString &stare,
                             const std::vector<QString> &programatori) {
    for (const auto& t : repo.getAll()) {
        if (t.getId() == id)
            throw std::runtime_error("Id duplicat");
    }

    Task task(id, descriere, stare, programatori);
    val.valideaza(task);
    repo.adauga(task);
    notify();
}

void ServiceTask::modificaStare(int id, const QString &newStare) {
    repo.updateStare(id, newStare);
    notify();
}

std::vector<Task> ServiceTask::filterByProgramatori(const QString &substr) {
    std::vector<Task> result;
    for (const auto& t : repo.getAll()) {
        for (const auto& prog : t.getProgramatori()) {
            if (prog.contains(substr, Qt::CaseInsensitive)) {
                result.push_back(t);
                break;
            }
        }
    }
    return result;
}