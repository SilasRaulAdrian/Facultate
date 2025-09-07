#include <stdlib.h>
#include <string.h>
#include "oferte.h"

Oferta *creeazaOferta(int id, char *tip, int suprafata, char *adresa, int pret) {
    Oferta *oferta = (Oferta *)malloc(sizeof(Oferta));

    oferta->id = id;
    oferta->tip = (char*)malloc(sizeof(char) * strlen(tip) + 1);
    strcpy(oferta->tip, tip);

    oferta->suprafata = suprafata;
    oferta->adresa = (char*)malloc(sizeof(char) * strlen(adresa) + 1);
    strcpy(oferta->adresa, adresa);

    oferta->pret = pret;

    return oferta;
}

void distrugeOferta(Oferta *oferta) {
    if (oferta == NULL) return;

    free(oferta->tip);
    free(oferta->adresa);
    free(oferta);
}

Oferta *copiazaOferta(Oferta *oferta) {
    Oferta *new_oferta = (Oferta *)malloc(sizeof(Oferta));

    new_oferta->id = oferta->id;

    new_oferta->tip = (char *)malloc(sizeof(char) * strlen(oferta->tip) + 1);
    strcpy(new_oferta->tip, oferta->tip);

    new_oferta->suprafata = oferta->suprafata;
    new_oferta->pret = oferta->pret;

    new_oferta->adresa = (char *)malloc(sizeof(char) * strlen(oferta->adresa) + 1);
    strcpy(new_oferta->adresa, oferta->adresa);

    return new_oferta;
}