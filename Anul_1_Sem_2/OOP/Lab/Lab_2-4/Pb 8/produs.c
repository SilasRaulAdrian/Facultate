#include "produs.h"
#include <string.h>
#include <stdlib.h>

Produs *creeazaProdus(int id, char *tip, char *producator, char *model, float pret, int cantitate) {
    Produs *produs = (Produs *)malloc(sizeof(Produs));

    produs->id = id;

    produs->tip = (char *)malloc(sizeof(char) * strlen(tip) + 1);
    strcpy(produs->tip, tip);

    produs->producator = (char *)malloc(sizeof(char) * strlen(producator) + 1);
    strcpy(produs->producator, producator);

    produs->model = (char *)malloc(sizeof(char) * strlen(model) + 1);
    strcpy(produs->model, model);

    produs->pret = pret;

    produs->cantitate = cantitate;

    return produs;
}

void distrugeProdus(Produs *produs)
{
    free(produs->tip);
    free(produs->producator);
    free(produs->model);
    free(produs);
}

Produs *copieProdus(Produs *produs)
{
    Produs *copie = (Produs *)malloc(sizeof(Produs));

    copie->id = produs->id;

    copie->tip = (char *)malloc(sizeof(char) * strlen(produs->tip) + 1);
    strcpy(copie->tip, produs->tip);

    copie->producator = (char *)malloc(sizeof(char) * strlen(produs->producator) + 1);
    strcpy(copie->producator, produs->producator);

    copie->model = (char *)malloc(sizeof(char) * strlen(produs->model) + 1);
    strcpy(copie->model, produs->model);

    copie->pret = produs->pret;

    copie->cantitate = produs->cantitate;

    return copie;
}