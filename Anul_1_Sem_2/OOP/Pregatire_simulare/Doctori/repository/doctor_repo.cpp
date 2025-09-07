#include "doctor_repo.h"
#include <QFile>
#include <QTextStream>

DoctorRepo::DoctorRepo(const QString &filename) : filename(filename) {
    loadFromFile();
}

void DoctorRepo::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.");
    }
    QTextStream in(&file);

    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 5) continue;

        QString cnp = tokens[0];
        QString nume = tokens[1];
        QString prenume = tokens[2];
        QString sectie = tokens[3];
        int concediu = tokens[4].toInt();

        doctori.emplace_back(cnp, nume, prenume, sectie, concediu);
    }

    file.close();
}

const std::vector<Doctor>& DoctorRepo::getAll() const {
    return doctori;
}