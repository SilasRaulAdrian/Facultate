#include "joc_repo.h"
#include <QFile>
#include <QTextStream>

RepoJoc::RepoJoc(const QString &filename) : filename(filename) {
    loadFromFile();
}

void RepoJoc::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul!\n");
    }
    QTextStream in(&file);

    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 5) continue;

        int id = tokens[0].toInt();
        int dim = tokens[1].toInt();
        QString tabla = tokens[2];
        QChar jucator = tokens[3][0];
        QString stare = tokens[4];

        jocuri.emplace_back(id, dim, tabla, jucator, stare);
    }

    file.close();
}

void RepoJoc::saveToFile() {
    QFile file(filename);
    if (!file.open(QIODevice::WriteOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul pentru scriere!\n");
    }
    QTextStream out(&file);

    for (const auto& j : jocuri) {
        out << j.getId() << ','
            << j.getDim() << ','
            << j.getTabla() << ','
            << j.getJucator() << ','
            << j.getStare() << '\n';
    }

    file.close();
}

const std::vector<Joc> &RepoJoc::getAll() const {
    return jocuri;
}

bool RepoJoc::exist(const Joc &j) {
    for (const auto& joc : jocuri)
        if (joc.getId() == j.getId())
            return true;
    return false;
}

void RepoJoc::adauga(Joc j) {
    while (exist(j))
        j.setId(rand());
    jocuri.push_back(j);
    saveToFile();
}

void RepoJoc::update(int id, int newDim, const QString &newTabla, const QChar &newJucator, const QString &newStare) {
    for (auto& j : jocuri) {
        if (j.getId() == id) {
            j.setDim(newDim);
            j.setTabla(newTabla);
            j.setJucator(newJucator);
            j.setStare(newStare);
        }
    }
    saveToFile();
}