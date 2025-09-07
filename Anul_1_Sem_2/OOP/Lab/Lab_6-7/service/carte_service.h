#ifndef LAB_6_7_CARTE_SERVICE_H
#define LAB_6_7_CARTE_SERVICE_H

#include "../repository/carte_repo.h"
#include "../cos_inchirieri/cos_inchirieri.h"
#include "../undo/undo.h"
#include <map>
#include <memory>
#include <stack>

class CarteService {
private:
    CarteRepo &repo;
    CosInchirieri cos;
    std::vector<std::unique_ptr<ActiuneUndo>> undoActions;

public:
    /**
     * Constructorul clasei CarteService
     * @param repo Repository-ul de carti
     */
    CarteService(CarteRepo &repo) : repo(repo) {}

    /**
     * Destructorul clasei CarteService
     */
    ~CarteService() = default;

    /**
     * Adauga o carte in lista de carti
     * @param id Id-ul cartii
     * @param titlu Titlul cartii
     * @param autor Autorul cartii
     * @param gen Genul cartii
     * @param an_aparitie Anul aparitiei cartii
     */
    void adaugaCarte(int id, const std::string &titlu, const std::string &autor, const std::string &gen, int an_aparitie);

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
     * @param titlu Titlul cartii
     * @return Cartea gasita sau nullptr daca nu exista
     */
    Carte cautaCarte(const std::string &titlu) const;

    /**
     * Filtreaza cartile dupa titlu
     * @param titlu Titlul dupa care se filtreaza
     * @return Lista de carti filtrate
     */
    Lista<Carte> filtreazaDupaTitlu(const std::string &titlu) const;

    /**
     * Filtreaza cartile dupa anul aparitiei
     * @param an Anul dupa care se filtreaza
     * @return Lista de carti filtrate
     */
    Lista<Carte> filtreazaDupaAn(int an) const;

    /**
     * Sorteaza cartile dupa titlu
     * @param mod 0 - crescator, 1 - descrescator
     * @return Lista de carti sortate
     */
    Lista<Carte> sorteazaDupaTitlu(int mod);

    /**
     * Sorteaza cartile dupa autor
     * @param mod 0 - crescator, 1 - descrescator
     * @return Lista de carti sortate
     */
    Lista<Carte> sorteazaDupaAutor(int mod);

    /**
     * Sorteaza cartile dupa anul aparitiei
     * @param mod 0 - crescator, 1 - descrescator
     * @return Lista de carti sortate
     */
    Lista<Carte> sorteazaDupaAnulAparitiei(int mod);

    /**
     * Sorteaza cartile dupa gen
     * @param mod 0 - crescator, 1 - descrescator
     * @return Lista de carti sortate
     */
    Lista<Carte> sorteazaDupaGen(int mod);

    /**
     * Goleste cosul de carti
     */
    void golesteCos();

    /**
     * Adauga o carte in cos dupa titlu
     * @param titlu Titlul cartii de adaugat
     */
    void adaugaCarteDupaTitlu(const std::string &titlu);

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
     * @return Cosul de carti
     */
    const std::vector<Carte> &getAllCos() const;

    /**
     * Functie care returneaza numarul de carti din cos
     * @return Numarul de carti din cos
     */
    int getSizeCos() const;

    /**
     * Functie care returneaza un map cu numarul de carti pe gen
     * @return Map cu numarul de carti pe gen
     */
    std::map<std::string, std::vector<Carte>> getCartiPeGen() const;

    /**
     * Functie care adauga o actiune undo
     */
    void undo();

    /**
     * Functie care afla titlul unei carti dupa id
     * @param id Id-ul cartii
     * @return Titlul cartii
     */
    std::string getTitluById(int id) const;
};

#endif //LAB_6_7_CARTE_SERVICE_H
