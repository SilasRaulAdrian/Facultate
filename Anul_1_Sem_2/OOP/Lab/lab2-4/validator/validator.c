#include "validator.h"
#include <string.h>

int validare_adresa(char *adresa) {
    if (strcmp(adresa, "") == 0)
        return 0;
    return 1;
}

int validare_pret(int pret) {
    if (pret < 0)
        return 0;
    return 1;
}

int validare_tip(char *tip) {
    if (strcmp(tip, "") == 0)
        return 0;
    if (strcmp(tip, "teren") == 0 || strcmp(tip, "casa") == 0 || strcmp(tip, "apartament") == 0)
        return 1;
    return 0;
}

int validare_suprafata(int suprafata) {
    if (suprafata < 0)
        return 0;
    return 1;
}

int validare_id(int id) {
    if (id <= 0)
        return 0;
    return 1;
}

int validare_oferta(Oferta *oferta) {
     return validare_id(oferta->id) && validare_adresa(oferta->adresa) && validare_pret(oferta->pret) && validare_tip(oferta->tip) && validare_suprafata(oferta->suprafata);
}