#include "service.h"

#include <stdlib.h>
#include <string.h>
#include <stdio.h>

Service* creeazaService() {
    Service *service = (Service*)malloc(sizeof(Service));
    service->oferte = creeazaRepo();
    service->undo_capacity = 10;
    service->undo_count = 0;
    service->undo_stack = (Repo**)malloc(service->undo_capacity * sizeof(Repo*));
    return service;
}

void distrugeService(Service *service) {
    if (service == NULL) return;

    for (int i = 0; i < service->undo_count; i++) {
        distruge_repo(service->undo_stack[i]);
    }
    free(service->undo_stack);
    distruge_repo(service->oferte);
    free(service);
}

void salveaza_stare(Service *service) {
    if (service->undo_count == service->undo_capacity) {
        service->undo_capacity *= 2;
        service->undo_stack = (Repo**)realloc(service->undo_stack, service->undo_capacity * sizeof(Repo*));
        if (service->undo_stack == NULL) return;
    }

    Repo *copie = copie_repo(service->oferte);
    service->undo_stack[service->undo_count++] = copie;
}

int adauga_oferta_service(Service *service, Oferta *oferta) {
    if (validare_oferta(oferta) == 0)
        return 0;

    salveaza_stare(service);

    if (adauga_oferta(service->oferte, oferta) == 0)
        return 0;

    return 1;
}

int modifica_tip_service(Service *service, int id, char *tip) {
    if (validare_id(id) == 0 || validare_tip(tip) == 0)
        return 0;

    salveaza_stare(service);

    if (modifica_tip(service->oferte, tip, id) == 0)
        return 0;

    return 1;
}

int modifica_adresa_service(Service *service, int id, char *adresa) {
    if (validare_id(id) == 0 || validare_adresa(adresa) == 0)
        return 0;

    salveaza_stare(service);

    if (modifica_adresa(service->oferte, adresa, id) == 0)
        return 0;

    return 1;
}

int modifica_pret_service(Service *service, int id, int pret) {
    if (validare_id(id) == 0 || validare_pret(pret) == 0)
        return 0;

    salveaza_stare(service);

    if (modifica_pret(service->oferte, pret, id) == 0)
        return 0;

    return 1;
}

int modifica_suprafata_service(Service *service, int id, int suprafata) {
    if (validare_id(id) == 0 || validare_suprafata(suprafata) == 0)
        return 0;

    salveaza_stare(service);

    if (modifica_suprafata(service->oferte, suprafata, id) == 0)
        return 0;

    return 1;
}

int sterge_oferta_service(Service *service, int id) {
    if (validare_id(id) == 0)
        return 0;

    salveaza_stare(service);

    if (sterge_oferta(service->oferte,id) == 0)
        return 0;

    return 1;
}

Repo* oferte_ordonate_pret(Service *service, int mod) {
    Repo *repo_nou = copie_repo(service->oferte);

    for (int i = 0; i < repo_nou->lungime - 1; i++) {
        for (int j = i + 1; j < repo_nou->lungime; j++) {
            if ((mod == 0 && repo_nou->oferte[i]->pret > repo_nou->oferte[j]->pret) ||
                (mod == 1 && repo_nou->oferte[i]->pret < repo_nou->oferte[j]->pret)) {
                Oferta *oferta = repo_nou->oferte[i];
                repo_nou->oferte[i] = repo_nou->oferte[j];
                repo_nou->oferte[j] = oferta;
                }
        }
    }

    return repo_nou;
}

Repo* oferte_ordonate_tip(Service *service, int mod) {
    Repo *repo_nou = copie_repo(service->oferte);

    for (int i = 0; i < repo_nou->lungime - 1; i++) {
        for (int j = i + 1; j < repo_nou->lungime; j++) {
            if ((mod == 0 && strcmp(repo_nou->oferte[i]->tip, repo_nou->oferte[j]->tip) > 0) ||
                (mod == 1 && strcmp(repo_nou->oferte[i]->tip, repo_nou->oferte[j]->tip) < 0)) {
                Oferta *oferta = repo_nou->oferte[i];
                repo_nou->oferte[i] = repo_nou->oferte[j];
                repo_nou->oferte[j] = oferta;
                }
        }
    }

    return repo_nou;
}

Repo* oferte_filtrate_tip(Service *service, char *tip) {
    if (validare_tip(tip) == 0)
        return NULL;

    Repo *repo_nou = creeazaRepo();

    for (int i = 0; i < service->oferte->lungime; i++) {
        if (strcmp(service->oferte->oferte[i]->tip, tip) == 0) {
            // 🔥 Create a new copy of the oferta object instead of copying the pointer
            Oferta *copie = creeaza_oferta(
                service->oferte->oferte[i]->id,
                service->oferte->oferte[i]->tip,
                service->oferte->oferte[i]->suprafata,
                service->oferte->oferte[i]->adresa,
                service->oferte->oferte[i]->pret
            );
            repo_nou->oferte[repo_nou->lungime] = copie;
            repo_nou->lungime++;
        }
    }

    return repo_nou;
}

void undo_service(Service *service) {
    if (service->undo_count == 0 || service == NULL)
        return;

    distruge_repo(service->oferte);

    service->undo_count--;
    service->oferte = service->undo_stack[service->undo_count];

    service->undo_stack[service->undo_count] = NULL;
}