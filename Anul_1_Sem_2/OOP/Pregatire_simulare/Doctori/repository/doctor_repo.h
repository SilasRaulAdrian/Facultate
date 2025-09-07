//
// Created by Raul on 16.05.2025.
//

#ifndef DOCTORI_DOCTOR_H
#define DOCTORI_DOCTOR_REPO_H

#include "../domain/doctor.h"
#include <QString>
#include <vector>

class DoctorRepo {
private:
    std::vector<Doctor> doctori;
    QString filename;

    void loadFromFile();

public:
    DoctorRepo(const QString &filename);
    const std::vector<Doctor>& getAll() const;
};

#endif //DOCTORI_DOCTOR_H
