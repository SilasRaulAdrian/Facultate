//
// Created by Raul on 24.06.2025.
//

#ifndef STUDENTI_211_STUDENT_H
#define STUDENTI_211_STUDENT_H

#include <QString>

class Student {
private:
    int nrMatricol;
    QString nume;
    int varsta;
    QString facultate;

public:
    Student(int nrMatricol, const QString& nume, int varsta, const QString& facultate);
    int getNrMatricol() const;
    QString getNume() const;
    int getVarsta() const;
    QString getFacultate() const;
    void setVarsta(int newVarsta);
};

#endif //STUDENTI_211_STUDENT_H
