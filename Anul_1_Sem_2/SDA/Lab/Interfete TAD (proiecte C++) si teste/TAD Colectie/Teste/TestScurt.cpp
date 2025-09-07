#include "TestScurt.h"
#include "../Proiect_C++/Colectie/Colectie.h"
#include "../Proiect_C++/Colectie/IteratorColectie.h"
#include <cassert>
#include <iostream>
using namespace std;

void testAll() {
	Colectie c;
	c.adauga(5);
	c.adauga(6);
	c.adauga(0);
	c.adauga(5);
	c.adauga(10);
	c.adauga(8);

	assert(c.dim() == 6);
	assert(c.nrAparitii(5) == 2);

	assert(c.sterge(5) == true);
	assert(c.dim() == 5);

	assert(c.cauta(6) == true);
	assert(c.vida() == false);

    IteratorColectie ic = c.iterator();

	assert(ic.valid() == true);
	while (ic.valid()) {
		ic.element();
		ic.urmator();
	}
	assert(ic.valid() == false);
    ic.prim();
	assert(ic.valid() == true);

}

void testAdaugaToateElementeScurt() {
    Colectie c1;
    c1.adauga(1);
    c1.adauga(2);
    c1.adauga(3);
    c1.adauga(1);
    c1.adauga(2);

    Colectie c2;
    c2.adauga(4);
    c2.adauga(5);
    c2.adauga(1);
    c2.adauga(3);

    c1.adaugaToateElemente(c2);

    assert(c1.dim() == 9);

    assert(c1.nrAparitii(1) == 3);
    assert(c1.nrAparitii(2) == 2);
    assert(c1.nrAparitii(3) == 2);
    assert(c1.nrAparitii(4) == 1);
    assert(c1.nrAparitii(5) == 1);
}
