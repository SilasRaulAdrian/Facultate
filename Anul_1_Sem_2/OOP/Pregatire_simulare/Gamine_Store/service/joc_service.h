//
// Created by Raul on 18.05.2025.
//

#ifndef GAMINE_STORE_JOC_SERVICE_H
#define GAMINE_STORE_JOC_SERVICE_H

#include "../domain/joc.h"
#include "../repository/joc_repo.h"
#include <vector>

class JocService {
private:
    JocRepo &repo;

public:
    JocService(JocRepo &repo) : repo(repo) {}

    std::vector<Joc> getAll() const;
    std::vector<Joc> sortByPret() const;
    std::vector<Joc> filterByPegi() const;
};

#endif //GAMINE_STORE_JOC_SERVICE_H
