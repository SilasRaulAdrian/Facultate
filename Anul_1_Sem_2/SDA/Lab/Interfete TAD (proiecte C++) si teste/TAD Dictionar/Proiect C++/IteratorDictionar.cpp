#include "IteratorDictionar.h"
#include "Dictionar.h"
#include <iostream>
using namespace std;

/*
 * Complexitati:
 * Best Case: Teta(1) - acceseaza un pointer.
 * Worst Case: O(1)
 * Overall complexity: O(1)
 */
IteratorDictionar::IteratorDictionar(const Dictionar& d) : dict(d){
	curent = d.prim;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: O(1)
 * Overall complexity: O(1)
 */
void IteratorDictionar::prim() {
	curent = dict.prim;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: O(1)
 * Overall complexity: O(1)
 */
void IteratorDictionar::urmator() {
	if (!valid()) {
        throw std::exception();
    }

    curent = curent->urmator;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: O(1)
 * Overall complexity: O(1)
 */
TElem IteratorDictionar::element() const{
	if (!valid()) {
        throw std::exception();
    }

    return curent->element;
}

/*
 * Complexitati:
 * Best Case: Teta(1)
 * Worst Case: O(1)
 * Overall complexity: O(1)
 */
bool IteratorDictionar::valid() const {
	return curent != nullptr;
}

/*
 * Complexitati:
 * Best Case: Teta(1) - cand k = 1 si curent->anterior este valid. Se executa un singur pas in bucla, apoi se iese
 * Worst Case: Teta(k) - cand k este foarte mare, aproape de numarul total de pasi care se pot face inapoi in lista.
 * Overall complexity: O(k) - linear in functie de k, deoarece se parcurg k noduri in lista.
 */
void IteratorDictionar::revinoKPasi(int k) {
    if (k <= 0 || !valid()) {
        throw std::exception();
    }

    for (int i = 0; i < k && curent != nullptr; ++i) {
        curent = curent->anterior;
    }

    if (curent == nullptr) {
        throw std::exception();
    }
}