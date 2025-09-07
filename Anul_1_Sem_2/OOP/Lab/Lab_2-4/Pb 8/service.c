#include <stdlib.h>
#include "list.h"
#include "produs.h"
#include "service.h"
#include "validare.h"

Service *creeazaService() {
    Service *service = (Service *)malloc(sizeof(Service));
    service->produse = creeazaLista((Distruge) distrugeProdus, (Copiaza) copieProdus);
    return service;
}

void distrugeService(Service *service) {
    distrugeLista(service->produse);
    free(service);
}

int adaugaProdus(Service *service, Produs *produs) {
    if (validareProdus(produs) == 0)
        return 0;

    for (int i = 0; i < service->produse->lg; i++) {
        Produs *produsExistent = (Produs *)service->produse->elems[i];
        if (produsExistent->id == produs->id) {
            produsExistent->cantitate += produs->cantitate;
            return 0;
        }
    }

    add(service->produse, produs);

    return 1;
}

int stergeProdus(Service *service, int id) {
    if (validareId(id) == 0)
        return 0;

    for (int i = 0; i < service->produse->lg; i++) {
        Produs *produs = (Produs *)service->produse->elems[i];
        if (produs->id == id) {
            service->produse->distruge(service->produse->elems[i]);
            for (int j = i; j < service->produse->lg - 1; j++) {
                service->produse->elems[j] = service->produse->elems[j + 1];
            }
            service->produse->lg--;
            return 1;
        }
    }

    return 0;
}

int modificaPret(Service *service, int id, float pret) {
    if (validareId(id) == 0 || validarePret(pret) == 0)
        return 0;

    for (int i = 0; i < service->produse->lg; i++) {
        Produs *produs = (Produs *)service->produse->elems[i];
        if (produs->id == id) {
            produs->pret = pret;
            return 1;
        }
    }

    return 0;
}

int modificaCantitate(Service *service, int id, int cantitate) {
    if (validareId(id) == 0 || validareCantitate(cantitate) == 0)
        return 0;

    for (int i = 0; i < service->produse->lg; i++) {
        Produs *produs = (Produs *)service->produse->elems[i];
        if (produs->id == id) {
            produs->cantitate = cantitate;
            return 1;
        }
    }

    return 0;
}

List *ordoneazaProduseDupaPret(Service *service, int mod) {
    List *listaNoua = copiazaLista(service->produse);

    for (int i = 0; i < listaNoua->lg - 1; i++) {
        for (int j = i + 1; j < listaNoua->lg; j++) {
            Produs *produs1 = (Produs *)listaNoua->elems[i];
            Produs *produs2 = (Produs *)listaNoua->elems[j];
            if ((mod == 0 && produs1->pret > produs2->pret) ||
                (mod == 1 && produs1->pret < produs2->pret)) {
                listaNoua->elems[i] = produs2;
                listaNoua->elems[j] = produs1;
            }
        }
    }

    return listaNoua;
}

List *ordoneazaProduseDupaCantitate(Service *service, int mod) {
    List *listaNoua = copiazaLista(service->produse);

    for (int i = 0; i < listaNoua->lg - 1; i++) {
        for (int j = i + 1; j < listaNoua->lg; j++) {
            Produs *produs1 = (Produs *)listaNoua->elems[i];
            Produs *produs2 = (Produs *)listaNoua->elems[j];
            if ((mod == 0 && produs1->cantitate > produs2->cantitate) ||
                (mod == 1 && produs1->cantitate < produs2->cantitate)) {
                listaNoua->elems[i] = produs2;
                listaNoua->elems[j] = produs1;
            }
        }
    }

    return listaNoua;
}

List *filtreazaProduseDupaCantitate(Service *service, int cantitate) {
    List *listaNoua = creeazaLista((Distruge) distrugeProdus, (Copiaza) copieProdus);

    for (int i = 0; i < service->produse->lg; i++) {
        Produs *produs = (Produs *)service->produse->elems[i];
        if (produs->cantitate == cantitate) {
            add(listaNoua, copieProdus(produs));
        }
    }

    return listaNoua;
}