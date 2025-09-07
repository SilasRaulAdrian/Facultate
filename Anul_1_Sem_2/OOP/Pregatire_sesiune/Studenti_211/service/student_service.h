//
// Created by Raul on 24.06.2025.
//

#ifndef STUDENTI_211_STUDENT_SERVICE_H
#define STUDENTI_211_STUDENT_SERVICE_H

#include "../repository/student_repo.h"
#include <stack>
#include <functional>

class ServiceStudent {
private:
    RepoStudent& repo;
    using FunctieUndoRedo = std::function<void()>;
    std::stack<FunctieUndoRedo> undoStack;
    std::stack<FunctieUndoRedo> redoStack;

public:
    ServiceStudent(RepoStudent& repo);
    const std::vector<Student>& getAll() const;
    void stergeStudenti(const std::vector<int>& ids);
    void imbatranire();
    void intinerire();
    void undo();
    void redo();
};

#endif //STUDENTI_211_STUDENT_SERVICE_H
