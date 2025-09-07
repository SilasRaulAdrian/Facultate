//
// Created by Raul on 16.05.2025.
//

#ifndef DOCTORI_DOCTOR_H
#define DOCTORI_DOCTOR_H

#include <QString>

class Doctor {
private:
    QString cnp;
    QString nume;
    QString prenume;
    QString sectie;
    int concediu;

public:
    Doctor(const QString &cnp, const QString &nume, const QString &prenume, const QString &sectie, int concediu) :
        cnp(cnp), nume(nume), prenume(prenume), sectie(sectie), concediu(concediu) {}

    QString getCnp() const;
    QString getNume() const;
    QString getPrenume() const;
    QString getSectie() const;
    int getConcediu() const;
};

#endif //DOCTORI_DOCTOR_H
