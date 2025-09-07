#include <exception>
#include "Iterator.h"
#include "DO.h"

using namespace std;

/*
 * Complexitati:
 * Best Case: Teta(1) - arbore gol sau radacina este minimul.
 * Worst Case: O(h) - parcurge pana la cel mai din stanga nod.
 * Overall complexity: O(h) - h este inaltimea arborelui.
 */
Iterator::Iterator(const DO& d) : dict(d){
	curent = d.radacina;
    if (curent != -1) {
        while (dict.stanga[curent] != -1) {
            curent = dict.stanga[curent];
        }
    }
}

/*
 * Complexitati:
 * Best Case: Teta(1) - radacina este minimul.
 * Worst Case: O(h) - parcurge pana la cel mai din stanga nod.
 * Overall complexity: O(h) - h este inaltimea arborelui.
 */
void Iterator::prim(){
	curent = dict.radacina;
    if (curent != -1) {
        while (dict.stanga[curent] != -1) {
            curent = dict.stanga[curent];
        }
    }
}

/*
 * Complexitati:
 * Best Case: Teta(1) - nodul are subarbore drept sau urca un singur nivel.
 * Worst Case: O(h) - urca sau coboara pana gaseste succesorul.
 * Overall complexity: O(h) - h este inaltimea arborelui.
 */
void Iterator::urmator(){
	if (!valid()) {
        throw exception();
    }

    if (dict.dreapta[curent] != -1) {
        curent = dict.dreapta[curent];
        while (dict.stanga[curent] != -1) {
            curent = dict.stanga[curent];
        }
    } else {
        int p = dict.parinte[curent];
        while (p != -1 && dict.dreapta[p] == curent) {
            curent = p;
            p = dict.parinte[p];
        }
        curent = p;
    }
}

/*
 * Complexitati:
 * Best Case: Teta(1) - verificare directa.
 * Worst Case: Teta(1) - acces direct.
 * Overall complexity: Teta(1) - constanta.
 */
bool Iterator::valid() const{
	return curent != -1;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - acces direct.
 * Worst Case: Teta(1) - acces direct.
 * Overall complexity: Teta(1) - constanta.
 */
TElem Iterator::element() const{
	if (!valid()) {
        throw exception();
    }
    return dict.elemente[curent];
}


