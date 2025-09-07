//
// Created by Raul on 24.06.2025.
//

#ifndef STUDENTI_211_STUDENT_REPO_H
#define STUDENTI_211_STUDENT_REPO_H

#include "../domain/student.h"
#include <vector>
#include <QString>

class RepoStudent {
private:
    QString filename;
    std::vector<Student> studenti;

    void loadFromFile();
    void saveToFile();

public:
    RepoStudent(const QString& filename);
    const std::vector<Student>& getAll() const;
    void sterge(int id);
    void setAll(const std::vector<Student>& nouaLista);
};

#endif //STUDENTI_211_STUDENT_REPO_H
