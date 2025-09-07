#ifndef LAB_6_7_CONSOLE_H
#define LAB_6_7_CONSOLE_H

#include "../service/carte_service.h"

class UI {
private:
    CarteService &service;

    /**
     * Afiseaza meniul aplicatiei
     */
    void afiseazaMeniu() const;

    /**
     * Adauga o carte in lista de carti
     */
    void adaugaCarte();

    /**
     * Sterge o carte din lista de carti
     */
    void stergeCarte();

    /**
     * Modifica titlul unei carti
     */
    void modificaTitlu();

    /**
     * Modifica autorul unei carti
     */
    void modificaAutor();

    /**
     * Modifica genul unei carti
     */
    void modificaGen();

    /**
     * Modifica anul aparitiei unei carti
     */
    void modificaAnAparitie();

    /**
     * Afiseaza toate cartile
     */
    void afiseazaCarti() const;

    /**
     * Cauta o carte dupa titlu
     */
    void cautaCarte() const;

    /**
     * Filtreaza cartile dupa titlu
     */
    void filtreazaDupaTitlu() const;

    /**
     * Filtreaza cartile dupa anul aparitiei
     */
    void filtreazaDupaAnulAparitiei() const;

    /**
     * Sorteaza cartile dupa titlu
     */
    void sorteazaDupaTitlu() const;

    /**
     * Sorteaza cartile dupa autor
     */
    void sorteazaDupaAutor() const;

    /**
     * Sorteaza cartile dupa anul aparitiei
     */
    void sorteazaDupaAnulAparitiei() const;

    /**
     * Sorteaza cartile dupa gen
     */
    void sorteazaDupaGen() const;

    /**
     * Goleste cosul de carti
     */
    void golesteCos() const;

    /**
     * Adauga o carte in cos dupa titlu
     */
    void adaugaCarteInCos() const;

    /**
     * Genereaza un cos de carti aleator
     */
    void genereazaCosAleator() const;

    /**
     * Exporta cosul de carti in fisier CSV
     */
    void exportaCosInCsv() const;

    /**
     * Afiseaza cartile in functie de gen
     */
     void afiseazaCartiDupaGen() const;

    /**
     * Executa operatiunea de undo
     */
    void undo();

public:
    /**
     * Constructorul clasei UI
     * @param service Serviciul de carti
     */
    UI(CarteService &service) : service(service) {}

    /**
     * Destructorul clasei UI
     */
    ~UI() = default;

    /**
     * Ruleaza aplicatia
     */
    void run();
};

#endif //LAB_6_7_CONSOLE_H
