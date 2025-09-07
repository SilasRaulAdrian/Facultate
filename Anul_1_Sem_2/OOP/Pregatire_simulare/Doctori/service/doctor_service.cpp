#include "doctor_service.h"

std::vector<Doctor> DoctorService::getAll() const {
    return repo.getAll();
}

std::vector<Doctor> DoctorService::filterByNume(const QString &nume) const {
    std::vector<Doctor> result;

    for (const auto& d : repo.getAll())
        if (d.getNume() == nume)
            result.push_back(d);

    return result;
}

std::vector<Doctor> DoctorService::filterBySectie(const QString &sectie) const {
    std::vector<Doctor> result;

    for (const auto& d : repo.getAll())
        if (d.getSectie() == sectie)
            result.push_back(d);

    return result;
}