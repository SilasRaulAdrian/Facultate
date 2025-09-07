#include "validare.h"
#include <string.h>

int validareId(int id) {
    if (id <= 0)
        return 0;
    return 1;
}

int validareTip(char *tip) {
    if (strcmp(tip, "") == 0)
        return 0;
    return 1;
}

int validareProducator(char *producator) {
    if (strcmp(producator, "") == 0)
        return 0;
    return 1;
}

int validareModel(char *model) {
    if (strcmp(model, "") == 0)
        return 0;
    return 1;
}

int validarePret(float pret) {
    if (pret < 0)
        return 0;
    return 1;
}

int validareCantitate(int cantitate) {
    if (cantitate < 0)
        return 0;
    return 1;
}

int validareProdus(Produs *produs) {
    return validareId(produs->id) && validareTip(produs->tip) && validareProducator(produs->producator) && validareModel(produs->model) && validarePret(produs->pret) && validareCantitate(produs->cantitate);
}
