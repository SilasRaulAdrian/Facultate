//
// Created by Raul on 19.05.2025.
//

#ifndef JUCATOR_V1_JUCATOR_REPO_H
#define JUCATOR_V1_JUCATOR_REPO_H

#include "../domain/jucator.h"
#include <QString>
#include <QSet>
#include <vector>

class JucatorRepo {
private:
    QString filename;
    std::vector<Jucator> jucatori;
    QSet<QString> tari;

    void loadFromFile();

public:
    JucatorRepo(const QString &filename);
    std::vector<Jucator>& getAll();
    void adaugaJucator(const Jucator& jucator);
    std::vector<Jucator> getJucatoriDupaTara(const QString &tara) const;
    QSet<QString>& getToateTarile();
    void actualizeazaRankinguri();
};

#endif //JUCATOR_V1_JUCATOR_REPO_H
