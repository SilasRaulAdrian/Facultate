#ifndef LAB_6_7_UNDO_H
#define LAB_6_7_UNDO_H

#include "../repository/carte_repo.h"
#include <memory>

/**
 * Clasa abstracta pentru operatii undo
 */
class ActiuneUndo {
public:
    /**
     * Functie virtuala pura pentru a efectua undo
     */
    virtual void doUndo() = 0;

    /**
     * Destructor virtual
     */
    virtual ~ActiuneUndo() = default;
};

/**
 * Clasa pentru undo adaugare carte
 */
class UndoAdaugaCarte : public ActiuneUndo {
private:
    CarteRepo &repo;
    Carte carte;
public:
    /**
     * Constructorul clasei UndoAdaugaCarte
     * @param repo Repository-ul de carti
     * @param carte Carte adaugata
     */
    UndoAdaugaCarte(CarteRepo &repo, const Carte &carte) : repo(repo), carte(carte) {}

    /**
     * Functie pentru a efectua undo
     */
    void doUndo() override {
        repo.stergeCarte(carte.getId());
    }
};

/**
 * Clasa pentru undo stergere carte
 */
class UndoStergeCarte : public ActiuneUndo {
private:
    CarteRepo &repo;
    Carte carte;
public:
    /**
     * Constructorul clasei UndoStergeCarte
     * @param repo Repository-ul de carti
     * @param carte Carte stearsa
     */
    UndoStergeCarte(CarteRepo &repo, const Carte &carte) : repo(repo), carte(carte) {}

    /**
     * Functie pentru a efectua undo
     */
    void doUndo() override {
        repo.adaugaCarte(carte);
    }
};

/**
 * Clasa pentru undo modificare carte
 */
class UndoModificaCarte : public ActiuneUndo {
private:
    CarteRepo &repo;
    Carte carteVeche;
    Carte carteNoua;
public:
    /**
     * Constructorul clasei UndoModificaCarte
     * @param repo Repository-ul de carti
     * @param carteVeche Carte veche
     * @param carteNoua Carte noua
     */
    UndoModificaCarte(CarteRepo &repo, const Carte &carteVeche, const Carte &carteNoua) : repo(repo), carteVeche(carteVeche), carteNoua(carteNoua) {}

    /**
     * Functie pentru a efectua undo
     */
    void doUndo() override {
        repo.modificaTitlu(carteNoua.getId(), carteVeche.getTitlu());
        repo.modificaAutor(carteNoua.getId(), carteVeche.getAutor());
        repo.modificaGen(carteNoua.getId(), carteVeche.getGen());
        repo.modificaAnAparitie(carteNoua.getId(), carteVeche.getAnAparitie());
    }
};

#endif //LAB_6_7_UNDO_H
