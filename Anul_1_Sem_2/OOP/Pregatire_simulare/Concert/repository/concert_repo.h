//
// Created by Raul on 20.05.2025.
//

#ifndef CONCERT_CONCERT_REPO_H
#define CONCERT_CONCERT_REPO_H

#include "../domain/concert.h"
#include <QString>
#include <vector>

class ConcertRepo {
private:
    QString filename;
    std::vector<Concert> concerte;

    void loadFromFile();

public:
    ConcertRepo(const QString &filename);
    std::vector<Concert> getAll() const;
    void actualizeazaConcert(int index, const Concert& concert);
};

#endif //CONCERT_CONCERT_REPO_H
