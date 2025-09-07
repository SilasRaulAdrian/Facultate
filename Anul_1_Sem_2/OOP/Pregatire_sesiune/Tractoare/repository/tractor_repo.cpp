#include "tractor_repo.h"
#include <stdexcept>

RepoTractor::RepoTractor(const QString& filename) : filename(filename) {
    loadFromFile();
}

void RepoTractor::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu se poate deschide fisierul!\n");
    }

    QTextStream in(&file);
    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList parts = line.split(',');

        if (parts.size() != 4) continue;

        int id = parts[0].toInt();
        QString denumire = parts[1];
        QString tip = parts[2];
        int numarRoti = parts[3].toInt();
        Tractor t(id, denumire, tip, numarRoti);

        tractoare.push_back(t);
    }

    file.close();
}

void RepoTractor::saveToFile() {
    QFile file(filename);
    if (!file.open(QIODevice::WriteOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu se poate deschide fisierul pentru scriere!\n");
    }

    QTextStream out(&file);
    for (const auto& t : tractoare) {
        out << t.getId() << ',' << t.getDenumire() << ',' << t.getTip() << ',' << t.getNumarRoti() << '\n';
    }

    file.close();
}

const std::vector<Tractor>& RepoTractor::getAll() const {
    return tractoare;
}

void RepoTractor::adauga(const Tractor& t) {
    for (const auto& tractor : tractoare) {
        if (tractor.getId() == t.getId()) {
            throw std::runtime_error("Tractor cu acest ID deja exista!\n");
        }
    }
    tractoare.push_back(t);
    saveToFile();
}

void RepoTractor::modifica(const Tractor& t) {
    for (auto& tractor : tractoare) {
        if (tractor.getId() == t.getId()) {
            tractor.setNumarRoti(t.getNumarRoti());
            saveToFile();
            return;
        }
    }
}