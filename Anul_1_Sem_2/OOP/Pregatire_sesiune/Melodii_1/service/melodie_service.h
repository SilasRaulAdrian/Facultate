//
// Created by Raul on 23.06.2025.
//

#ifndef MELODII_1_MELODIE_SERVICE_H
#define MELODII_1_MELODIE_SERVICE_H

#include "../repository/melodie_repo.h"
#include <vector>
#include <algorithm>

class ServiceMelodii {
    RepoMelodii& repo;

public:
    ServiceMelodii(RepoMelodii& repo);
    std::vector<Melodie> getAllSortate() const;
    void updateMelodie(int id, const QString& titlu, int rank);
    void stergeMelodie(int id);
    bool esteUltimaMelodie(int id);
    int countRank(int rank) const;
};

#endif //MELODII_1_MELODIE_SERVICE_H
