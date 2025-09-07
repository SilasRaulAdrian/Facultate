#include "Iterator.h"
#include "DO.h"
#include <iostream>

#include <exception>
using namespace std;

/*
 * Complexitati:
 * Best Case: Teta(n) - initializarea vectorilor.
 * Worst Case: Teta(n) - initializarea tuturor elementelor.
 * Overall complexity: Teta(n) - cost constant per element, dar total liniar.
 */
DO::DO(Relatie r) {
	capacitate = 10;
    dimensiune = 0;
    radacina = -1;
    elemente = new TElem[capacitate];
    stanga = new int[capacitate];
    dreapta = new int[capacitate];
    parinte = new int[capacitate];
    primLiber = 0;
    rel = r;

    for (int i = 0; i < capacitate - 1; i++) {
        stanga[i] = i + 1;
    }

    stanga[capacitate - 1] = -1;
}

/*
 * Complexitati:
 * Best Case: Teta(n) - copiere liniara a vectorilor.
 * Worst Case: Teta(n) - toti vectorii sunt realocati si copiati.
 * Overall complexity: Teta(n) - liniar in functie de vechea capacitate.
 */
void DO::redimensionare() {
    int nouaCapacitate = capacitate * 2;
    TElem* noileElemente = new TElem[nouaCapacitate];
    int* nouaStanga = new int[nouaCapacitate];
    int* nouaDreapta = new int[nouaCapacitate];
    int* nouParinte = new int[nouaCapacitate];

    for (int i = 0; i < capacitate; i++) {
        noileElemente[i] = elemente[i];
        nouaStanga[i] = stanga[i];
        nouaDreapta[i] = dreapta[i];
        nouParinte[i] = parinte[i];
    }

    for (int i = capacitate; i < nouaCapacitate - 1; i++) {
        nouaStanga[i] = i + 1;
    }
    nouaStanga[nouaCapacitate - 1] = -1;

    primLiber = capacitate;

    delete[] elemente;
    delete[] stanga;
    delete[] dreapta;
    delete[] parinte;

    elemente = noileElemente;
    stanga = nouaStanga;
    dreapta = nouaDreapta;
    parinte = nouParinte;
    capacitate = nouaCapacitate;
}

//adauga o pereche (cheie, valoare) in dictionar
//daca exista deja cheia in dictionar, inlocuieste valoarea asociata cheii si returneaza vechea valoare
//daca nu exista cheia, adauga perechea si returneaza null
/*
 * Complexitati:
 * Best Case: Teta(1) - cheia este radacina.
 * Worst Case: O(n) - arbore degenerat, cheia nu exista.
 * Overall complexity: O(h) - h este inaltimea arborelui, O(log n) daca e balansat.
 */
TValoare DO::adauga(TCheie c, TValoare v) {
    // Daca arborele este gol inseram la radacina
	if (radacina == -1) {
        if (primLiber == -1) {
            redimensionare();
        }
        int nouIndex = primLiber;
        primLiber = stanga[primLiber];
        elemente[nouIndex] = make_pair(c, v);
        stanga[nouIndex] = -1;
        dreapta[nouIndex] = -1;
        parinte[nouIndex] = -1;
        radacina = nouIndex;
        dimensiune++;
        return NULL_TVALOARE;
    }

    int curent = radacina;
    int p = -1;

    // Cautam pozitia potrivita pentru inserare
    while (curent != -1) {
        p = curent;
        if (c == elemente[curent].first) {
            // Cheia exista deja inlocuim valoarea
            TValoare veche = elemente[curent].second;
            elemente[curent].second = v;
            return veche;
        } else if (rel(c, elemente[curent].first)) {
            curent = stanga[curent];
        } else {
            curent = dreapta[curent];
        }
    }

    if (primLiber == -1) {
        redimensionare();
    }

    int nouIndex = primLiber;
    primLiber = stanga[primLiber];
    elemente[nouIndex] = make_pair(c, v);
    stanga[nouIndex] = -1;
    dreapta[nouIndex] = -1;
    parinte[nouIndex] = p;

    // Legam noul nod de parinte
    if (rel(c, elemente[p].first)) {
        stanga[p] = nouIndex;
    } else {
        dreapta[p] = nouIndex;
    }

    dimensiune++;
    return NULL_TVALOARE;
}

//cauta o cheie si returneaza valoarea asociata (daca dictionarul contine cheia) sau null
/*
 * Complexitati:
 * Best Case: Teta(1) - cheia este radacina.
 * Worst Case: O(n) - cautare in arbore degenerat (lista).
 * Overall complexity: O(h) - h este inaltimea arborelui.
 */
TValoare DO::cauta(TCheie c) const {
	int curent = radacina;

    // Parcurgem arborele binar de cautare in functie de relatie
    while (curent != -1) {
        if (c == elemente[curent].first) {
            // Cheia a fost gasita returnam valoarea asociata
            return elemente[curent].second;
        } else if (rel(c, elemente[curent].first)) {
            // Daca cheia cautata este mai mica mergem in stanga
            curent = stanga[curent];
        } else {
            // Daca cheia cautata este mai mare mergem in dreapta
            curent = dreapta[curent];
        }
    }

    return NULL_TVALOARE;
}

