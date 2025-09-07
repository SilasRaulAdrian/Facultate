#ifndef TICTACTOE_JOC_REPO_H
#define TICTACTOE_JOC_REPO_H

#include <vector>
#include <QString>
#include "../domain/joc.h"

class RepoJoc {
private:
    QString filename;
    std::vector<Joc> jocuri;

    void loadFromFile();
    void saveToFile();
    bool exist(const Joc& j);
public:
    RepoJoc(const QString& filename);
    void adauga(Joc j);
    void update(int id, int newDim, const QString& newTabla, const QChar& newJucator, const QString& newStare);
    const std::vector<Joc>& getAll() const;
};

#endif //TICTACTOE_JOC_REPO_H
