#include <stdio.h>
#include <stdlib.h>
#include "ui.h"
#include "service.h"
#include "oferte.h"
#include "list.h"

void meniu() {
    printf("-----------------------------------------------------------\n");
    printf("1. Adauga oferta\n");
    printf("2. Sterge oferta\n");
    printf("3. Modifica tip oferta\n");
    printf("4. Modifica adresa oferta\n");
    printf("5. Modifica pret oferta\n");
    printf("6. Modifica suprafata oferta\n");
    printf("7. Afiseaza oferte\n");
    printf("8. Oferte ordonate dupa pret\n");
    printf("9. Oferte ordonate dupa tip\n");
    printf("10. Oferte filtrate dupa tip\n");
    printf("11. Oferte filtrate dupa suprafata\n");
    printf("12. Undo\n");
    printf("0. Iesi din aplicatie\n\n");
}

void run() {
    Service *service = creeazaService();
    int optiune;
    do {
        meniu();
        printf("Introduceti optiunea: ");
        if (scanf("%d", &optiune) == 0) {
            printf("Optiune invalida.\n");
            while (getchar() != '\n') {}
            continue;
        }
        switch (optiune) {
            case 1: {
                int id, suprafata, pret;
                char *tip = (char *) malloc(sizeof(char) * 100);
                char *adresa = (char *) malloc(sizeof(char) * 100);
                printf("Introduceti id-ul ofertei: ");
                if (scanf("%d", &id) == 0) {
                    printf("Id invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                printf("Introduceti tipul ofertei: ");
                if (scanf("%s", tip) == 0) {
                    printf("Tip invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                printf("Introduceti suprafata ofertei: ");
                if (scanf("%d", &suprafata) == 0) {
                    printf("Suprafata invalida.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                printf("Introduceti adresa ofertei: ");
                if (scanf("%s", adresa) == 0) {
                    printf("Adresa invalida.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                printf("Introduceti pretul ofertei: ");
                if (scanf("%d", &pret) == 0) {
                    printf("Pret invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                Oferta *oferta = creeazaOferta(id, tip, suprafata, adresa, pret);
                if (adaugaOferta(service, oferta) == 1) {
                    printf("Oferta adaugata cu succes!\n");
                    free(tip);
                    free(adresa);
                } else {
                    distrugeOferta(oferta);
                    free(tip);
                    free(adresa);
                    printf("Oferta nu a putut fi adaugata.\n");
                }
                break;
            }
            case 2: {
                int id;
                printf("Introduceti id-ul ofertei: ");
                if (scanf("%d", &id) == 0) {
                    printf("Id invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                if (stergeOferta(service, id) == 1) {
                    printf("Oferta stearsa cu succes!\n");
                } else {
                    printf("Oferta nu a putut fi stearsa.\n");
                }
                break;
            }
            case 3: {
                int id;
                char *tip = (char *) malloc(sizeof(char) * 100);
                printf("Introduceti id-ul ofertei: ");
                if (scanf("%d", &id) == 0) {
                    printf("Id invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                printf("Introduceti noul tip: ");
                if (scanf("%s", tip) == 0) {
                    printf("Tip invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                if (modificaTip(service, id, tip) == 1) {
                    printf("Tip modificat cu succes!\n");
                    free(tip);
                } else {
                    printf("Tipul nu a putut fi modificat.\n");
                    free(tip);
                }
                break;
            }
            case 4: {
                int id;
                char *adresa = (char *) malloc(sizeof(char) * 100);
                printf("Introduceti id-ul ofertei: ");
                if (scanf("%d", &id) == 0) {
                    printf("Id invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                printf("Introduceti noua adresa: ");
                if (scanf("%s", adresa) == 0) {
                    printf("Adresa invalida.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                if (modificaAdresa(service, id, adresa) == 1) {
                    printf("Adresa modificata cu succes!\n");
                    free(adresa);
                } else {
                    printf("Adresa nu a putut fi modificata.\n");
                    free(adresa);
                }
                break;
            }
            case 5: {
                int id, pret;
                printf("Introduceti id-ul ofertei: ");
                if (scanf("%d", &id) == 0) {
                    printf("Id invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                printf("Introduceti noul pret: ");
                if (scanf("%d", &pret) == 0) {
                    printf("Pret invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                if (modificaPret(service, id, pret) == 1) {
                    printf("Pret modificat cu succes!\n");
                } else {
                    printf("Pretul nu a putut fi modificat.\n");
                }
                break;
            }
            case 6: {
                int id, suprafata;
                printf("Introduceti id-ul ofertei: ");
                if (scanf("%d", &id) == 0) {
                    printf("Id invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                printf("Introduceti noua suprafata: ");
                if (scanf("%d", &suprafata) == 0) {
                    printf("Suprafata invalida.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                if (modificaSuprafata(service, id, suprafata) == 1) {
                    printf("Suprafata modificata cu succes!\n");
                } else {
                    printf("Suprafata nu a putut fi modificata.\n");
                }
                break;
            }
            case 7: {
                if (service->oferte->lg == 0) {
                    printf("Nu exista oferte.\n");
                } else {
                    for (int i = 0; i < service->oferte->lg; i++) {
                        Oferta *oferta = (Oferta *) service->oferte->elems[i];
                        printf("Id: %d, Tip: %s, Suprafata: %d, Adresa: %s, Pret: %d\n",
                               oferta->id, oferta->tip, oferta->suprafata, oferta->adresa, oferta->pret);
                    }
                }
                break;
            }
            case 8: {
                int mod;
                printf("Introduceti modul de ordonare (0 - crescator, 1 - descrescator): ");
                if (scanf("%d", &mod) == 0) {
                    printf("Mod invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                List *listaNoua = oferteOrdonateDupaPret(service, mod);
                if (listaNoua->lg == 0) {
                    printf("Nu exista oferte.\n");
                } else {
                    for (int i = 0; i < listaNoua->lg; i++) {
                        Oferta *oferta = (Oferta *) listaNoua->elems[i];
                        printf("Id: %d, Tip: %s, Suprafata: %d, Adresa: %s, Pret: %d\n",
                               oferta->id, oferta->tip, oferta->suprafata, oferta->adresa, oferta->pret);
                    }
                }
                break;
            }
            case 9: {
                int mod;
                printf("Introduceti modul de ordonare (0 - crescator, 1 - descrescator): ");
                if (scanf("%d", &mod) == 0) {
                    printf("Mod invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                List *listaNoua = oferteOrdonateDupaTip(service, mod);
                if (listaNoua->lg == 0) {
                    printf("Nu exista oferte.\n");
                } else {
                    for (int i = 0; i < listaNoua->lg; i++) {
                        Oferta *oferta = (Oferta *) listaNoua->elems[i];
                        printf("Id: %d, Tip: %s, Suprafata: %d, Adresa: %s, Pret: %d\n",
                               oferta->id, oferta->tip, oferta->suprafata, oferta->adresa, oferta->pret);
                    }
                }
                break;
            }
            case 10: {
                char *tip = (char *) malloc(sizeof(char) * 100);
                printf("Introduceti tipul ofertei: ");
                if (scanf("%s", tip) == 0) {
                    printf("Tip invalid.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                List *listaNoua = oferteFiltrateDupaTip(service, tip);
                if (listaNoua->lg == 0) {
                    printf("Nu exista oferte.\n");
                } else {
                    for (int i = 0; i < listaNoua->lg; i++) {
                        Oferta *oferta = (Oferta *) listaNoua->elems[i];
                        printf("Id: %d, Tip: %s, Suprafata: %d, Adresa: %s, Pret: %d\n",
                               oferta->id, oferta->tip, oferta->suprafata, oferta->adresa, oferta->pret);
                    }
                }
                distrugeLista(listaNoua);
                break;
            }
            case 11: {
                int suprafata;
                printf("Introduceti suprafata ofertei: ");
                if (scanf("%d", &suprafata) == 0) {
                    printf("Suprafata invalida.\n");
                    while (getchar() != '\n') {}
                    continue;
                }
                List *listaNoua = oferteFiltrateDupaSuprafata(service, suprafata);
                if (listaNoua->lg == 0) {
                    printf("Nu exista oferte.\n");
                } else {
                    for (int i = 0; i < listaNoua->lg; i++) {
                        Oferta *oferta = (Oferta *) listaNoua->elems[i];
                        printf("Id: %d, Tip: %s, Suprafata: %d, Adresa: %s, Pret: %d\n",
                               oferta->id, oferta->tip, oferta->suprafata, oferta->adresa, oferta->pret);
                    }
                }
                distrugeLista(listaNoua);
                break;
            }
            case 12: {
                if (undo(service) == 1) {
                    printf("Operatie de undo efectuata cu succes!\n");
                } else {
                    printf("Nu se poate efectua operatia de undo.\n");
                }
                break;
            }
            case 0: {
                printf("Aplicatia s-a inchis.\n");
                distrugeService(service);
                break;
            }
            default: {
                printf("Optiune invalida.\n");
                break;
            }
        }
    } while (optiune != 0);
}

