//
// Created by Raul on 21.05.2025.
//

#ifndef APARTAMENTE_APARTAMENTE_SERVICE_H
#define APARTAMENTE_APARTAMENTE_SERVICE_H

#include "../domain/apartament.h"
#include "../repository/apartament_repo.h"
#include <vector>

class ApartamentService {
private:
    ApartamentRepo &repo;

public:
    ApartamentService(ApartamentRepo &repo) : repo(repo) {}

    std::vector<Apartament> getAll() const;
    void stergeApartament(int index);
    std::vector<Apartament> filterBySuprafata(int min, int max) const;
    std::vector<Apartament> filterByPret(int min, int max) const;
};

#endif //APARTAMENTE_APARTAMENTE_SERVICE_H
