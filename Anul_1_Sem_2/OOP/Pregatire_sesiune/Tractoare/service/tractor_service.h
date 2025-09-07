#ifndef TRACTOARE_TRACTOR_SERVICE_H
#define TRACTOARE_TRACTOR_SERVICE_H

#include "../repository/tractor_repo.h"
#include <QMap>
#include <QSet>

class ServiceTractor {
private:
    RepoTractor& repo;

public:
    /**
     * Constructorul clasei
     * @param repo RepoTractor
     */
    ServiceTractor(RepoTractor& repo) : repo(repo) {}

    /**
     * Functie care returneaza toate tractoarele sortate
     * @return const std::vector<Tractor>, vector cu toate tractoarele sortate
     */
    std::vector<Tractor> getAllSorted() const;

    /**
     * Adauga un tractor
     * @param id int, id-ul tractorului
     * @param denumire QString, denumirea tractorului
     * @param tip QString, tipul tractorului
     * @param numarRoti int, nr de roti al tractorului
     */
    void adaugaTractor(int id, const QString& denumire, const QString& tip, int numarRoti);

    /**
     * Functie care numara cate tractoare sunt de fiecare tip
     * @return QMap<QString, int>, map cu tipurile de tractoare si numarul lor
     */
    QMap<QString, int> countByType() const;

    /**
     * Functie care returneaza un set cu tipurile distincte de tractoare
     * @return QSet<QString>, set cu tipurile distincte de tractoare
     */
    QSet<QString> getDistinctTypes() const;

    /**
     * Functie care decrementeaza nr de roti al unui tractor
     * @param id int, id-ul tractorului
     */
    void decrementRoti(int id);

    /**
     * Functie care acceseaza un tractor dupa id
     * @param id int, id-ul tractorului
     * @return Tractor, tractorul cu id-ul specificat
     */
    Tractor getById(int id) const;
};

#endif //TRACTOARE_TRACTOR_SERVICE_H
