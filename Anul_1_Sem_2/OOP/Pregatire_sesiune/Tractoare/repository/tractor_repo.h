#ifndef TRACTOARE_TRACTOR_REPO_H
#define TRACTOARE_TRACTOR_REPO_H

#include "../domain/tractor.h"
#include <vector>
#include <QString>
#include <QFile>
#include <QTextStream>

class RepoTractor {
private:
    std::vector<Tractor> tractoare;
    QString filename;

    /**
     * Incarca tractoarele din fisier
     */
    void loadFromFile();

    /**
     * Salveaza tractoarele in fisier
     */
    void saveToFile();

public:
    /**
     * Constructorul clasei
     * @param filename QString, numele fisierului
     */
    RepoTractor(const QString& filename);

    /**
     * Functie care returneaza toate tractoarele
     * @return const std::vector<Tractor>, vector cu toate tractoarele
     */
    const std::vector<Tractor>& getAll() const;

    /**
     * Adauga un tractor
     * @param t Tractor, tractorul de adaugat
     */
    void adauga(const Tractor& t);

    /**
     * Modifica un tractor
     * @param t Tractor, tractorul de modificat
     */
    void modifica(const Tractor& t);
};

#endif //TRACTOARE_TRACTOR_REPO_H
