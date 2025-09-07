//
// Created by Raul on 19.05.2025.
//

#ifndef JUCATOR_V1_JUCATOR_H
#define JUCATOR_V1_JUCATOR_H

#include <QString>

class Jucator {
private:
    QString nume;
    QString tara;
    int puncte;
    int ranking;

public:
    Jucator(const QString &nume, const QString &tara, int puncte, int ranking) :
        nume(nume), tara(tara), puncte(puncte), ranking(ranking) {}

    QString getNume() const;
    QString getTara() const;
    int getPuncte() const;
    int getRanking() const;

    void setNume(const QString &nume);
    void setTara(const QString &tara);
    void setPuncte(int puncte);
    void setRanking(int ranking);
};

#endif //JUCATOR_V1_JUCATOR_H
