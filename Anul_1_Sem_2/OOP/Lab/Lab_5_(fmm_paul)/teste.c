#include "service.h"
#include "teste.h"
#include "list.h"
#include "validare.h"
#include <assert.h>
#include <stdio.h>

#define INIT_CAP 10

void testList() {
    List *list = creeazaLista((Distruge) distrugeOferta, (Copiaza) copiazaOferta);
    assert(list->lg == 0);
    assert(list->cap == INIT_CAP);

    Oferta *oferta = creeazaOferta(1, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    add(list, oferta);
    List *copie = copiazaLista(list);
    assert(copie->lg == 1);
    assert(lungime(copie) == 1);
    pop(list);
    assert(list->lg == 0);

    List *altaLista = creeazaLista((Distruge) distrugeOferta, (Copiaza) copiazaOferta);
    Oferta *oferta1 = creeazaOferta(2, "casa", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta2 = creeazaOferta(3, "apartament", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta3 = creeazaOferta(4, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta4 = creeazaOferta(5, "casa", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta5 = creeazaOferta(6, "apartament", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta6 = creeazaOferta(7, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta7 = creeazaOferta(8, "casa", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta8 = creeazaOferta(9, "apartament", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta9 = creeazaOferta(10, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta10 = creeazaOferta(11, "casa", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta11 = creeazaOferta(12, "apartament", 1521, "Strada Mihai Viteazu 124", 100520);
    add(altaLista, oferta1);
    add(altaLista, oferta2);
    add(altaLista, oferta3);
    add(altaLista, oferta4);
    add(altaLista, oferta5);
    add(altaLista, oferta6);
    add(altaLista, oferta7);
    add(altaLista, oferta8);
    add(altaLista, oferta9);
    add(altaLista, oferta10);
    add(altaLista, oferta11);
    assert(altaLista->lg == 11);
    assert(altaLista->cap == 20);

    distrugeLista(list);
    distrugeLista(copie);
    distrugeLista(altaLista);
    distrugeOferta(oferta);

    printf("Testele pentru lista au trecut cu succes!\n");
}

void testService() {
    Service *service = creeazaService();
    assert(modificaTip(service, 1, "teren") == 0);
    assert(modificaAdresa(service, 1, "Strada Mihai Viteazu 124") == 0);
    assert(modificaPret(service, 1, 100520) == 0);
    assert(modificaSuprafata(service, 1, 1521) == 0);
    Oferta *oferta1 = creeazaOferta(1, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta2 = creeazaOferta(2, "casa", 1519, "Strada Mihai Viteazu 124", 100500);
    Oferta *oferta_invalid = creeazaOferta(-1, "teren", 1521, "Strada Mihai Viteazu 124", 100520);

    assert(adaugaOferta(service, oferta1) == 1);
    assert(adaugaOferta(service, oferta2) == 1);
    assert(adaugaOferta(service, oferta_invalid) == 0);
    assert(service->oferte->lg == 2);

    List *lista1 = oferteOrdonateDupaTip(service, 0); // ordonata crescator
    assert(((Oferta *)lista1->elems[0])->id == 2);
    assert(((Oferta *)lista1->elems[1])->id == 1);
    distrugeLista(lista1);

    List *lista2 = oferteOrdonateDupaTip(service, 1);
    assert(((Oferta *)lista2->elems[0])->id == 1);
    assert(((Oferta *)lista2->elems[1])->id == 2);
    distrugeLista(lista2);

    List *lista3 = oferteOrdonateDupaPret(service, 0);
    assert(((Oferta *)lista3->elems[0])->id == 2);
    assert(((Oferta *)lista3->elems[1])->id == 1);
    distrugeLista(lista3);

    List *lista4 = oferteOrdonateDupaPret(service, 1);
    assert(((Oferta *)lista4->elems[0])->id == 1);
    assert(((Oferta *)lista4->elems[1])->id == 2);
    distrugeLista(lista4);

    List *lista5 = oferteFiltrateDupaTip(service, "apartament");
    assert(lista5->lg == 0);
    distrugeLista(lista5);

    List *lista6 = oferteFiltrateDupaTip(service, "casa");
    assert(lista6->lg == 1);
    distrugeLista(lista6);

    List *lista7 = oferteFiltrateDupaSuprafata(service, 1521);
    assert(lista7->lg == 1);
    distrugeLista(lista7);

    List *lista8 = oferteFiltrateDupaSuprafata(service, 1000);
    assert(lista8->lg == 0);
    distrugeLista(lista8);

    assert(idExists(service, 1) == 1);
    assert(idExists(service, 3) == 0);

    assert(modificaTip(service, 1, "apartament") == 1);
    assert(modificaTip(service, 3, "apartament") == 0);
    assert(modificaTip(service, 1, "abc") == 0);
    assert(modificaTip(service, -1, "teren") == 0);

    assert(modificaAdresa(service, 1, "Strada Iuliu Maniu 12") == 1);
    assert(modificaAdresa(service, 3, "Strada Iuliu Maniu 12") == 0);
    assert(modificaAdresa(service, 1, "") == 0);
    assert(modificaAdresa(service, -1, "Strada Iuliu Maniu 12") == 0);

    assert(modificaPret(service, 1, 500000) == 1);
    assert(modificaPret(service, 3, 500000) == 0);
    assert(modificaPret(service, 1, -1) == 0);
    assert(modificaPret(service, -1, 500000) == 0);

    assert(modificaSuprafata(service, 1, 4000) == 1);
    assert(modificaSuprafata(service, 3, 4000) == 0);
    assert(modificaSuprafata(service, 1, -1) == 0);
    assert(modificaSuprafata(service, -1, 4000) == 0);

    assert(stergeOferta(service, 1) == 1);
    assert(stergeOferta(service, 1) == 0);
    assert(stergeOferta(service, -1) == 0);
    assert(service->oferte->lg == 1);

    distrugeService(service);
    distrugeOferta(oferta_invalid);

    printf("Testele pentru service au trecut cu succes!\n");
}

void testValidare() {
    Oferta *oferta = creeazaOferta(1, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    assert(validare_oferta(oferta));
    distrugeOferta(oferta);

    oferta = creeazaOferta(-1, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    assert(!validare_oferta(oferta));
    distrugeOferta(oferta);

    oferta = creeazaOferta(1, "", 1521, "Strada Mihai Viteazu 124", 100520);
    assert(!validare_oferta(oferta));
    distrugeOferta(oferta);

    oferta = creeazaOferta(1, "teren", 1521, "", 100520);
    assert(!validare_oferta(oferta));
    distrugeOferta(oferta);

    oferta = creeazaOferta(1, "teren", 1521, "Strada Mihai Viteazu 124", -100520);
    assert(!validare_oferta(oferta));
    distrugeOferta(oferta);

    oferta = creeazaOferta(1, "teren", -1521, "Strada Mihai Viteazu 124", 100520);
    assert(!validare_oferta(oferta));
    distrugeOferta(oferta);

    oferta = creeazaOferta(1, "abc", 1521, "Strada Mihai Viteazu 124", 100520);
    assert(!validare_oferta(oferta));
    distrugeOferta(oferta);

    printf("Testele pentru validare au trecut cu succes!\n");
}

void testUndo() {
    Service *service = creeazaService();
    Oferta *oferta1 = creeazaOferta(1, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta2 = creeazaOferta(2, "casa", 1521, "Strada Mihai Viteazu 124", 100500);
    assert(adaugaOferta(service, oferta1) == 1);
    assert(adaugaOferta(service, oferta2) == 1);
    assert(service->oferte->lg == 2);
    assert(undo(service) == 1);
    assert(service->oferte->lg == 1);
    assert(undo(service) == 1);
    assert(service->oferte->lg == 0);
    assert(undo(service) == 0);
    assert(service->oferte->lg == 0);

    distrugeService(service);

    printf("Testele pentru undo au trecut cu succes!\n");
}

void testAll() {
    printf("Ruleaza testele...\n");
    testList();
    testService();
    testValidare();
    testUndo();
    printf("Testele au trecut cu succes!\n");
}