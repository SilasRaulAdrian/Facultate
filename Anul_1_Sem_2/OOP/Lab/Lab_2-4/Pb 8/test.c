#include "service.h"
#include "test.h"
#include "list.h"
#include "validare.h"
#include <assert.h>
#include <stdio.h>

#define INIT_CAP 10

void testList() {
    List *list = creeazaLista((Distruge) distrugeProdus, (Copiaza) copieProdus);
    assert(list->lg == 0);
    assert(list->cap == INIT_CAP);
    Produs *produs = creeazaProdus(1, "Laptop", "Asus", "Zenbook", 5000, 10);
    add(list, produs);
    List *copie = copiazaLista(list);
    assert(copie->lg == 1);
    assert(lungime(copie) == 1);
    List *altaLista = creeazaLista((Distruge) distrugeProdus, (Copiaza) copieProdus);
    Produs *produs1 = creeazaProdus(2, "Telefon", "Samsung", "Galaxy S20", 3000, 5);
    Produs *produs2 = creeazaProdus(3, "Tableta", "Apple", "iPad", 4000, 3);
    Produs *produs3 = creeazaProdus(4, "Laptop", "Dell", "Inspiron", 4500, 7);
    Produs *produs4 = creeazaProdus(5, "Telefon", "Apple", "iPhone 11", 3500, 8);
    Produs *produs5 = creeazaProdus(6, "Tableta", "Samsung", "Galaxy Tab", 2500, 6);
    Produs *produs6 = creeazaProdus(7, "Laptop", "Lenovo", "IdeaPad", 4200, 4);
    Produs *produs7 = creeazaProdus(8, "Telefon", "Huawei", "P40", 3200, 9);
    Produs *produs8 = creeazaProdus(9, "Tableta", "Huawei", "MediaPad", 2300, 2);
    Produs *produs9 = creeazaProdus(10, "Laptop", "HP", "Pavilion", 4100, 1);
    Produs *produs10 = creeazaProdus(11, "Telefon", "Xiaomi", "Mi 10", 3300, 0);
    Produs *produs11 = creeazaProdus(12, "Tableta", "Lenovo", "Tab", 2400, 0);
    add(altaLista, produs1);
    add(altaLista, produs2);
    add(altaLista, produs3);
    add(altaLista, produs4);
    add(altaLista, produs5);
    add(altaLista, produs6);
    add(altaLista, produs7);
    add(altaLista, produs8);
    add(altaLista, produs9);
    add(altaLista, produs10);
    add(altaLista, produs11);
    assert(altaLista->lg == 11);
    assert(altaLista->cap == 20);
    distrugeLista(list);
    distrugeLista(copie);
    distrugeLista(altaLista);
    printf("Testele pentru lista au trecut cu succes!\n");
}

void testService() {
    Service *service = creeazaService();
    assert(modificaPret(service, 1, 5000) == 0);
    assert(modificaCantitate(service, 1, 10) == 0);

    Produs *produs1 = creeazaProdus(1, "Laptop", "Asus", "Vivobook", 4000, 10);
    Produs *produs2 = creeazaProdus(2, "Telefon", "Samsung", "Galaxy S20", 3000, 5);
    Produs *produs_invalid = creeazaProdus(-1, "Laptop", "Asus", "Vivobook", 2000, 10);

    assert(adaugaProdus(service, produs1) == 1);
    assert(adaugaProdus(service, produs2) == 1);
    assert(adaugaProdus(service, produs1) == 0);
    assert(adaugaProdus(service, produs_invalid) == 0);
    assert(service->produse->lg == 2);

    List *lista1 = ordoneazaProduseDupaPret(service, 0);
    assert(((Produs *)lista1->elems[0])->pret == 3000);
    assert(((Produs *)lista1->elems[1])->pret == 4000);
    distrugeLista(lista1);

    List *lista2 = ordoneazaProduseDupaPret(service, 1);
    assert(((Produs *)lista2->elems[0])->pret == 4000);
    assert(((Produs *)lista2->elems[1])->pret == 3000);
    distrugeLista(lista2);

    List *lista3 = ordoneazaProduseDupaCantitate(service, 0);
    assert(((Produs *)lista3->elems[0])->cantitate == 5);
    assert(((Produs *)lista3->elems[1])->cantitate == 20);
    distrugeLista(lista3);

    List *lista4 = ordoneazaProduseDupaCantitate(service, 1);
    assert(((Produs *)lista4->elems[0])->cantitate == 20);
    assert(((Produs *)lista4->elems[1])->cantitate == 5);
    distrugeLista(lista4);

    List *lista5 = filtreazaProduseDupaCantitate(service, 5);
    assert(lista5->lg == 1);
    distrugeLista(lista5);

    List *lista6 = filtreazaProduseDupaCantitate(service, 30);
    assert(lista6->lg == 0);
    distrugeLista(lista6);

    assert(modificaPret(service, 1, 5000) == 1);
    assert(modificaPret(service, 3, 5000) == 0);
    assert(modificaPret(service, 1, -1) == 0);

    assert(modificaCantitate(service, 1, 5) == 1);
    assert(modificaCantitate(service, 3, 5) == 0);
    assert(modificaCantitate(service, 1, -1) == 0);

    assert(stergeProdus(service, 1) == 1);
    assert(stergeProdus(service, 1) == 0);
    assert(stergeProdus(service, -1) == 0);
    assert(service->produse->lg == 1);

    distrugeService(service);
    distrugeProdus(produs_invalid);
    printf("Testele pentru service au trecut cu succes!\n");
}

void testValidare() {
    Produs *produs = creeazaProdus(1, "Laptop", "Asus", "Vivobook", 2000, 10);
    assert(validareProdus(produs));
    distrugeProdus(produs);

    produs = creeazaProdus(-1, "Laptop", "Asus", "Vivobook", 2000, 10);
    assert(!validareProdus(produs));
    distrugeProdus(produs);

    produs = creeazaProdus(1, "", "Asus", "Vivobook", 2000, 10);
    assert(!validareProdus(produs));
    distrugeProdus(produs);

    produs = creeazaProdus(1, "Laptop", "", "Vivobook", 2000, 10);
    assert(!validareProdus(produs));
    distrugeProdus(produs);

    produs = creeazaProdus(1, "Laptop", "Asus", "", 2000, 10);
    assert(!validareProdus(produs));
    distrugeProdus(produs);

    produs = creeazaProdus(1, "Laptop", "Asus", "Vivobook", -2000, 10);
    assert(!validareProdus(produs));
    distrugeProdus(produs);

    produs = creeazaProdus(1, "Laptop", "Asus", "Vivobook", 2000, -10);
    assert(!validareProdus(produs));
    distrugeProdus(produs);

    printf("Testele pentru validare au trecut cu succes!\n");
}

void testAll() {
    printf("Ruleaza testele...\n");
    testList();
    testService();
    testValidare();
    printf("Toate testele au trecut cu succes!\n");
}
