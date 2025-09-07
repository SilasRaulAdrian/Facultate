#ifndef MELODII_2_MELODII_SERVICE_H
#define MELODII_2_MELODII_SERVICE_H

#include "../repository/melodii_repo.h"
#include "../validator/validator.h"

class ServiceMelodii {
private:
    RepoMelodii &repo;
    ValidatorMelodie &val;

public:
    /**
     * Constructorul clasei ServiceMelodii
     * @param repo RepoMelodii
     * @param val ValidatorMelodie
     */
    ServiceMelodii(RepoMelodii& repo, ValidatorMelodie& val) : repo(repo), val(val) {}

    /**
     * Functie care returneaza toate melodiile sortate
     * @return std::vector<Melodie>, vectorul de melodii sortate
     */
    std::vector<Melodie> getAllSortate();

    /**
     * Functie care adauga o melodie
     * @param titlu QString, titlul melodiei
     * @param artist QString, artistul melodiei
     * @param gen QString, genul melodiei
     */
    void adaugaMelodie(const QString& titlu, const QString& artist, const QString& gen);

    /**
     * Functie care sterge o melodie dupa id
     * @param id int, id-ul melodiei de sters
     */
    void stergeMelodie(int id);

    /**
     * Functie care numara melodiile cu acelasi artist
     * @param artist QString, artistul melodiei
     * @return int, numarul de melodii cu acelasi artist
     */
    int countArtist(const QString& artist);

    /**
     * Functie care numara melodiile cu acelasi gen
     * @param gen QString, genul melodiei
     * @return int, numarul de melodii cu acelasi gen
     */
    int countGen(const QString& gen);
};

#endif //MELODII_2_MELODII_SERVICE_H
