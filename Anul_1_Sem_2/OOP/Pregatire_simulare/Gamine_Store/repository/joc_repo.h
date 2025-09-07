//
// Created by Raul on 18.05.2025.
//

#ifndef GAMINE_STORE_JOC_REPO_H
#define GAMINE_STORE_JOC_REPO_H

#include "../domain/joc.h"
#include <QString>
#include <vector>

class JocRepo {
private:
    std::vector<Joc> jocuri;
    QString filename;

    void loadFromFile();
public:
    JocRepo(const QString &filename);
    const std::vector<Joc>& getAll() const;
};

#endif //GAMINE_STORE_JOC_REPO_H
