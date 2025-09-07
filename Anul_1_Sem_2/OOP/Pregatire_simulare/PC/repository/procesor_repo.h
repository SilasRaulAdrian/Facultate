//
// Created by Raul on 18.05.2025.
//

#ifndef PC_PROCESOR_REPO_H
#define PC_PROCESOR_REPO_H

#include "../domain/procesor.h"
#include <vector>
#include <QString>

class ProcesorRepo {
private:
    std::vector<Procesor> procesoare;
    QString filename;

    void loadFromFile();

public:
    ProcesorRepo(const QString &filename);
    const std::vector<Procesor>& getAll() const;
};

#endif //PC_PROCESOR_REPO_H
