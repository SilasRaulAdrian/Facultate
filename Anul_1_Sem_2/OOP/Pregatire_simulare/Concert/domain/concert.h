//
// Created by Raul on 20.05.2025.
//

#ifndef CONCERT_CONCERT_H
#define CONCERT_CONCERT_H

#include <QString>
#include <QDate>

class Concert {
private:
    QString numeFormatie;
    QDate data;
    int nrBilete;

public:
    Concert(const QString& numeFormatie, const QDate &data, int nrBilete) :
            numeFormatie(numeFormatie), data(data), nrBilete(nrBilete) {}

    QString getNumeFormatie() const;
    QDate getData() const;
    int getNrBilete() const;
    QString getDataString() const;

    void setNrBilete(int bilete);
    void inmultesteBilete(int factor);
};

#endif //CONCERT_CONCERT_H
