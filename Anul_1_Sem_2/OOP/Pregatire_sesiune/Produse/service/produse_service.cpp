#include "produse_service.h"
#include <algorithm>

ServiceProduse::ServiceProduse(RepoProduse &repo, ValidatorProduse& val) : repo(repo), val(val) {
    sortProduse();
}

void ServiceProduse::sortProduse() {
    produseSorted = repo.getAll();
    std::sort(produseSorted.begin(), produseSorted.end(), [](const Produs &a, const Produs &b) {
        return a.getPret() < b.getPret();
    });
}

std::vector<int> ServiceProduse::getExistingIds() const {
    std::vector<int> ids;
    for (const auto& p : repo.getAll())
        ids.push_back(p.getId());
    return ids;
}

const std::vector<Produs> &ServiceProduse::getAllSortat() {
    sortProduse();
    return produseSorted;
}

void ServiceProduse::adaugaProdus(int id, const QString &nume, const QString &tip, double pret) {
    const auto existingIds = getExistingIds();
    val.valideaza(id, nume, tip, pret, existingIds);
    repo.adauga(Produs{id, nume, tip, pret});
    sortProduse();
    notify();
}

int ServiceProduse::getPretFiltru() {
    return pretFiltru;
}

void ServiceProduse::setPretFiltru(int p) {
    if (pretFiltru != p) {
        pretFiltru = p;
        notify();
    }
}

int ServiceProduse::countByTip(const QString &tip) const {
    int cnt = 0;
    for (const auto& p : repo.getAll()) {
        if (p.getTip() == tip) {
            cnt++;
        }
    }
    return cnt;
}

std::vector<QString> ServiceProduse::getTipuri() const {
    std::vector<QString> tipuri;
    for (const auto &p : repo.getAll()) {
        for (const auto &tip : tipuri) {
            if (p.getTip() == tip) {
                continue;
            }
        }
        tipuri.push_back(p.getTip());
    }
    return tipuri;
}