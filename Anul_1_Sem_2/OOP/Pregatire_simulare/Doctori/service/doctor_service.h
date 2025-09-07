//
// Created by Raul on 16.05.2025.
//

#ifndef DOCTORI_DOCTOR_SERVICE_H
#define DOCTORI_DOCTOR_SERVICE_H

#include "../repository/doctor_repo.h"
#include "../domain/doctor.h"
#include <vector>

class DoctorService {
private:
    DoctorRepo &repo;

public:
    DoctorService(DoctorRepo &repo) : repo(repo) {}

    std::vector<Doctor> getAll() const;
    std::vector<Doctor> filterByNume(const QString &nume) const;
    std::vector<Doctor> filterBySectie(const QString &sectie) const;
};

#endif //DOCTORI_DOCTOR_SERVICE_H
