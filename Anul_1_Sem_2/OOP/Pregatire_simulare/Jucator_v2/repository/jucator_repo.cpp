#include "jucator_repo.h"
#include <QFile>
#include <QTextStream>
#include <algorithm>

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
        tari.insert(tara);
    }

    file.close();
}

std::vector<Jucator> &JucatorRepo::getAll() {
    return jucatori;
}

std::vector<Jucator> JucatorRepo::getJucatoriDupaTara(const QString &tara) const {
    std::vector<Jucator> result;
    for (const auto& j : jucatori)
        if (j.getTara() == tara)
            result.push_back(j);
    return result;
}

QSet<QString> &JucatorRepo::getToateTarile() {
    return tari;
}

void JucatorRepo::adaugaJucator(const Jucator &jucator) {
    jucatori.push_back(jucator);
    tari.insert(jucator.getTara());
    actualizeazaRankinguri();
}

void JucatorRepo::actualizeazaRankinguri() {
    std::sort(jucatori.begin(), jucatori.end(), [](const Jucator &a, const Jucator &b) {
        return a.getPuncte() > b.getPuncte();
    });

    for (int i = 0; i < jucatori.size(); ++i)
        jucatori[i].setRanking(i + 1);
}