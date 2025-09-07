#include "Dictionar.h"
#include <iostream>
#include "IteratorDictionar.h"

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: O(1)
 * Overall complexity: O(1)
 * Este o initializare simpla de pointeri si o variabila, deci totul constant.
 */
Dictionar::Dictionar() {
	prim = nullptr;
    ultim = nullptr;
    dimensiune = 0;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - daca dictionarul este deja gol (nu intra in while).
 * Worst Case: O(n) - daca sunt n noduri, se parcurg si se elibereaza toate.
 * Overall complexity: O(n) - complexitatea este liniara in functie de numarul de elemente.
 */
Dictionar::~Dictionar() {
	while (prim != nullptr) {
        Nod* temp = prim;
        prim = prim->urmator;
        delete temp;
    }
}

/*
 * Complexitati:
 * Best Case: Teta(1) - cheia este primul nod.
 * Worst Case: O(n) - cheia nu exista, se parcurge toata lista.
 * Overall complexity: O(n) - cautarea este liniara, iar adaugarea in coada e O(1).
 */
TValoare Dictionar::adauga(TCheie c, TValoare v){
	Nod* curent = prim;

    while (curent != nullptr) {
        if (curent->element.first == c) {
            TValoare veche = curent->element.second;
            curent->element.second = v;
            return veche;
        }
        curent = curent->urmator;
    }

    Nod* nou = new Nod({c, v}, nullptr, ultim);
    if (ultim != nullptr) {
        ultim->urmator = nou;
    } else {
        prim = nou;
    }
    ultim = nou;
    dimensiune++;

	return NULL_TVALOARE;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - cheia este in primul nod.
 * Worst Case: O(n) - cheia este ultimul nod sau nu exista.
 * Overall complexity: O(n)
 */
//cauta o cheie si returneaza valoarea asociata (daca dictionarul contine cheia) sau null
TValoare Dictionar::cauta(TCheie c) const{
	Nod* curent = prim;

    while (curent != nullptr) {
        if (curent->element.first == c) {
            return curent->element.second;
        }
        curent = curent->urmator;
    }

	return NULL_TVALOARE;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - cheia este primul nod.
 * Worst Case: Teta(n) - cheia este ultimul nod sau nu exista, unde n este numarul de noduri.
 * Overall complexity: O(n)
 */
TValoare Dictionar::sterge(TCheie c){
	Nod* curent = prim;

    while (curent != nullptr) {
        if (curent->element.first == c) {
            TValoare val = curent->element.second;

            if (curent->anterior != nullptr) {
                curent->anterior->urmator = curent->urmator;
            } else {
                prim = curent->urmator;
            }

            if (curent->urmator != nullptr) {
                curent->urmator->anterior = curent->anterior;
            } else {
                ultim = curent->anterior;
            }

            delete curent;
            dimensiune--;
            return val;
        }
        curent = curent->urmator;
    }

	return NULL_TVALOARE;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: O(1)
 * Overall complexity: O(1)
 * Valoarea e deja memorata in dimensiune.
 */
int Dictionar::dim() const {
	return dimensiune;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: O(1)
 * Overall complexity: O(1)
 * Verifica daca dictionarul este gol.
 */
bool Dictionar::vid() const{
	return dimensiune == 0;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: O(1)
 * Overall complexity: O(1) - presupunand ca constructorul iteratorului este constant.
 * Returneaza un iterator la inceputul listei.
 */
IteratorDictionar Dictionar::iterator() const {
	return  IteratorDictionar(*this);
}