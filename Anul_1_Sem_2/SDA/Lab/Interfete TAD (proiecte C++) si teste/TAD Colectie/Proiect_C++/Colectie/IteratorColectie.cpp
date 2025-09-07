#include "IteratorColectie.h"
#include "Colectie.h"
#include <iostream>

/*
 * Complexitati:
 * Best case (Teta(1)): Initializarea iteratorului se face in timp constant, doar seteaza cateva variabile.
 * Worst case (O(1)): Nu depinde de dimensiunea colectiei, ramane O(1).
 * Average case (O(1)): O operatie constanta.
 */
IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
    pozitieCurenta = 0;
    if (pozitieCurenta < col.dimensiune)
        frecventaCurenta = 1;
    else
        frecventaCurenta = 0;
}

/*
 * Complexitati:
 * Best case (Teta(1)): Verifica daca iteratorul este valid si returneaza elementul curent.
 * Worst case (O(1)): Daca iteratorul nu este valid, arunca exceptie, dar aceasta tot O(1) este.
 * Average case (O(1)): O operatie constanta.
 */
TElem IteratorColectie::element() const{
    if (!valid())
        throw std::exception();
    return col.elemente[pozitieCurenta].elem;
}

/*
 * Complexitati:
 * Best case (Teta(1)): Se verifica doua conditii logice, ceea ce dureaza constant.
 * Worst case (O(1)): Indiferent de situatie, ramane O(1).
 * Average case (O(1)): O operatie constanta.
 */
bool IteratorColectie::valid() const {
    return pozitieCurenta < col.dimensiune && frecventaCurenta <= col.elemente[pozitieCurenta].frecventa;
}

/*
 * Complexitati:
 * Best case (Teta(1)): Daca elementul curent are frecventa mai mare decat 1,
 *                      doar incrementeaza frecventaCurenta, ceea ce este O(1).
 * Worst case (O(n)): Daca s-a epuizat frecventa unui element, trece la
 *                    urmatoarea pozitie. Dar acest lucru implica doar modificarea a doua variabile,
 *                    deci tot O(1).
 * Average case (O(1)): O operatie constanta.
 */
void IteratorColectie::urmator() {
    if (!valid())
        throw std::exception();
    if (frecventaCurenta < col.elemente[pozitieCurenta].frecventa)
        frecventaCurenta++;
    else {
        pozitieCurenta++;
        frecventaCurenta = 1;
    }
}

/*
 * Complexitati:
 * Best case (Teta(1)): Reseteaza iteratorul la inceputul colectiei, ceea ce implica doar atribuiri.
 * Worst case (O(1)): Nu depinde de dimensiunea colectiei, ramane O(1).
 * Average case (O(1)): O operatie constanta.
 */
void IteratorColectie::prim() {
    pozitieCurenta = 0;
    if (pozitieCurenta < col.dimensiune)
        frecventaCurenta = 1;
    else
        frecventaCurenta = 0;
}
