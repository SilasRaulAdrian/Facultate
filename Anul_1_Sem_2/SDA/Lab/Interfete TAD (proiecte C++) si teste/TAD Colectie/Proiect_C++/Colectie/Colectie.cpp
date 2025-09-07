#include "Colectie.h"
#include "IteratorColectie.h"
#include <iostream>

using namespace std;

/*
 * Complexitati:
 * Best case (Teta(1)): Initializarea relatiei se face in timp constant.
 * Worst case (O(1)): La fel, nu depinde de dimensiunea colectiei.
 * Average case (O(1)): O operatie constanta.
 */
bool rel(TElem e1, TElem e2) {
    return e1 <= e2;
}

/*
 * Complexitati:
 * Best case (Teta(1)): Constructorul nu face nimic, doar initializeaza niste variabile.
 * Worst case (O(1)): La fel, nu depinde de dimensiunea colectiei.
 * Average case (O(1)): O operatie constanta.
 */
Colectie::Colectie() {
    capacitate = 10;
    dimensiune = 0;
    elemente = new Pereche[capacitate];
    relatie = rel;
}

/*
 * Complexitati:
 * Best case (Teta(1)): Daca elementul este deja in colectie si se afla la inceput, doar ii creste frecventa,
 *                      ceea ce dureaza constant.
 * Worst case (O(n)): Daca trebuie sa insereze un nou element si colectia este plina, se realoca un vector
 *                    de dimensiune dubla si se muta toate elementele, ceea ce este O(n).
 * Average case (0(n)): Deoarece in medie se poate parcurge jumatate din colectie pentru a gasi pozitia
 *                      corecta, complexitatea medie este O(n).
 */
void Colectie::adauga(TElem e) {
    int poz = 0;

    while (poz < dimensiune && relatie(elemente[poz].elem, e)) {
        if (elemente[poz].elem == e) {
            elemente[poz].frecventa++;
            return;
        }
        poz++;
    }

    if (dimensiune == capacitate) {
        capacitate *= 2;
        Pereche* elementeNou = new Pereche[capacitate];
        for (int i = 0; i < dimensiune; i++) {
            elementeNou[i] = elemente[i];
        }
        elementeNou[poz].elem = e;
        elementeNou[poz].frecventa = 1;
        for (int i = poz; i < dimensiune; i++) {
            elementeNou[i + 1] = elemente[i];
        }
        delete[] elemente;
        elemente = elementeNou;
    }
    else {
        for (int i = dimensiune; i > poz; i--) {
            elemente[i] = elemente[i - 1];
        }
        elemente[poz].elem = e;
        elemente[poz].frecventa = 1;
    }
    dimensiune++;
}

/*
 * Complexitati:
 * Best case (Teta(1)): Daca elementul este la inceputul colectiei
 *                      si are frecventa mai mare decat 1, doar i se scade frecventa.
 * Worst case (O(n)): Daca elementul este ultimul sau nu exista, trebuie parcurs intregul vector.
 * Average case (O(n)): Deoarece in medie se poate parcurge jumatate din colectie, este O(n).
 */
bool Colectie::sterge(TElem e) {
    for (int i = 0; i < dimensiune; i++) {
        if (elemente[i].elem == e) {
            elemente[i].frecventa--;
            if (elemente[i].frecventa == 0) {
                for (int j = i; j < dimensiune - 1; j++) {
                    elemente[j] = elemente[j + 1];
                }
                dimensiune--;
            }
            return true;
        }
    }
    return false;
}

/*
 * Complexitati:
 * Best case (Teta(1)): Daca elementul se afla la inceputul colectiei.
 * Worst case (O(n)): Daca elementul nu exista sau este ultimul, se parcurge toata colectia.
 * Average case (O(n)): Se parcurge in medie jumatate din colectie.
 */
bool Colectie::cauta(TElem elem) const {
    for (int i = 0; i < dimensiune; i++) {
        if (elemente[i].elem == elem) {
            return true;
        }
    }
    return false;
}

/*
 * Complexitati:
 * Best case (Teta(1)): Daca elementul se afla la inceputul colectiei.
 * Worst case (O(n)): Daca elementul nu exista sau este ultimul, se parcurge toata colectia.
 * Average case (O(n)): Se parcurge in medie jumatate din colectie.
 */
int Colectie::nrAparitii(TElem elem) const {
    for (int i = 0; i < dimensiune; i++) {
        if (elemente[i].elem == elem) {
            return elemente[i].frecventa;
        }
    }
    return 0;
}

/*
 * Complexitati:
 * Best case (Teta(n)): Trebuie sa adune frecventele tuturor elementelor, deci minim O(n).
 * Worst case (O(n)): Tot O(n), deoarece mereu se parcurge intreaga colectie.
 * Average case (O(n)): Indiferent de caz, trebuie sa viziteze fiecare element.
 */
int Colectie::dim() const {
    int total = 0;
    for (int i = 0; i < dimensiune; i++) {
        total += elemente[i].frecventa;
    }
    return total;
}

/*
 * Complexitati:
 * Best case (Teta(1)): Compara dimensiunea cu 0, operatie constanta.
 * Worst case (O(1)): Tot O(1), deoarece verifica doar dimensiunea.
 * Average case (O(1)): O operatie constanta.
 */
bool Colectie::vida() const {
    return dimensiune == 0;
}

/*
 * Complexitati:
 * Best case (Teta(n)): Daca b contine elemente deja existente in this, doar se mareste frecventa acestora.
 * Worst case (Teta(n*m)): Daca toate elementele trebuie inserate individual, fiecare apel adauga()
 *                      poate costa O(n), iar daca sunt n elemente, avem O(n^2).
 * Average case (O(n*m)): In cazul general, se fac n apeluri la adauga(), care in medie sunt O(n).
 */
void Colectie::adaugaToateElemente(const Colectie &b) {
    for (int i = 0; i < b.dimensiune; i++) {
        for (int j = 0; j < b.elemente[i].frecventa; j++) {
            adauga(b.elemente[i].elem);
        }
    }
}

/*
 * Complexitati:
 * Best case (Teta(1)): Constructorul iteratorului doar seteaza niste valori.
 * Worst case (O(1)): La fel, aloca un obiect si seteaza pozitia initiala.
 * Average case (O(1)): O operatie constanta.
 */
IteratorColectie Colectie::iterator() const {
    return IteratorColectie(*this);
}

/*
 * Complexitati:
 * Best case (Teta(1)): Dezalocarea unui vector este O(1).
 * Worst case (O(1)): La fel, doar se elibereaza memoria.
 * Average case (O(1)): O operatie constanta.
 */
Colectie::~Colectie() {
    delete[] elemente;
}