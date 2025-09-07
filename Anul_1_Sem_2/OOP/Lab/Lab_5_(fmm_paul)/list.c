#include <stdlib.h>
#include "list.h"

#define INIT_CAP 10

List *creeazaLista(Distruge distruge, Copiaza copiaza) {
    List *list = (List *)malloc(sizeof(List));
    if (list == NULL) {
        return NULL;
    }
    list->lg = 0;
    list->cap = INIT_CAP;
    list->elems = (TElem *)malloc(sizeof(TElem) * INIT_CAP);
    list->distruge = distruge;
    list->copiaza = copiaza;
    return list;
}

void distrugeLista(List *list) {
    for (int i = 0; i < list->lg; i++) {
        list->distruge(list->elems[i]);
    }
    free(list->elems);
    free(list);
}

void add(List *list, TElem elem) {
    // resize
    if (list->lg == list->cap) {
        list->cap *= 2;
        list->elems = (TElem *)realloc(list->elems, sizeof(TElem) * list->cap);
    }
    list->elems[list->lg++] = elem;
}

List *copiazaLista(List *list) {
    List *copie = creeazaLista(list->distruge, list->copiaza);
    for (int i = 0; i < list->lg; i++) {
        copie->elems[i] = list->copiaza(list->elems[i]);
    }
    copie->lg = list->lg;
    copie->cap = list->cap;
    return copie;
}

int lungime(List *list) {
    return list->lg;
}

TElem pop(List *list) {
    TElem elem = list->elems[list->lg - 1];
    list->lg--;
    return elem;
}
