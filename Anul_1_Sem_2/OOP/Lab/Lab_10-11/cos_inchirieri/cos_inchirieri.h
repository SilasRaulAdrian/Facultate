#ifndef LAB_6_7_COS_INCHIRIERI_H
#define LAB_6_7_COS_INCHIRIERI_H

#include <vector>
#include <string>
#include "../domain/carte.h"
#include "../vector_dinamic/lista.h"
#include "../observer/observer.h"

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
     * Exporta cosul de carti in fisier HTML
     * @param numeFisier Numele fisierului HTML
     */
    void exportHtml(const std::string &numeFisier) const;

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

    /**
     * Functie care adauga un observer la cos
     * @param observer Observerul de adaugat
     */
    void addObserver(Observer* observer);

    /**
     * Functie care elimina un observer din cos
     * @param observer Observerul de eliminat
     */
    void removeObserver(Observer* observer);

    /**
     * Functie care notifica toti observerii despre o modificare in cos
     */
    void notify();

private:
    std::vector<Carte> cos;
    std::vector<Observer*> observers;
};

#endif //LAB_6_7_COS_INCHIRIERI_H
