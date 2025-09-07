#ifndef LAB_6_7_CARTE_REPO_H
#define LAB_6_7_CARTE_REPO_H

#include "../domain/carte.h"
#include "../vector_dinamic/lista.h"


class CarteRepo {
private:
    Lista<Carte> carti;

public:
    /**
     * Constructorul clasei CarteRepo
     */
    CarteRepo() = default;

    /**
     * Destructorul clasei CarteRepo
     */
    ~CarteRepo() = default;

    /**
     * Adauga o carte in lista de carti
     * @param carte Cartea de adaugat
     */
    void adaugaCarte(const Carte &carte);

    /**
     * Sterge o carte din lista de carti
     * @param id Id-ul cartii de sters
     * @return True daca s-a sters cu succes, false altfel
     */
    bool stergeCarte(int id);

    /**
     * Modifica titlul unei carti
     * @param id Id-ul cartii de modificat
     * @param newTitlu Noul titlu
     * @return True daca s-a modificat cu succes, false altfel
     */
    bool modificaTitlu(int id, const std::string &newTitlu);

    /**
     * Modifica autorul unei carti
     * @param id Id-ul cartii de modificat
     * @param newAutor Noul autor
     * @return True daca s-a modificat cu succes, false altfel
     */
    bool modificaAutor(int id, const std::string &newAutor);

    /**
     * Modifica genul unei carti
     * @param id Id-ul cartii de modificat
     * @param newGen Noul gen
     * @return True daca s-a modificat cu succes, false altfel
     */
    bool modificaGen(int id, const std::string &newGen);

    /**
     * Modifica anul de aparitie al unei carti
     * @param id Id-ul cartii de modificat
     * @param newAnAparitie Noul an de aparitie
     * @return True daca s-a modificat cu succes, false altfel
     */
    bool modificaAnAparitie(int id, int newAnAparitie);

    /**
     * Getter pentru lista de carti
     * @return Lista de carti
     */
    Lista<Carte> &getCarti();

    /**
     * Cauta o carte dupa titlu
     * @param titlu Titlul cartii de cautat
     * @return Cartea gasita
     */
    Carte cautaCarte(const std::string &titlu) const;
};

#endif //LAB_6_7_CARTE_REPO_H
