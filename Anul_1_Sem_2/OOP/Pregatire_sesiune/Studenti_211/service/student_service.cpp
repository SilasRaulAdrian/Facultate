//
// Created by Raul on 24.06.2025.
//
#include "student_service.h"

ServiceStudent::ServiceStudent(RepoStudent &repo) : repo(repo) {}

const std::vector<Student> &ServiceStudent::getAll() const {
    return repo.getAll();
}

void ServiceStudent::stergeStudenti(const std::vector<int> &ids) {
    std::vector<Student> inainte;

    for (const auto& s : repo.getAll()) {
        for (int id : ids) {
            if (s.getNrMatricol() == id) {
                inainte.push_back(s);
            }
        }
    }

    for (const auto& s : inainte) {
        repo.sterge(s.getNrMatricol());
    }

    undoStack.push([this, inainte]() {
        auto curent = repo.getAll();
        auto copie = curent;
        copie.insert(copie.end(), inainte.begin(), inainte.end());
        repo.setAll(copie);
    });

    redoStack = {};
}

void ServiceStudent::imbatranire() {
    auto inainte = repo.getAll();
    auto dupa = inainte;

    for (auto& s : dupa) {
        s.setVarsta(s.getVarsta() + 1);
    }

    repo.setAll(dupa);

    undoStack.push([this, inainte]() {
        repo.setAll(inainte);
    });

    redoStack = {};
}

void ServiceStudent::intinerire() {
    auto inainte = repo.getAll();
    auto dupa = inainte;

    for (auto& s : dupa) {
        s.setVarsta(s.getVarsta() - 1);
    }

    repo.setAll(dupa);

    undoStack.push([this, inainte]() {
        repo.setAll(inainte);
    });

    redoStack = {};
}

void ServiceStudent::undo() {
    if (undoStack.empty())
        return;

    auto actiune = undoStack.top();
    undoStack.pop();

    auto stareCurenta = repo.getAll();
    redoStack.push([this, stareCurenta]() {
        repo.setAll(stareCurenta);
    });

    actiune();
}

void ServiceStudent::redo() {
    if (redoStack.empty())
        return;

    auto actiune = redoStack.top();
    redoStack.pop();

    auto stareCurenta = repo.getAll();
    undoStack.push([this, stareCurenta]() {
        repo.setAll(stareCurenta);
    });

    actiune();
}