//sterge o cheie si returneaza valoarea asociata (daca exista) sau null
/*
 * Complexitati:
 * Best Case: Teta(1) - cheia este radacina si nu are copii.
 * Worst Case: O(n) - stergere cu succesor in arbore degenerat.
 * Overall complexity: O(h) - h este inaltimea arborelui.
 */
TValoare DO::sterge(TCheie c) {
	int nod = -1;
    int curent = radacina;

    // Cautam nodul cu cheia c
    while (curent != -1) {
        if (c == elemente[curent].first) {
            nod = curent;
            break;
        } else if (rel(c, elemente[curent].first)) {
            curent = stanga[curent];
        } else {
            curent = dreapta[curent];
        }
    }

    if (nod == -1) {
        return NULL_TVALOARE;
    }

    // Daca nodul are doi copii cautam succesorul sau in subarborele drept
    if (stanga[nod] != -1 && dreapta[nod] != -1) {
        int succesor = dreapta[nod];
        while (stanga[succesor] != -1) {
            succesor = stanga[succesor];
        }
        // Facem swap intre nod si succesor
        TElem temp = elemente[nod];
        elemente[nod] = elemente[succesor];
        elemente[succesor] = temp;
        nod = succesor; // Continuam stergerea cu succesorul
    }

    // Determinam copilul care il va inlocui pe nod
    int copil = -1;
    if (stanga[nod] != -1) {
        copil = stanga[nod];
    } else {
        copil = dreapta[nod];
    }

    // Daca nodul este radacina
    if (nod == radacina) {
        radacina = copil;
        if (copil != -1) {
            parinte[copil] = -1;
        }
    } else {
        int p = parinte[nod];
        // Actualizam legaturile parintelui
        if (stanga[p] == nod) {
            stanga[p] = copil;
        } else {
            dreapta[p] = copil;
        }
        if (copil != -1) {
            parinte[copil] = p;
        }
    }

    // Adaugam nodul la lista spatiilor libere
    TValoare val = elemente[nod].second;
    stanga[nod] = primLiber;
    primLiber = nod;
    dimensiune--;
    return val;
}

//returneaza numarul de perechi (cheie, valoare) din dictionar
/*
 * Complexitati:
 * Best Case: Teta(1) - returneaza direct dimensiunea.
 * Worst Case: Teta(1) - acces direct.
 * Overall complexity: Teta(1) - operatia este constanta.
 */
int DO::dim() const {
    return dimensiune;
}

//verifica daca dictionarul e vid
/*
 * Complexitati:
 * Best Case: Teta(1) - verificare directa pe dimensiune.
 * Worst Case: Teta(1) - acces direct.
 * Overall complexity: Teta(1) - constanta.
 */
bool DO::vid() const {
    return dimensiune == 0;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - arborele are un singur nod (radacina).
 * Worst Case: Teta(n) - se parcurg toate nodurile din arbore, unde n este dimensiunea arborelui.
 * Overall complexity: O(n) - fiecare nod este vizitat o singura data.
 */
int DO::diferentaValoareMaxMin() const {
    if (vid()) {
        return -1;
    }

    // Cautam valoarea minima, mergem spre stanga pana la capat
    int nod = radacina;
    while (stanga[nod] != -1) {
        nod = stanga[nod];
    }
    int minVal = elemente[nod].second;

    // Cautam valoarea maxima, mergem spre dreapta pana la capat
    nod = radacina;
    while (dreapta[nod] != -1) {
        nod = dreapta[nod];
    }
    int maxVal = elemente[nod].second;

    // Resetam minVal si maxVal pentru o parcurgere completa
    nod = radacina;
    minVal = maxVal = elemente[nod].second;

    // Parcurgere DFS
    int* stack = new int[dimensiune]; // tine nodurile de vizitat
    int top = -1;
    stack[++top] = radacina;

    while (top != -1) {
        int current = stack[top--];
        int val = elemente[current].second;

        if (val < minVal) {
            minVal = val;
        }
        if (val > maxVal) {
            maxVal = val;
        }

        if (stanga[current] != -1) {
            stack[++top] = stanga[current];
        }
        if (dreapta[current] != -1) {
            stack[++top] = dreapta[current];
        }
    }

    delete[] stack;
    return maxVal - minVal;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - creare directa a iteratorului.
 * Worst Case: Teta(1) - nu are initializare grea.
 * Overall complexity: Teta(1) - alocare obiect iterator.
 */
Iterator DO::iterator() const {
	return  Iterator(*this);
}

/*
 * Complexitati:
 * Best Case: Teta(1) - eliberare memorie.
 * Worst Case: Teta(1) - eliberare pointeri.
 * Overall complexity: Teta(1) - operatii constante.
 */
DO::~DO() {
	delete[] elemente;
    delete[] stanga;
    delete[] dreapta;
    delete[] parinte;
}
