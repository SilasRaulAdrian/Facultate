#ifndef PRODUSE_VALIDATOR_H
#define PRODUSE_VALIDATOR_H

#include <QString>
#include <vector>

class Validator {
private:
    std::vector<QString> errors;

public:
    /**
     * Constructorul clasei
     * @param errs std::vector<QString>, vector cu erorile de validare
     */
    Validator(std::vector<QString> errs) : errors(std::move(errs)) {}

    /**
     * Adauga o eroare de validare
     * @param error QString, eroarea de adaugat
     */
    QString what() const {
        QString msg;
        for (const auto& e: errors)
            msg += e + "\n";
        return msg;
    }
};

class ValidatorProduse {
public:
    /**
     * Valideaza un produs
     * @param id int, id-ul produsului
     * @param nume QString, numele produsului
     * @param tip QString, tipul produsului
     * @param pret double, pretul produsului
     * @param existingIds std::vector<int>, vector cu id-urile existente
     */
    void valideaza(int id, const QString& nume, const QString& tip, double pret, const std::vector<int>& existingIds);
};

#endif //PRODUSE_VALIDATOR_H
