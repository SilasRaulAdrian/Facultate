//
// Created by Raul on 20.05.2025.
//

#ifndef CONCERT_CONCERT_SERVICE_H
#define CONCERT_CONCERT_SERVICE_H

#include "../repository/concert_repo.h"

class ConcertService {
private:
    ConcertRepo &repo;

public:
    ConcertService(ConcertRepo &repo) : repo(repo) {}
    void modificaBilete(int indexConcert, int factor);
    void cumparaBilete(int indexConcert);
    void sorteazaCronologic();
    std::vector<Concert> getAll() const;
};

#endif //CONCERT_CONCERT_SERVICE_H
