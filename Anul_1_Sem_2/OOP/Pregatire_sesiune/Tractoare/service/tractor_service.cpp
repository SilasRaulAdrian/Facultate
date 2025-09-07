#include "tractor_service.h"
#include <stdexcept>
#include <algorithm>

std::vector<Tractor> ServiceTractor::getAllSorted() const {
    auto tractors = repo.getAll();
    std::sort(tractors.begin(), tractors.end(), [](const Tractor& a, const Tractor& b) {
        return a.getDenumire() < b.getDenumire();
    });
    return tractors;
}

void ServiceTractor::adaugaTractor(int id, const QString& denumire, const QString& tip, int numarRoti) {
    if (denumire.isEmpty() || tip.isEmpty()) {
        throw std::runtime_error("Denumirea si tipul nu pot fi goale!");
    }
    if (numarRoti < 2 || numarRoti > 16 || numarRoti % 2 != 0) {
        throw std::runtime_error("Numarul de roti trebuie sa fie par si intre 2 si 16!");
    }

    Tractor t(id, denumire, tip, numarRoti);
    repo.adauga(t);
}

QMap<QString, int> ServiceTractor::countByType() const {
    QMap<QString, int> map;
    for (const auto& t : repo.getAll()) map[t.getTip()]++;
    return map;
}

QSet<QString> ServiceTractor::getDistinctTypes() const {
    QSet<QString> types;
    for (const auto& t : repo.getAll()) {
        types.insert(t.getTip());
    }
    return types;
}

void ServiceTractor::decrementRoti(int id) {
    for (const auto& t : repo.getAll())
        if (t.getId() == id && t.getNumarRoti() > 2) {
            Tractor updated = t;
            updated.setNumarRoti(t.getNumarRoti() - 2);
            repo.modifica(updated);
            break;
        }
}

Tractor ServiceTractor::getById(int id) const {
    for (const auto& t : repo.getAll()) {
        if (t.getId() == id)
            return t;
    }
    throw std::runtime_error("Tractor inexistent");
}