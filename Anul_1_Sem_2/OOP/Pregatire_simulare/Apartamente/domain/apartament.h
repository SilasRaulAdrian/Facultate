//
// Created by Raul on 21.05.2025.
//

#ifndef APARTAMENTE_APARTAMENT_H
#define APARTAMENTE_APARTAMENT_H

#include <QString>

class Apartament {
private:
    int suprafata;
    QString strada;
    int pret;

public:
    Apartament(int suprafata, const QString &strada, int pret) :
        suprafata(suprafata), strada(strada), pret(pret) {}

    int getSuprafata() const;
    QString getStrada() const;
    int getPret() const;
};

#endif //APARTAMENTE_APARTAMENT_H
