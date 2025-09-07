#include "validator.h"

void ValidatorProduse::valideaza(int id, const QString &nume, const QString &tip, double pret,
                                 const std::vector<int> &existingIds) {
    std::vector<QString> errs;
    if (nume.trimmed().isEmpty())
        errs.push_back("Numele produsului nu poate fi vid!");
    if (pret < 1.0 || pret > 100.0)
        errs.push_back("Pretul trebuie sa fie intre 1.0 si 100.0!");
    for (int existingId : existingIds) {
        if (existingId == id) {
            errs.push_back("Exista deja un produs cu acest ID!");
            break;
        }
    }
    if (!errs.empty())
        throw Validator(errs);
}