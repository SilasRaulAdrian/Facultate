#include "apartamente_service.h"

std::vector<Apartament> ApartamentService::getAll() const {
    return repo.getAll();
}

void ApartamentService::stergeApartament(int index) {
    repo.stergeApartament(index);
}

std::vector<Apartament> ApartamentService::filterBySuprafata(int min, int max) const {
    std::vector<Apartament> apartamente = repo.getAll();
    std::vector<Apartament> result;

    for (const auto &a : apartamente)
        if (a.getSuprafata() >= min && a.getSuprafata() <= max)
            result.push_back(a);

    return result;
}

std::vector<Apartament> ApartamentService::filterByPret(int min, int max) const {
    std::vector<Apartament> apartamente = repo.getAll();
    std::vector<Apartament> result;

    for (const auto &a : apartamente)
        if (a.getPret() >= min && a.getPret() <= max)
            result.push_back(a);

    return result;
}