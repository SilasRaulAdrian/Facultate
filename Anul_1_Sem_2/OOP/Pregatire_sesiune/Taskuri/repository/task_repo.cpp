//
// Created by Raul on 24.06.2025.
//
#include <QFile>
#include <QTextStream>
#include "task_repo.h"

RepoTask::RepoTask(const QString &filename) : filename(filename) {
    loadFromFile();
}

void RepoTask::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.\n");
    }
    QTextStream in(&file);

    while(!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 4) continue;

        int id = tokens[0].toInt();
        QString descriere = tokens[1];
        QString stare = tokens[2];
        QStringList programatoriList = tokens[3].split(';');

        std::vector<QString> programatori;
        for (const auto& prog : programatoriList) {
            if (!prog.isEmpty()) {
                programatori.push_back(prog);
            }
        }

        taskuri.emplace_back(id, descriere, stare, programatori);
    }

    file.close();
}

void RepoTask::saveToFile() {
    QFile file(filename);
    if (!file.open(QIODevice::WriteOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul pentru scriere.\n");
    }
    QTextStream out(&file);

    for (const auto &task : taskuri) {
        out << task.getId() << ","
            << task.getDescriere() << ","
            << task.getStare() << ",";

        QStringList programatoriList;
        for (const auto& prog : task.getProgramatori()) {
            programatoriList.push_back(prog);
        }

        out << programatoriList.join(';') << "\n";
    }

    file.close();
}

const std::vector<Task> &RepoTask::getAll() const {
    return taskuri;
}

void RepoTask::adauga(const Task &task) {
    taskuri.push_back(task);
    saveToFile();
}

void RepoTask::updateStare(int id, const QString &newStare) {
    for (auto& task : taskuri)
        if (task.getId() == id) {
            task.setStare(newStare);
            break;
        }
    saveToFile();
}