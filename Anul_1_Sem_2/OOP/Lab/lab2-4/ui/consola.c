#include "consola.h"
#include <stdio.h>
#include <stdlib.h>

#include "../domain/oferta.h"
#include "../repo/repo_oferte.h"
#include "../service/service.h"

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
    printf("11. Undo\n");
    printf("0. Iesi din aplicatie\n\n");
}

int run() {
    Service *service = creeazaService();

    int optiune;
    while (1) {
        meniu();
        printf("Alege cerinta: ");
        if (scanf("%d", &optiune) == 0) {
            printf("Optiune invalida.\n");
            while (getchar() != '\n') {}
            continue;
        }
        if (optiune == 0) {
            distrugeService(service);
            printf("Aplicatia se inchide.\n");
            break;
        }
        if (optiune == 1) {

            int id;
            printf("Introdu id: ");
            if (scanf("%d", &id) == 0) {
                printf("Id invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            char *tip = (char*)malloc(sizeof(char) * 100);
            printf("Introdu tip: ");
            if (scanf("%s", tip) == 0) {
                printf("Tip invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            char *adresa = (char*)malloc(sizeof(char) * 100);
            printf("Introdu adresa: ");
            if (scanf(" %[^\n]", adresa) == 0) {
                printf("Adresa invalida.\n");
                while (getchar() != '\n') {}
                continue;
            }
            int pret;
            printf("Introdu pret: ");
            if (scanf("%d", &pret) == 0) {
                printf("Pret invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            int suprafata;
            printf("Introdu suprafata: ");
            if (scanf("%d", &suprafata) == 0) {
                printf("Suprafata invalida.\n");
                while (getchar() != '\n') {}
                continue;
            }
            Oferta *oferta = creeaza_oferta(id, tip, suprafata, adresa, pret);
            if (adauga_oferta_service(service, oferta) == 1) {
                printf("Oferta adaugata cu succes!\n");
            }
            else {
                distruge_oferta(oferta);
                free(tip);
                free(adresa);
                printf("Oferta invalida.\n");
            }
        }
        else if (optiune == 2) {
            int id;
            printf("Introdu id: ");
            scanf("%d", &id);
            if (sterge_oferta_service(service, id) == 1) {
                printf("Oferta stearsa cu succes!\n");
            }
            else
                printf("Oferta inexistenta.\n");
        }
        else if (optiune == 3) {
            int id;
            char *tip = (char*)malloc(sizeof(char) * 100);
            printf("Introdu id: ");
            if (scanf("%d", &id) == 0) {
                printf("Id invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            printf("Introdu tip nou: ");
            if (scanf("%s", tip) == 0) {
                printf("Tip invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            if (modifica_tip_service(service, id, tip) == 1) {
                printf("Tip modificat cu succes.\n");
            }
            else
                printf("Tipul sau id-ul invalid.\n");
        }
        else if (optiune == 4) {
            int id;
            char *adresa = (char*)malloc(sizeof(char) * 100);
            printf("Introdu id: ");
            if (scanf("%d", &id) == 0) {
                printf("Id invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            printf("Introdu adresa noua: ");
            if (scanf(" %[^\n]", adresa) == 0) {
                printf("Adresa invalida.\n");
                while (getchar() != '\n') {}
                continue;
            }
            if (modifica_adresa_service(service, id, adresa) == 1) {
                printf("Adresa modificata cu succes.\n");
            }
            else
                printf("Tipul invalid.\n");
        }
        else if (optiune == 5) {
            int id;
            int pret;
            printf("Introdu id: ");
            if (scanf("%d", &id) == 0) {
                printf("Id invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            printf("Introdu pret nou: ");
            if (scanf("%d", &pret) == 0) {
                printf("Pret invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            if (modifica_pret_service(service, id, pret) == 1) {
                printf("Pret modificat cu succes.\n");
            }
            else
                printf("Tipul sau pretul invalid.\n");
        }
        else if (optiune == 6) {
            int id;
            int suprafata;
            printf("Introdu id: ");
            if (scanf("%d", &id) == 0) {
                printf("Id invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            printf("Introdu suprafata noua: ");
            if (scanf("%d", &suprafata) == 0) {
                printf("Suprafata invalida.\n");
                while (getchar() != '\n') {}
                continue;
            }
            if (modifica_suprafata_service(service, id, suprafata) == 1) {
                printf("Suprafata modificata cu succes.\n");
            }
            else
                printf("Id-ul sau suprafata invalide.\n");
        }
        else if (optiune == 7) {
            if (service->oferte->lungime == 0)
                printf("Lista de oferte este goala.\n");
            else {
                for (int i = 0; i < service->oferte->lungime; i++) {
                    printf("id oferta: %d --- tip oferta: %s --- adresa oferta: %s --- pret oferta: %d --- suprafata oferta: %d\n", service->oferte->oferte[i]->id, service->oferte->oferte[i]->tip, service->oferte->oferte[i]->adresa, service->oferte->oferte[i]->pret, service->oferte->oferte[i]->suprafata);
                }
            }
        }
        else if (optiune == 8) {
            printf("Introdu 1 daca vrei sa fie ordonate descrescator sau 0 daca vrei sa fie ordonate crescator: ");
            int mod;
            if (scanf("%d", &mod) == 0) {
                printf("Mod invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            Repo* repo = oferte_ordonate_pret(service, mod);
            for (int i = 0; i < repo->lungime; i++) {
                printf("id oferta: %d --- tip oferta: %s --- adresa oferta: %s --- pret oferta: %d --- suprafata oferta: %d\n", repo->oferte[i]->id, repo->oferte[i]->tip, repo->oferte[i]->adresa, repo->oferte[i]->pret, repo->oferte[i]->suprafata);
            }
            free(repo);
        }
        else if (optiune == 9) {
            printf("Introdu 1 daca vrei sa fie ordonate descrescator sau 0 daca vrei sa fie ordonate crescator: ");
            int mod;
            if (scanf("%d", &mod) == 0) {
                printf("Mod invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            Repo *repo = oferte_ordonate_tip(service, mod);
            for (int i = 0; i < repo->lungime; i++) {
                printf("id oferta: %d --- tip oferta: %s --- adresa oferta: %s --- pret oferta: %d --- suprafata oferta: %d\n", repo->oferte[i]->id, repo->oferte[i]->tip, repo->oferte[i]->adresa, repo->oferte[i]->pret, repo->oferte[i]->suprafata);
            }
            free(repo);
        }
        else if (optiune == 10) {
            printf("Introdu tipul dupa care vrei sa fie filtrate ofertele: ");
            char *tip = (char*)malloc(sizeof(char) * 100);
            if (scanf("%s", tip) == 0) {
                printf("Tip invalid.\n");
                while (getchar() != '\n') {}
                continue;
            }
            Repo *repo = oferte_filtrate_tip(service, tip);
            if (repo == NULL) {
                printf("Tip invalid.\n");
                free(tip);
                continue;
            }
            if (repo->lungime == 0) {
                printf("Nu exista oferte cu tipul precizat.\n");
                free(tip);
                free(repo);
                continue;
            }
            for (int i = 0; i < repo->lungime; i++) {
                printf("id oferta: %d --- tip oferta: %s --- adresa oferta: %s --- pret oferta: %d --- suprafata oferta: %d\n", repo->oferte[i]->id, repo->oferte[i]->tip, repo->oferte[i]->adresa, repo->oferte[i]->pret, repo->oferte[i]->suprafata);
            }
            free(repo);
            free(tip);
        }
        else if (optiune == 11) {
            undo_service(service);
            free(service->undo_stack);
        }
        else {
            printf("Optiune invalida.\n");
        }
    }
    return 0;
}