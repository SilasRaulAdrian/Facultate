#include "joc_service.h"

ServiceJoc::ServiceJoc(RepoJoc &repo, Validator &val) : repo(repo), val(val) {}

void ServiceJoc::adaugaJoc(int id, int dim, const QString &tabla, const QChar &jucator, const QString &stare) {
    Joc j{id, dim, tabla, jucator, stare};
    val.valideaza(j);
    repo.adauga(j);
}

void ServiceJoc::modificaJoc(int id, int newDim, const QString &newTabla, const QChar &newJucator, const QString &newStare) {
    Joc j{id, newDim, newTabla, newJucator, newStare};
    val.valideaza(j);
    repo.update(id, newDim, newTabla, newJucator, newStare);
}

const std::vector<Joc> ServiceJoc::getAllSorted() {
    std::vector<Joc> jocuri = repo.getAll();
    std::sort(jocuri.begin(), jocuri.end(), [](const Joc &a, const Joc &b){
        return a.getStare() < b.getStare();
    });
    return jocuri;
}