#include <stdlib.h>
#include <string.h>
#include "list.h"
#include "oferte.h"
#include "service.h"
#include "validare.h"

Service *creeazaService() {
    Service *service = (Service *)malloc(sizeof(Service));
    service->oferte = creeazaLista((Distruge) distrugeOferta, (Copiaza) copiazaOferta);
    service->undoList = creeazaLista((Distruge) distrugeLista, (Copiaza) copiazaLista);
    return service;
}

void distrugeService(Service *service) {
    distrugeLista(service->oferte);
    distrugeLista(service->undoList);
    free(service);
}

int idExists(Service *service, int id) {
    for (int i = 0; i < service->oferte->lg; i++) {
        Oferta *oferta = (Oferta *)service->oferte->elems[i];
        if (oferta->id == id) {
            return 1;
        }
    }

    return 0;
}

int adaugaOferta(Service *service, Oferta *oferta) {
    if (validare_oferta(oferta) == 0 || idExists(service, oferta->id) == 1)
        return 0;

    List *copy = copiazaLista(service->oferte);
    add(service->undoList, copy);

    add(service->oferte, oferta);
    return 1;
}

int modificaTip(Service *service, int id, char *tip) {
    if (validare_id(id) == 0 || validare_tip(tip) == 0)
        return 0;

    List *copy = copiazaLista(service->oferte);
    add(service->undoList, copy);

    for (int i = 0; i < service->oferte->lg; i++) {
        Oferta *oferta = (Oferta *)service->oferte->elems[i];
        if (oferta->id == id) {
            free(oferta->tip);
            oferta->tip = (char *)malloc(sizeof(char) * strlen(tip) + 1);
            strcpy(oferta->tip, tip);
            return 1;
        }
    }

    return 0;
}

int modificaAdresa(Service *service, int id, char *adresa) {
    if (validare_id(id) == 0 || validare_adresa(adresa) == 0)
        return 0;

    List *copy = copiazaLista(service->oferte);
    add(service->undoList, copy);

    for (int i = 0; i < service->oferte->lg; i++) {
        Oferta *oferta = (Oferta *)service->oferte->elems[i];
        if (oferta->id == id) {
            free(oferta->adresa);
            oferta->adresa = (char *)malloc(sizeof(char) * strlen(adresa) + 1);
            strcpy(oferta->adresa, adresa);
            return 1;
        }
    }

    return 0;
}

int modificaSuprafata(Service *service, int id, int suprafata) {
    if (validare_id(id) == 0 || validare_suprafata(suprafata) == 0)
        return 0;

    List *copy = copiazaLista(service->oferte);
    add(service->undoList, copy);

    for (int i = 0; i < service->oferte->lg; i++) {
        Oferta *oferta = (Oferta *)service->oferte->elems[i];
        if (oferta->id == id) {
            oferta->suprafata = suprafata;
            return 1;
        }
    }

    return 0;
}

int modificaPret(Service *service, int id, int pret) {
    if (validare_id(id) == 0 || validare_pret(pret) == 0)
        return 0;

    List *copy = copiazaLista(service->oferte);
    add(service->undoList, copy);

    for (int i = 0; i < service->oferte->lg; i++) {
        Oferta *oferta = (Oferta *)service->oferte->elems[i];
        if (oferta->id == id) {
            oferta->pret = pret;
            return 1;
        }
    }

    return 0;
}

int stergeOferta(Service *service, int id) {
    if (validare_id(id) == 0)
        return 0;

    List *copy = copiazaLista(service->oferte);
    add(service->undoList, copy);

    for (int i = 0; i < service->oferte->lg; i++) {
        Oferta *oferta = (Oferta *)service->oferte->elems[i];
        if (oferta->id == id) {
            service->oferte->distruge(service->oferte->elems[i]);
            for (int j = i; j < service->oferte->lg - 1; j++) {
                service->oferte->elems[j] = service->oferte->elems[j + 1];
            }
            service->oferte->lg--;
            return 1;
        }
    }

    return 0;
}

List *oferteOrdonateDupaPret(Service *service, int mod) {
    List *listaNoua = copiazaLista(service->oferte);

    for (int i = 0; i < listaNoua->lg - 1; i++) {
        for (int j = i + 1; j < listaNoua->lg; j++) {
            Oferta *oferta1 = (Oferta *)listaNoua->elems[i];
            Oferta *oferta2 = (Oferta *)listaNoua->elems[j];
            if ((mod == 0 && oferta1->pret > oferta2->pret) ||
                (mod == 1 && oferta1->pret < oferta2->pret)) {
                listaNoua->elems[i] = oferta2;
                listaNoua->elems[j] = oferta1;
            }
        }
    }

    return listaNoua;
}

List *oferteOrdonateDupaTip(Service *service, int mod) {
    List *listaNoua = copiazaLista(service->oferte);

    for (int i = 0; i < listaNoua->lg - 1; i++) {
        for (int j = i + 1; j < listaNoua->lg; j++) {
            Oferta *oferta1 = (Oferta *)listaNoua->elems[i];
            Oferta *oferta2 = (Oferta *)listaNoua->elems[j];
            if ((mod == 0 && strcmp(oferta1->tip, oferta2->tip) > 0) ||
                (mod == 1 && strcmp(oferta1->tip, oferta2->tip) < 0)) {
                listaNoua->elems[i] = oferta2;
                listaNoua->elems[j] = oferta1;
            }
        }
    }

    return listaNoua;
}

List *oferteFiltrateDupaTip(Service *service, char *tip) {
    List *listaNoua = creeazaLista((Distruge) distrugeOferta, (Copiaza) copiazaOferta);

    for (int i = 0; i < service->oferte->lg; i++) {
        Oferta *oferta = (Oferta *)service->oferte->elems[i];
        if (strcmp(oferta->tip, tip) == 0) {
            add(listaNoua, copiazaOferta(oferta));
        }
    }

    return listaNoua;
}

List *oferteFiltrateDupaSuprafata(Service *service, int suprafata) {
    List *listaNoua = creeazaLista((Distruge) distrugeOferta, (Copiaza) copiazaOferta);

    for (int i = 0; i < service->oferte->lg; i++) {
        Oferta *oferta = (Oferta *)service->oferte->elems[i];
        if (oferta->suprafata == suprafata) {
            add(listaNoua, copiazaOferta(oferta));
        }
    }

    return listaNoua;
}

int undo(Service *service) {
    if (lungime(service->undoList) == 0)
        return 0;

    List *lista = pop(service->undoList);
    distrugeLista(service->oferte);
    service->oferte = lista;
    return 1;
}
