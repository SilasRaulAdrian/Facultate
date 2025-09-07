#include "Multime.h"
#include "IteratorMultime.h"
#include <iostream>

using namespace std;

//o posibila relatie
bool rel(TElem e1, TElem e2) {
	return e1 <= e2;
}

/*
 * Complexitati:
 * Best Case: Teta(n) - unde n = capacitate initiala (10); se initializeaza elemente si se seteaza legaturile.
 * Worst Case: Teta(n)
 * Overall complexity: O(n)
 */
Multime::Multime() {
	capacitate = 10;
    elemente = new Nod[capacitate];
    prim = -1;
    dimensiune = 0;

    primLiber = 0;
    for (int i = 0; i < capacitate - 1; i++) {
        elemente[i].urmator = i + 1;
    }
    elemente[capacitate - 1].urmator = -1;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - Daca elementul este deja in multime si se afla pe prima pozitie.
 * Worst Case: Teta(n) - Daca elementul este la sfarsitul multimii sau nu exista.
 * Overall complexity: O(n) - Operatie liniara in functie de numarul de elemente deja existente.
 */
bool Multime::adauga(TElem elem) {
	if (cauta(elem)) {
        return false;
    }

    if (primLiber == -1) {
        int nouaCapacitate = capacitate * 2;
        Nod* elementeNoi = new Nod[nouaCapacitate];

        for (int i = 0; i < capacitate; i++) {
            elementeNoi[i] = elemente[i];
        }

        for (int i = capacitate; i < nouaCapacitate - 1; i++) {
            elementeNoi[i].urmator = i + 1;
        }
        elementeNoi[nouaCapacitate - 1].urmator = -1;

        primLiber = capacitate;

        delete[] elemente;
        elemente = elementeNoi;
        capacitate = nouaCapacitate;
    }

    int pozitie = primLiber;
    primLiber = elemente[primLiber].urmator;

    elemente[pozitie].informatie = elem;

    if (prim == -1 || !rel(elemente[prim].informatie, elem)) {
        elemente[pozitie].urmator = prim;
        prim = pozitie;
    } else {
        int curent = prim;
        while (elemente[curent].urmator != -1 &&
               rel(elemente[elemente[curent].urmator].informatie, elem)) {
            curent = elemente[curent].urmator;
        }
        elemente[pozitie].urmator = elemente[curent].urmator;
        elemente[curent].urmator = pozitie;
    }

    dimensiune++;
    return true;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - Daca elementul este primul din multime.
 * Worst Case: Teta(n) - Daca elementul este la sfarsitul multimii sau nu exista.
 * Overall complexity: O(n) - Parcurgere liniara pana se gaseste elementul de sters.
 */
bool Multime::sterge(TElem elem) {
	if (prim == -1) {
        return false;
    }

    if (elemente[prim].informatie == elem) {
        int deSters = prim;
        prim = elemente[prim].urmator;

        elemente[deSters].urmator = primLiber;
        primLiber = deSters;

        dimensiune--;
        return true;
    }

    int curent = prim;
    while (elemente[curent].urmator != -1 &&
           elemente[elemente[curent].urmator].informatie != elem) {
        curent = elemente[curent].urmator;
    }

    if (elemente[curent].urmator == -1) {
        return false;
    }

    int deSters = elemente[curent].urmator;
    elemente[curent].urmator = elemente[deSters].urmator;

    elemente[deSters].urmator = primLiber;
    primLiber = deSters;

    dimensiune--;
    return true;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - Daca elementul este primul din multime.
 * Worst Case: Teta(n) - Daca elementul este la sfarsitul multimii sau nu exista.
 * Overall complexity: O(n)
 */
bool Multime::cauta(TElem elem) const {
	int curent = prim;
    while (curent != -1) {
        if (elemente[curent].informatie == elem) {
            return true;
        }
        curent = elemente[curent].urmator;
    }

    return false;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: Teta(1)
 * Overall complexity: O(1)
 */
int Multime::dim() const {
	return dimensiune;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: Teta(1)
 * Overall complexity: O(1)
 */
bool Multime::vida() const {
	return dimensiune == 0;
}

/*
 * Complexitati:
 * Best Case: Teta(n) - trebuie verificat fiecare element, indiferent de conditie.
 * Worst Case: Teta(n) - la fel ca best case, toate elementele sunt verificate.
 * Overall complexity: Teta(n) - parcurgem toate elementele si stergem cele care nu indeplinesc conditia.
 */
void Multime::filtreaza(Conditie cond) {
    int curent = prim;
    int precedent = -1;

    while (curent != -1) {
        if (!cond(elemente[curent].informatie)) {
            if (precedent == -1) {
                prim = elemente[curent].urmator;
            } else {
                elemente[precedent].urmator = elemente[curent].urmator;
            }

            int deSters = curent;
            curent = elemente[curent].urmator;

            elemente[deSters].urmator = primLiber;
            primLiber = deSters;

            dimensiune--;
        } else {
            precedent = curent;
            curent = elemente[curent].urmator;
        }
    }
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: Teta(1)
 * Overall complexity: O(1)
 */
IteratorMultime Multime::iterator() const {
	return IteratorMultime(*this);
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: Teta(1)
 * Overall complexity: O(1)
 */
Multime::~Multime() {
	delete[] elemente;
}