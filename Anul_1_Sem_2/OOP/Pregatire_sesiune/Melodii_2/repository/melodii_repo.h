#ifndef MELODII_2_MELODII_REPO_H
#define MELODII_2_MELODII_REPO_H

#include "../domain/melodie.h"
#include <vector>

class RepoMelodii {
private:
    std::vector<Melodie> melodii;
    QString filename;

    /**
     * Functie care incarca melodiile din fisier
     */
    void loadFromFile();

    /**
     * Functie care salveaza melodiile in fisier
     */
    void saveToFile();

public:
    /**
     * Constructorul clasei
     * @param filename QString, numele fisierului
     */
    RepoMelodii(const QString& filename);

    /**
     * Functie care returneaza toate melodiile
     * @return std::vector<Melodie>, vectorul de melodii
     */
    const std::vector<Melodie>& getAll() const;

    /**
     * Functie care adauga o melodie
     * @param m Melodie, melodie de adaugat
     */
    void adauga(const Melodie& m);

    /**
     * Functie care sterge o melodie dupa id
     * @param id int, id-ul melodiei de sters
     */
    void sterge(int id);

    /**
     * Functie care genereaza un id pt melodie
     * @return int, id-ul generat
     */
    int genereazaId();
};

#endif //MELODII_2_MELODII_REPO_H
