#include "IteratorMultime.h"
#include "Multime.h"
#include <exception>

/*
 * Complexitati:
 * Best Case: Teta(1) - doar copiaza o referinta si o valoare intreaga.
 * Worst Case: Teta(1) - nu exista bucle sau operatii dependente de dimensiune.
 * Overall complexity: O(1) - operatie constanta, initializeaza iteratorul.
 */
IteratorMultime::IteratorMultime(const Multime& m): mult(m) {
	curent = m.prim;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - verifica validitatea si returneaza un element.
 * Worst Case: Teta(1) - verificarea si accesarea sunt ambele constante.
 * Overall complexity: O(1) - operatie constanta, acceseaza un element dintr-un vector.
 */
TElem IteratorMultime::element() const {
	if (!valid()) {
        throw std::exception();
    }

    return mult.elemente[curent].informatie;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: Teta(1)
 * Overall complexity: O(1) - verifica o singura valoare.
 */
bool IteratorMultime::valid() const {
	return curent != -1;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - verifica validitatea si actualizeaza curent.
 * Worst Case: Teta(1) - nu exista bucle sau traversari multiple.
 * Overall complexity: O(1) - muta iteratorul pe pozitia urmatoare, care este deja cunoscuta.
 */
void IteratorMultime::urmator() {
	if (!valid()) {
        throw std::exception();
    }

    curent = mult.elemente[curent].urmator;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: Teta(1)
 * Overall complexity: O(1) - repozitioneaza iteratorul la inceputul structurii.
 */
void IteratorMultime::prim() {
	curent = mult.prim;
}