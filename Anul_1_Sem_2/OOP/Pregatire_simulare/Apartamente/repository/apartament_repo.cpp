#include "apartament_repo.h"
#include <QFile>
#include <QTextStream>

ApartamentRepo::ApartamentRepo(const QString &filename) : filename(filename) {
    loadFromFile();
}

void ApartamentRepo::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.\n");
    }
    QTextStream in(&file);

    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 3) continue;

        int suprafata = tokens[0].toInt();
        QString strada = tokens[1];
        int pret = tokens[2].toInt();

        apartamente.emplace_back(suprafata, strada, pret);
    }

    file.close();
}

const std::vector<Apartament> &ApartamentRepo::getAll() const {
    return apartamente;
}

void ApartamentRepo::stergeApartament(int index) {
    if (index >= 0 && index < apartamente.size())
        apartamente.erase(apartamente.begin() + index);

}