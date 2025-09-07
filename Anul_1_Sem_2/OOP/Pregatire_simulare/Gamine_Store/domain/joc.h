//
// Created by Raul on 18.05.2025.
//

#ifndef GAMINE_STORE_JOC_H
#define GAMINE_STORE_JOC_H

#include <QString>

class Joc {
private:
    QString titlu;
    int pret;
    QString platforma;
    int pegi;

public:
    Joc(const QString &titlu, int pret, const QString &platforma, int pegi) :
        titlu(titlu), pret(pret), platforma(platforma), pegi(pegi) {}

    QString getTitlu() const;
    int getPret() const;
    QString getPlatforma() const;
    int getPegi() const;
};

#endif //GAMINE_STORE_JOC_H
