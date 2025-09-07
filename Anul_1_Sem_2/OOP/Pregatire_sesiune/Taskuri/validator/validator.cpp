//
// Created by Raul on 24.06.2025.
//
#include <QList>
#include "validator.h"

void Validator::valideaza(const Task &task) {
    if (task.getDescriere().isEmpty())
        throw std::runtime_error("Descrierea nu poate fi vida");

    const auto& programatori = task.getProgramatori();
    if (programatori.size() < 1 || programatori.size() > 4)
        throw std::runtime_error("Numarul de programatori trebuie sa fie intre 1 si 4");

    QStringList stariValide = {"open", "inprogress", "closed"};
    if (!stariValide.contains(task.getStare()))
        throw std::runtime_error("Starea trebuie sa fie una din: open, inprogress, closed");
}