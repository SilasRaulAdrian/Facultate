//
// Created by Raul on 24.06.2025.
//
#include "task.h"

Task::Task(int id, const QString &descriere, const QString &stare, const std::vector<QString> &programatori) :
    id(id), descriere(descriere), stare(stare), programatori(programatori) {}

int Task::getId() const {
    return id;
}

QString Task::getDescriere() const {
    return descriere;
}

QString Task::getStare() const {
    return stare;
}

std::vector<QString> Task::getProgramatori() const {
    return programatori;
}

void Task::setStare(const QString &newStare) {
    stare = newStare;
}