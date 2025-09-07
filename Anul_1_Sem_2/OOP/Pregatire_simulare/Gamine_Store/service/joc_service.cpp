#include "joc_service.h"
#include <algorithm>

std::vector<Joc> JocService::getAll() const {
    return repo.getAll();
}

std::vector<Joc> JocService::sortByPret() const {
    std::vector<Joc> sorted = repo.getAll();
    std::sort(sorted.begin(), sorted.end(), [](const Joc &a, const Joc &b) {
        return a.getPret() < b.getPret();
    });
    return sorted;
}

std::vector<Joc> JocService::filterByPegi() const {
    std::vector<Joc> result;
    for (const auto& j : repo.getAll())
        if (j.getPegi() < 12)
            result.push_back(j);
    return result;
}