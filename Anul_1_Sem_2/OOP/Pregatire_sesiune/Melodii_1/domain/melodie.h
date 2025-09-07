//
// Created by Raul on 23.06.2025.
//

#ifndef MELODII_1_MELODIE_H
#define MELODII_1_MELODIE_H

#include <QString>

class Melodie {
    int id;
    QString titlu;
    QString artist;
    int rank;

public:
    Melodie(int id, const QString& titlu, const QString& artist, int rank);
    int getId() const;
    QString getTitlu() const;
    QString getArtist() const;
    int getRank() const;

    void setTitlu(const QString& newTitlu);
    void setRank(int newRank);
};

#endif //MELODII_1_MELODIE_H
