//
// Created by Raul on 19.05.2025.
//

#ifndef JUCATOR_V1_JUCATOR_REPO_H
#define JUCATOR_V1_JUCATOR_REPO_H

#include "../domain/jucator.h"
#include <QString>
#include <vector>

class JucatorRepo {
private:
    QString filename;
    std::vector<Jucator> jucatori;

    void loadFromFile();

public:
    JucatorRepo(const QString &filename);
    std::vector<Jucator>& getAll();
};

#endif //JUCATOR_V1_JUCATOR_REPO_H
