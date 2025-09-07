#include "produse_repo.h"
#include <QFile>
#include <QTextStream>

RepoProduse::RepoProduse(const QString &filename) : filename(filename) {
    loadFromFile();
}

void RepoProduse::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.\n");
    }
    QTextStream in(&file);

    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 4) continue;

        int id = tokens[0].toInt();
        QString nume = tokens[1];
        QString tip = tokens[2];
        double pret = tokens[3].toDouble();

        produse.emplace_back(id, nume, tip, pret);
    }

    file.close();
}

void RepoProduse::saveToFile() {
    QFile file(filename);
    if (!file.open(QIODevice::WriteOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul pentru scriere.\n");
    }
    QTextStream out(&file);

    for (const auto &produs : produse) {
        out << produs.getId() << ","
            << produs.getNume() << ","
            << produs.getTip() << ","
            << produs.getPret() << "\n";
    }

    file.close();
}

const std::vector<Produs> &RepoProduse::getAll() const {
    return produse;
}

void RepoProduse::adauga(const Produs &p) {
    produse.push_back(p);
    saveToFile();
}