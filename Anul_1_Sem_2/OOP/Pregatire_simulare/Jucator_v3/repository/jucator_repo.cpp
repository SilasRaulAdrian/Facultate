#include "jucator_repo.h"
#include <QFile>
#include <QTextStream>
#include <QRandomGenerator>

JucatorRepo::JucatorRepo(const QString &filename) : filename(filename) {
    loadFromFile();
}

void JucatorRepo::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.");
    }
    QTextStream in(&file);

    while(!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 4) continue;

        QString nume = tokens[0];
        QString tara = tokens[1];
        int puncte = tokens[2].toInt();
        int ranking = tokens[3].toInt();

        jucatori.emplace_back(nume, tara, puncte, ranking);
    }

    file.close();
}

void JucatorRepo::saveToFile() {
    QFile file(filename);
    if (file.open(QIODevice::WriteOnly)) {
        QTextStream out(&file);
        for (const auto& j : jucatori)
            out << j.getNume() << ',' << j.getTara() << ','
                << j.getPuncte() << ',' << j.getRanking() << '\n';
        file.close();
    }
}

std::vector<Jucator> &JucatorRepo::getAll() {
    return jucatori;
}

bool JucatorRepo::existaRank(int rank) const {
    for (const auto& j : jucatori)
        if (j.getRanking() == rank)
            return true;
    return false;
}

int JucatorRepo::getPunctajPtRank(int targetRank) const {
    if (jucatori.empty()) {
        return QRandomGenerator::global()->bounded(100, 5000);
    }

    int nextHigherPoints = 10;
    int nextLowerPoints = 5000;

    for (const auto& j : jucatori) {
        if (j.getRanking() == targetRank) {
            throw std::runtime_error("Rank-ul exista deja!");
        }

        if (j.getRanking() < targetRank && j.getPuncte() > nextHigherPoints) {
            nextHigherPoints = j.getPuncte();
        }
        else if (j.getRanking() > targetRank && j.getPuncte() < nextLowerPoints) {
            nextLowerPoints = j.getPuncte();
        }
    }

    if (targetRank > jucatori.back().getRanking()) {
        return QRandomGenerator::global()->bounded(10, jucatori.back().getPuncte());
    }
    else if (targetRank < jucatori.front().getRanking()) {
        return QRandomGenerator::global()->bounded(jucatori.front().getPuncte(), 5000);
    }
    else {
        return QRandomGenerator::global()->bounded(nextLowerPoints, nextHigherPoints);
    }
}

void JucatorRepo::adaugaJucator(const Jucator &jucator) {
    jucatori.push_back(jucator);

    std::sort(jucatori.begin(), jucatori.end(), [](const Jucator &a, const Jucator &b) {
        return a.getRanking() < b.getRanking();
    });
}