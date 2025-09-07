#include "service.h"
#include "ui.h"
#include "produs.h"
#include <stdio.h>
#include <stdlib.h>

void afiseazaMeniu() {
    printf("1. Adauga produs\n");
    printf("2. Sterge produs\n");
    printf("3. Modifica pret produs\n");
    printf("4. Modifica cantitate produs\n");
    printf("5. Afiseaza produse\n");
    printf("6. Produse ordonate dupa pret\n");
    printf("7. Produse ordonate dupa cantitate\n");
    printf("8. Produse filtrate dupa cantitate\n");
    printf("0. Iesire\n");
}

void gestionareProduse() {
    Service *service = creeazaService();
    int optiune;
    do {
        afiseazaMeniu();
        printf("Optiune: ");
        scanf("%d", &optiune);
        switch (optiune) {
            case 1: {
                int id;
                char *tip = (char *)malloc(sizeof(char) * 50 + 1);
                char *producator = (char *)malloc(sizeof(char) * 50 + 1);
                char *model = (char *)malloc(sizeof(char) * 50 + 1);
                float pret;
                int cantitate;

                printf("Id: ");
                scanf("%d", &id);
                printf("Tip: ");
                scanf("%s", tip);
                printf("Producator: ");
                scanf("%s", producator);
                printf("Model: ");
                scanf("%s", model);
                printf("Pret: ");
                scanf("%f", &pret);
                printf("Cantitate: ");
                scanf("%d", &cantitate);
                Produs *produs = creeazaProdus(id, tip, producator, model, pret, cantitate);

                if (adaugaProdus(service, produs) == 1) {
                    printf("Produsul a fost adaugat cu succes!\n");
                    free(tip);
                    free(producator);
                    free(model);
                }
                else {
                    distrugeProdus(produs);
                    free(tip);
                    free(producator);
                    free(model);
                    printf("Produsul nu a putut fi adaugat!\n");
                }

                break;
            }
            case 2: {
                int id;
                printf("Id: ");
                scanf("%d", &id);

                if (stergeProdus(service, id) == 1) {
                    printf("Produs sters cu succes!\n");
                }
                else {
                    printf("Produsul nu a putut fi sters!\n");
                }

                break;
            }
            case 3: {
                int id;
                float pret;
                printf("Id: ");
                scanf("%d", &id);
                printf("Pret: ");
                scanf("%f", &pret);

                if (modificaPret(service, id, pret) == 1) {
                    printf("Pret modificat cu succes!\n");
                }
                else {
                    printf("Pretul nu a putut fi modificat!\n");
                }

                break;
            }
            case 4: {
                int id;
                int cantitate;
                printf("Id: ");
                scanf("%d", &id);
                printf("Cantitate: ");
                scanf("%d", &cantitate);

                if (modificaCantitate(service, id, cantitate) == 1) {
                    printf("Cantitate modificata cu succes!\n");
                }
                else {
                    printf("Cantitatea nu a putut fi modificata!\n");
                }

                break;
            }
            case 5: {
                if (service->produse->lg == 0) {
                    printf("Nu exista produse!\n");
                }
                else {
                    for (int i = 0; i < service->produse->lg; i++) {
                        Produs *produs = (Produs *)service->produse->elems[i];
                        printf("Id: %d, Tip: %s, Producator: %s, Model: %s, Pret: %.2f, Cantitate: %d\n",
                               produs->id, produs->tip, produs->producator, produs->model, produs->pret, produs->cantitate);
                    }
                }

                break;
            }
            case 6: {
                int mod;
                printf("Introduceti modul de ordonare (0 - crescator, 1 - descrescator): ");
                scanf("%d", &mod);

                List *listaNoua = ordoneazaProduseDupaPret(service, mod);
                if (listaNoua->lg == 0) {
                    printf("Nu exista produse!\n");
                }
                else {
                    for (int i = 0; i < listaNoua->lg; i++) {
                        Produs *produs = (Produs *)listaNoua->elems[i];
                        printf("Id: %d, Tip: %s, Producator: %s, Model: %s, Pret: %.2f, Cantitate: %d\n",
                               produs->id, produs->tip, produs->producator, produs->model, produs->pret, produs->cantitate);
                    }
                }

                break;
            }
            case 7: {
                int mod;
                printf("Introduceti modul de ordonare (0 - crescator, 1 - descrescator): ");
                scanf("%d", &mod);

                List *listaNoua = ordoneazaProduseDupaCantitate(service, mod);
                if (listaNoua->lg == 0) {
                    printf("Nu exista produse!\n");
                }
                else {
                    for (int i = 0; i < listaNoua->lg; i++) {
                        Produs *produs = (Produs *)listaNoua->elems[i];
                        printf("Id: %d, Tip: %s, Producator: %s, Model: %s, Pret: %.2f, Cantitate: %d\n",
                               produs->id, produs->tip, produs->producator, produs->model, produs->pret, produs->cantitate);
                    }
                }

                break;
            }
            case 8: {
                int cantitate;
                printf("Introduceti cantitatea: ");
                scanf("%d", &cantitate);

                List *listaNoua = filtreazaProduseDupaCantitate(service, cantitate);
                if (listaNoua->lg == 0) {
                    printf("Nu exista produse!\n");
                }
                else {
                    for (int i = 0; i < listaNoua->lg; i++) {
                        Produs *produs = (Produs *)listaNoua->elems[i];
                        printf("Id: %d, Tip: %s, Producator: %s, Model: %s, Pret: %.2f, Cantitate: %d\n",
                               produs->id, produs->tip, produs->producator, produs->model, produs->pret, produs->cantitate);
                    }
                }
                distrugeLista(listaNoua);

                break;
            }
            case 0: {
                printf("Aplicatia s-a inchis!\n");
                distrugeService(service);
                break;
            }
            default: {
                printf("Optiune invalida!\n");
                break;
            }
        }
    } while (optiune != 0);
}
