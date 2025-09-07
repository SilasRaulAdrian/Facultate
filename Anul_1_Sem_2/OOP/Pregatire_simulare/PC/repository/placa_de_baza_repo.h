//
// Created by Raul on 18.05.2025.
//

#ifndef PC_PLACA_DE_BAZA_REPO_H
#define PC_PLACA_DE_BAZA_REPO_H

#include "../domain/placa_de_baza.h"
#include <vector>
#include <QString>

class PlacaDeBazaRepo {
private:
    std::vector<PlacaDeBaza> placiDeBaza;
    QString filename;

    void loadFromFile();

public:
    PlacaDeBazaRepo(const QString &filename);
    void adaugaPlacaDeBaza(const QString &nume, const QString &socluProcesor, int pret);
    const std::vector<PlacaDeBaza>& getAll() const;
};

#endif //PC_PLACA_DE_BAZA_REPO_H
