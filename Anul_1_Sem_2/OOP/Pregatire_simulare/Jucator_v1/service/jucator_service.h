//
// Created by Raul on 19.05.2025.
//

#ifndef JUCATOR_V1_JUCATOR_SERVICE_H
#define JUCATOR_V1_JUCATOR_SERVICE_H

#include "../domain/jucator.h"
#include "../repository/jucator_repo.h"
#include <vector>

class JucatorService {
private:
    JucatorRepo &repo;

public:
    JucatorService(JucatorRepo &repo) : repo(repo) {}

    void adaugaPuncte(QString nume, int puncte, QString tip);
    void recalculeazaRanking();
    void eliminaUltimii(int m);
    std::vector<Jucator> getAll();
};

#endif //JUCATOR_V1_JUCATOR_SERVICE_H
