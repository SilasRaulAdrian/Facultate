#include "repo_oferte.h"

#include <stdlib.h>
#include <string.h>

#define INIT_CAPACITY 10

Repo* creeazaRepo() {
    Repo* repo = (Repo*)malloc(sizeof(Repo));
    repo->lungime = 0;
    repo->dimensiune = INIT_CAPACITY;
    repo->oferte = (Oferta**)malloc(sizeof(Oferta*) * INIT_CAPACITY);
    return repo;
}

void distruge_repo(Repo *repo) {
    if (repo == NULL) return;

    if (repo->oferte != NULL) {
        for (int i = 0; i < repo->lungime; i++) {
            if (repo->oferte[i] != NULL) {
                distruge_oferta(repo->oferte[i]);
                repo->oferte[i] = NULL;
            }
        }
        free(repo->oferte);
        repo->oferte = NULL;
    }

    free(repo);
}

Repo* copie_repo(Repo* repo) {
    Repo* nou = creeazaRepo();

    for (int i = 0; i < repo->lungime; i++) {
        Oferta* oferta_noua = creeaza_oferta(
            repo->oferte[i]->id,
            repo->oferte[i]->tip,
            repo->oferte[i]->suprafata,
            repo->oferte[i]->adresa,
            repo->oferte[i]->pret
        );
        adauga_oferta(nou, oferta_noua);
    }

    return nou;
}

int id_exists(Repo *repo, int id) {
    for (int i = 0; i < repo->lungime; i++) {
        if (repo->oferte[i]->id == id) {
            return 1;
        }
    }

    return 0;
}

int adauga_oferta(Repo *repo, Oferta *oferta) {
    if (id_exists(repo, oferta->id) == 0) {
        if (repo->lungime == repo->dimensiune) {
            int dimensiune_noua = repo->dimensiune * 2;
            Oferta** oferte_noi = (Oferta**)malloc(sizeof(Oferta*) * dimensiune_noua);
            for (int i = 0; i < repo->lungime; i++) {
                oferte_noi[i] = repo->oferte[i];
            }
            free(repo->oferte);
            repo->oferte = oferte_noi;
            repo->dimensiune = dimensiune_noua;
        }
        repo->oferte[repo->lungime] = oferta;
        repo->lungime = repo->lungime + 1;
        return 1;
    }

    return 0;
}

int modifica_tip(Repo *repo, char *tip, int id) {
    if (id_exists(repo, id) == 1) {
        for (int i = 0; i < repo->lungime; i++) {
            if (repo->oferte[i]->id == id) {
                free(repo->oferte[i]->tip);
                repo->oferte[i]->tip = (char*)malloc(sizeof(char) * strlen(tip) + 1);
                strcpy(repo->oferte[i]->tip, tip);
                return 1;
            }
        }
    }

    return 0;
}

int modifica_adresa(Repo *repo, char *adresa, int id) {
    if (id_exists(repo, id) == 1) {
        for (int i = 0; i < repo->lungime; i++) {
            if (repo->oferte[i]->id == id) {
                free(repo->oferte[i]->adresa);
                repo->oferte[i]->adresa = (char*)malloc(sizeof(char) * strlen(adresa) + 1);
                strcpy(repo->oferte[i]->adresa, adresa);
                return 1;
            }
        }
    }

    return 0;
}

int modifica_pret(Repo *repo, int pret, int id) {
    if (id_exists(repo, id) == 1) {
        for (int i = 0; i < repo->lungime; i++) {
            if (repo->oferte[i]->id == id) {
                repo->oferte[i]->pret = pret;
                return 1;
            }
        }
    }

    return 0;
}

int modifica_suprafata(Repo *repo, int suprafata, int id) {
    if (id_exists(repo, id) == 1) {
        for (int i = 0; i < repo->lungime; i++) {
            if (repo->oferte[i]->id == id) {
                repo->oferte[i]->suprafata = suprafata;
                return 1;
            }
        }
    }

    return 0;
}

int sterge_oferta(Repo *repo, int id) {
    int poz = -1;
    if (id_exists(repo, id) == 0 || repo->lungime <= 0)
        return 0;

    for (int i = 0; i < repo->lungime; i++) {
        if (repo->oferte[i]->id == id) {
            poz = i;
            break;
        }
    }

    for (int i = poz; i < repo->lungime - 1; i++) {
        repo->oferte[i] = repo->oferte[i + 1];
    }
    repo->lungime--;

    return 1;
}