#ifndef PRODUSE_PRODUSE_SERVICE_H
#define PRODUSE_PRODUSE_SERVICE_H

#include "../repository/produse_repo.h"
#include "../validator/validator.h"
#include "../domain/produs.h"
#include "../observer/observable.h"
#include <vector>
#include <QString>

class ServiceProduse : public Observable {
private:
    RepoProduse& repo;
    ValidatorProduse val;

    std::vector<Produs> produseSorted;
    int pretFiltru = 100;

    /**
     * Functie care sorteaza produsele
     */
    void sortProduse();

    /**
     * Functie care returneaza un vector cu id-urile existente
     * @return std::vector<int>, vector cu id-urile existente
     */
    std::vector<int> getExistingIds() const;

public:
    /**
     * Constructorul clasei
     * @param repo RepoProduse
     * @param val ValidatorProduse
     */
    ServiceProduse(RepoProduse& repo, ValidatorProduse& val);

    /**
     * Functie care returneaza toate produsele sortate
     * @return std::vector<Produs>, vector cu toate produsele sortate
     */
    const std::vector<Produs>& getAllSortat();

    /**
     * Functie care adauga un produs
     * @param id int, id-ul produsului
     * @param nume QString, numele produsului
     * @param tip QString, tipul produsului
     * @param pret double, pretul produsului
     */
    void adaugaProdus(int id, const QString& nume, const QString& tip, double pret);

    /**
     * Getter pt pretul filtrului
     * @return int, pretul filtrului
     */
    int getPretFiltru();

    /**
     * Setter pt pretul filtrului
     * @param p int, noul pret al filtrului
     */
    void setPretFiltru(int p);

    /**
     * Functie care numara produsele de un anumit tipu
     * @param tip QString, tipul produsului
     * @return int, numarul de produse de acest tip
     */
    int countByTip(const QString& tip) const;

    /**
     * Functie care returneaza un vector cu toate tipurile de produse
     * @return std::vector<QString>, vector cu toate tipurile de produse
     */
    std::vector<QString> getTipuri() const;
};

#endif //PRODUSE_PRODUSE_SERVICE_H
