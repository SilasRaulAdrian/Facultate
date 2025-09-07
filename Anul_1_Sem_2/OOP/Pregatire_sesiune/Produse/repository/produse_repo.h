#ifndef PRODUSE_PRODUSE_REPO_H
#define PRODUSE_PRODUSE_REPO_H

#include <QString>
#include <vector>
#include "../domain/produs.h"

class RepoProduse {
private:
    QString filename;
    std::vector<Produs> produse;

    /**
     * Incarca produsele din fisier
     */
    void loadFromFile();

    /**
     * Salveaza produsele in fisier
     */
    void saveToFile();

public:
    /**
     * Constructorul clasei
     * @param filename QString, numele fisierului
     */
    RepoProduse(const QString& filename);

    /**
     * Functie care returneaza toate produsele
     * @return std::vector<Produs>, vector cu toate produsele
     */
    const std::vector<Produs>& getAll() const;

    /**
     * Adauga un produs
     * @param p Produs, produsul de adaugat
     */
    void adauga(const Produs& p);
};

#endif //PRODUSE_PRODUSE_REPO_H
