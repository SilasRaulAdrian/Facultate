#ifndef TICTACTOE_JOC_SERVICE_H
#define TICTACTOE_JOC_SERVICE_H

#include "../domain/joc.h"
#include "../repository/joc_repo.h"
#include "../validator/validator.h"
#include <algorithm>

class ServiceJoc {
private:
    RepoJoc &repo;
    Validator &val;
public:
    ServiceJoc(RepoJoc &repo, Validator &val);
    void adaugaJoc(int id, int dim, const QString& tabla, const QChar& jucator, const QString& stare);
    void modificaJoc(int id, int newDim, const QString& newTabla, const QChar& newJucator, const QString& newStare);
    const std::vector<Joc> getAllSorted();
};

#endif //TICTACTOE_JOC_SERVICE_H
