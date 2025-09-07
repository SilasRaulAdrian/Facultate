//
// Created by Raul on 18.05.2025.
//

#ifndef PC_PROCESOR_H
#define PC_PROCESOR_H

#include <QString>

class Procesor {
private:
    QString nume;
    int numarThreaduri;
    QString socluProcesor;
    int pret;

public:
    Procesor(const QString &nume, int numarThreaduri, const QString &socluProcesor, int pret) :
        nume(nume), numarThreaduri(numarThreaduri), socluProcesor(socluProcesor), pret(pret) {}

    QString getNume() const;
    int getNumarThreaduri() const;
    QString getSocluProcesor() const;
    int getPret() const;
};

#endif //PC_PROCESOR_H
