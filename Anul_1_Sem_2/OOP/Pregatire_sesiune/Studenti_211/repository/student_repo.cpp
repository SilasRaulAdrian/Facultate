//
// Created by Raul on 24.06.2025.
//
#include "student_repo.h"
#include <QFile>
#include <QTextStream>

RepoStudent::RepoStudent(const QString &filename) : filename(filename) {
    loadFromFile();
}

void RepoStudent::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.\n");
    }
    QTextStream in(&file);

    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 4) continue;

        int nrMatricol = tokens[0].toInt();
        QString nume = tokens[1];
        int varsta = tokens[2].toInt();
        QString facultate = tokens[3];

        studenti.emplace_back(nrMatricol, nume, varsta, facultate);
    }

    file.close();
}

void RepoStudent::saveToFile() {
    QFile file(filename);
    if (!file.open(QIODevice::WriteOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul pentru scriere.\n");
    }
    QTextStream out(&file);

    for (const auto& s : studenti) {
        out << s.getNrMatricol() << ','
            << s.getNume() << ','
            << s.getVarsta() << ','
            << s.getFacultate() << '\n';
    }

    file.close();
}

const std::vector<Student> &RepoStudent::getAll() const {
    return studenti;
}

void RepoStudent::sterge(int id) {
    for (size_t i = 0; i < studenti.size(); ++i) {
        if (studenti[i].getNrMatricol() == id) {
            studenti.erase(studenti.begin() + i);
            return;
        }
    }
    saveToFile();
}

void RepoStudent::setAll(const std::vector<Student> &nouaLista) {
    studenti = nouaLista;
    saveToFile();
}