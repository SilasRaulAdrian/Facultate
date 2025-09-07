#include "MD.h"
#include "IteratorMD.h"
#include <exception>
#include <iostream>

using namespace std;

/*
 * Complexitati:
 * Best Case: Teta(CAPACITY) - initializam toate bucket-urile cu nullptr
 * Worst Case: Teta(CAPACITY) - la fel ca best case, trebuie initializate toate pozitiile
 * Overall complexity: Teta(CAPACITY) - initializarea este liniara in functie de capacitate
 */
MD::MD() {
	tabel = new Nod*[capacitate];
    for (int i = 0; i < capacitate; i++) {
        tabel[i] = nullptr;
    }
    dimensiune = 0;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - inserare la inceputul listei, hash function constanta
 * Worst Case: Teta(1) - chiar daca lista e lunga, inseram la inceput
 * Overall complexity: Teta(1) - operatie constanta, indiferent de dimensiune
 */
void MD::adauga(TCheie c, TValoare v) {
	int index = hash(c);
    Nod* nodNou = new Nod;
    nodNou->elem = make_pair(c, v);
    nodNou->urm = tabel[index];
    tabel[index] = nodNou;
    dimensiune++;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - elementul de sters este primul in lista
 * Worst Case: O(n) - elementul este ultimul in lista sau nu exista (n = nr elemente in bucket)
 * Overall complexity: O(n) - in cel mai rau caz parcurgem toata lista din bucket
 */
bool MD::sterge(TCheie c, TValoare v) {
	int index = hash(c);
    Nod* nodCurent = tabel[index];
    Nod* nodPrecedent = nullptr;

    while (nodCurent != nullptr) {
        if (nodCurent->elem.first == c && nodCurent->elem.second == v) {
            if (nodPrecedent == nullptr) {
                tabel[index] = nodCurent->urm;
            } else {
                nodPrecedent->urm = nodCurent->urm;
            }
            delete nodCurent;
            dimensiune--;
            return true;
        }
        nodPrecedent = nodCurent;
        nodCurent = nodCurent->urm;
    }
    return false;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - bucket-ul este gol
 * Worst Case: O(n) - toate elementele din bucket au cheia c (n = nr elemente in bucket)
 * Overall complexity: O(n) - trebuie parcursa toata lista din bucket
 */
vector<TValoare> MD::cauta(TCheie c) const {
	vector<TValoare> valori;
    int index = hash(c);
    Nod* nodCurent = tabel[index];

    while (nodCurent != nullptr) {
        if (nodCurent->elem.first == c) {
            valori.push_back(nodCurent->elem.second);
        }
        nodCurent = nodCurent->urm;
    }
    return valori;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - doar returnam size
 * Worst Case: Teta(1) - la fel ca best case
 * Overall complexity: Teta(1) - operatie constanta
 */
int MD::dim() const {
	return dimensiune;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - verificam doar size
 * Worst Case: Teta(1) - la fel ca best case
 * Overall complexity: Teta(1) - operatie constanta
 */
bool MD::vid() const {
	return dimensiune == 0;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: O(1)
 * Overall complexity: O(1) - presupunand ca constructorul iteratorului este constant.
 */
IteratorMD MD::iterator() const {
	return IteratorMD(*this);
}

/*
 * Complexitati:
 * Best Case: Teta(CAPACITY) - toate bucket-urile sunt goale
 * Worst Case: Teta(CAPACITY + n) - trebuie sterse toate elementele (n = nr total elemente)
 * Overall complexity: Teta(CAPACITY + n) - trebuie parcursa toata tabela si toate listele
 */
MD::~MD() {
	for (int i = 0; i < capacitate; i++) {
        Nod* nodCurent = tabel[i];
        while (nodCurent != nullptr) {
            Nod* nodDeSters = nodCurent->urm;
            delete nodCurent;
            nodCurent = nodDeSters;
        }
    }
    delete[] tabel;
}

