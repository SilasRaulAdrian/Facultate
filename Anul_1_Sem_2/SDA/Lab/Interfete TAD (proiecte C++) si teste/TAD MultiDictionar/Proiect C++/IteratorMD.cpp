#include "IteratorMD.h"
#include "MD.h"

using namespace std;

/*
 * Complexitati:
 * Best Case: Teta(1) - primul bucket nu este gol
 * Worst Case: O(CAPACITY) - toate bucket-urile sunt goale sau primul element e in ultimul bucket
 * Overall complexity: O(CAPACITY) - in cel mai rau caz parcurgem toata tabela
 */
IteratorMD::IteratorMD(const MD& _md): md(_md) {
	indexCurent = 0;
    nodCurent = nullptr;
    gasestePrimulElement();
}

/*
 * Complexitati:
 * Best Case: Teta(1) - doar returnam elementul
 * Worst Case: Teta(1) - la fel ca best case
 * Overall complexity: Teta(1) - operatie constanta
 */
TElem IteratorMD::element() const{
	if (!valid()) {
        throw exception();
    }
    return nodCurent->elem;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - verificam doar currentNode
 * Worst Case: Teta(1) - la fel ca best case
 * Overall complexity: Teta(1) - operatie constanta
 */
bool IteratorMD::valid() const {
	return nodCurent != nullptr;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - urmatorul nod in lista curenta exista
 * Worst Case: O(CAPACITY) - trebuie sa cautam urmatorul bucket nevid
 * Overall complexity: O(CAPACITY) - in cel mai rau caz parcurgem toata tabela
 */
void IteratorMD::urmator() {
	if (!valid()) {
        throw exception();
    }
    nodCurent = nodCurent->urm;
    if (nodCurent == nullptr) {
        indexCurent++;
        gasestePrimulElement();
    }
}

/*
 * Complexitati:
 * Best Case: Teta(1) - nodul precedent se afla la inceputul lista (bucket)
 * Worst Case: O(CAPACITY) - trebuie sa cautam un bucket anterior nevid si sa parcurgem lista acestuia pana la ultimul nod
 * Overall complexity: O(CAPACITY) - in cel mai rau caz se parcurg mai multe bucket-uri si noduri
 */
void IteratorMD::previous() {
    if (!valid()) {
        throw exception();
    }

    // daca suntem la inceputul listei din bucket
    MD::Nod* head = md.tabel[indexCurent];
    if (nodCurent == head) {
        // trebuie sa cautam un bucket anterior nevid
        int i = indexCurent - 1;
        while (i >= 0 && md.tabel[i] == nullptr) {
            i--;
        }
        if (i < 0) {
            // nu exista element precedent => iteratorul devine invalid
            nodCurent = nullptr;
            indexCurent = -1;
            return;
        }

        // mergem la ultimul nod din bucketul anterior
        indexCurent = i;
        nodCurent = md.tabel[i];
        while (nodCurent->urm != nullptr) {
            nodCurent = nodCurent->urm;
        }
    } else {
        // suntem undeva in mijlocul listei, cautam nodul precedent in acelasi bucket
        MD::Nod* p = md.tabel[indexCurent];
        while (p != nullptr && p->urm != nodCurent) {
            p = p->urm;
        }
        nodCurent = p;
    }
}

/*
 * Complexitati:
 * Best Case: Teta(1) - primul bucket nu este gol
 * Worst Case: O(CAPACITY) - toate bucket-urile sunt goale sau primul element e in ultimul bucket
 * Overall complexity: O(CAPACITY) - in cel mai rau caz parcurgem toata tabela
 */
void IteratorMD::prim() {
	indexCurent = 0;
    nodCurent = nullptr;
    gasestePrimulElement();
}

/*
 * Complexitati:
 * Best Case: Teta(1) - gasim imediat un bucket nevid
 * Worst Case: O(CAPACITY) - toate bucket-urile sunt goale
 * Overall complexity: O(CAPACITY) - in cel mai rau caz parcurgem toata tabela
 */
void IteratorMD::gasestePrimulElement() {
    while (indexCurent < md.capacitate && md.tabel[indexCurent] == nullptr) {
        indexCurent++;
    }
    if (indexCurent < md.capacitate) {
        nodCurent = md.tabel[indexCurent];
    } else {
        nodCurent = nullptr;
    }
}

