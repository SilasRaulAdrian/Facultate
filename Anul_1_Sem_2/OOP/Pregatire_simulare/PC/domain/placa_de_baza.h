//
// Created by Raul on 18.05.2025.
//

#ifndef PC_PLACA_DE_BAZA_H
#define PC_PLACA_DE_BAZA_H

#include <QString>

class PlacaDeBaza{
private:
    QString nume;
    QString socluProcesor;
    int pret;

public:
    PlacaDeBaza(const QString &nume, const QString &socluProcesor, int pret) :
        nume(nume), socluProcesor(socluProcesor), pret(pret) {}

    QString getNume() const;
    QString getSocluProcesor() const;
    int getPret() const;
};

#endif //PC_PLACA_DE_BAZA_H
