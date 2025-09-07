#ifndef LAB_6_7_COS_INCHIRIERI_H
#define LAB_6_7_COS_INCHIRIERI_H

#include <vector>
#include <string>
#include "../domain/carte.h"
#include "../vector_dinamic/lista.h"

class CosInchirieri {
public:
    /**
     * Constructorul clasei CosInchirieri
     */
    CosInchirieri() = default;

    /**
     * Destructorul clasei CosInchirieri
     */
    ~CosInchirieri() = default;

    /**
     * Goleste cosul de carti
     */
    void golesteCos();

    /**
     * Adauga o carte in cos dupa titlu
     * @param carte Cartea de adaugat
     */
    void adaugaCarte(const Carte &carte);

    /**
     * Genereaza un cos de carti aleator
     * @param numarCarti Numarul de carti dorit
     * @param carti Lista de carti disponibile
     */
    void genereazaCos(int numarCarti, Lista<Carte> &carti);

    /**
     * Exporta cosul de carti in fisier CSV
     * @param numeFisier Numele fisierului CSV
     */
    void exportCsv(const std::string &numeFisier) const;

    /**
     * Functie care returneaza toate cartile din cos
     * @return Lista de carti din cos
     */
    const std::vector<Carte> &getAllCos() const;

    /**
     * Functie care returneaza numarul de carti din cos
     * @return Numarul de carti din cos
     */
    int getSizeCos() const;

private:
    std::vector<Carte> cos;
};

#endif //LAB_6_7_COS_INCHIRIERI_H
