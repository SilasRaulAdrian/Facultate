#include <assert.h>

#include "DO.h"
#include "Iterator.h"

#include <exception>
using namespace std;

bool relatie1(TCheie cheie1, TCheie cheie2) {
	if (cheie1 <= cheie2) {
		return true;
	}
	else {
		return false;
	}
}

void testDiferentaValori() {
    DO dictOrd = DO(relatie1);

    assert(dictOrd.diferentaValoareMaxMin() == -1);

    dictOrd.adauga(10, 5);
    assert(dictOrd.diferentaValoareMaxMin() == 0);

    dictOrd.adauga(20, 15);
    dictOrd.adauga(5, -10);
    dictOrd.adauga(15, 100);
    dictOrd.adauga(8, 3);

    assert(dictOrd.diferentaValoareMaxMin() == 110);

    dictOrd.adauga(6, 100);
    dictOrd.adauga(7, -10);
    assert(dictOrd.diferentaValoareMaxMin() == 110);

    dictOrd.sterge(15);
    assert(dictOrd.diferentaValoareMaxMin() == 110);

    dictOrd.sterge(6);
    assert(dictOrd.diferentaValoareMaxMin() == 25);

    dictOrd.sterge(5);
    dictOrd.sterge(7);
    dictOrd.sterge(8);
    dictOrd.sterge(10);
    dictOrd.sterge(20);
    assert(dictOrd.diferentaValoareMaxMin() == -1);
}

void testAll(){
	DO dictOrd = DO(relatie1);
	assert(dictOrd.dim() == 0);
	assert(dictOrd.vid());
    dictOrd.adauga(1,2);
    assert(dictOrd.dim() == 1);
    assert(!dictOrd.vid());
    assert(dictOrd.cauta(1)!=NULL_TVALOARE);
    TValoare v =dictOrd.adauga(1,3);
    assert(v == 2);
    assert(dictOrd.cauta(1) == 3);
    Iterator it = dictOrd.iterator();
    it.prim();
    while (it.valid()){
    	TElem e = it.element();
    	assert(e.second != NULL_TVALOARE);
    	it.urmator();
    }
    assert(dictOrd.sterge(1) == 3);
    assert(dictOrd.vid());
}

