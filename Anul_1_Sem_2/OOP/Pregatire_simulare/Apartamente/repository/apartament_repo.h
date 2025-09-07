//
// Created by Raul on 21.05.2025.
//

#ifndef APARTAMENTE_APARTAMENT_REPO_H
#define APARTAMENTE_APARTAMENT_REPO_H

#include "../domain/apartament.h"
#include <vector>
#include <QString>

class ApartamentRepo {
private:
    QString filename;
    std::vector<Apartament> apartamente;

    void loadFromFile();

public:
    ApartamentRepo(const QString &filename);

    const std::vector<Apartament>& getAll() const;
    void stergeApartament(int index);
};

#endif //APARTAMENTE_APARTAMENT_REPO_H
