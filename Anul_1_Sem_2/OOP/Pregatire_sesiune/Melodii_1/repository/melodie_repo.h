//
// Created by Raul on 23.06.2025.
//

#ifndef MELODII_1_MELODIE_REPO_H
#define MELODII_1_MELODIE_REPO_H

#include "../domain/melodie.h"
#include <QString>
#include <vector>

class RepoMelodii {
private:
    std::vector<Melodie> melodii;
    QString filename;

    void loadFromFile();
    void saveToFile();

public:
    RepoMelodii(const QString& filename);
    const std::vector<Melodie>& getAll() const;
    void update(const Melodie& melodie);
    void sterge(int id);
    Melodie& find(int id);
};

#endif //MELODII_1_MELODIE_REPO_H
