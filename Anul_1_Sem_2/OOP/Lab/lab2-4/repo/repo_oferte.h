#ifndef REPO_OFERTE_H
#define REPO_OFERTE_H

#include "../domain/oferta.h"

typedef struct repo {
    Oferta **oferte;
    int lungime;
    int dimensiune;
}Repo;

/**
 * Functie care creeaza un repo
 * @return Repo-ul creat
 */
Repo* creeazaRepo();

/**
 * Functie care creeaza o copie a unui repo
 * @param repo Repo-ul care va fi copiat
 * @return Copia repo-ului
 */
Repo* copie_repo(Repo *repo);

/**
 * Functie care distruge un repo
 * @param repo Repo-ul care va fi distrus
 */
void distruge_repo(Repo *repo);

/**
 * Functie care verifica daca un id exista in repo
 * @param repo Repo-ul in care se cauta id-ul
 * @param id Id-ul care se cauta
 * @return 1 daca id-ul exista, 0 altfel
 */
int id_exists(Repo *repo, int id);

/**
 * Functie care adauga o oferta in repo
 * @param repo Repo-ul in care se adauga oferta
 * @param oferta Oferta care va fi adaugata
 * @return 1 daca oferta a fost adaugata, 0 altfel
 */
int adauga_oferta(Repo *repo, Oferta *oferta);

/**
 * Functie care modifica tipul unei oferte
 * @param repo Repo-ul in care se face modificarea
 * @param tip Noul tip
 * @param id Id-ul ofertei care va fi modificata
 * @return 1 daca tipul a fost modificat, 0 altfel
 */
int modifica_tip(Repo *repo, char *tip, int id);

/**
 * Functie care modifica adresa unei oferte
 * @param repo Repo-ul in care se face modificarea
 * @param adresa Noua adresa
 * @param id Id-ul ofertei care va fi modificata
 * @return 1 daca adresa a fost modificata, 0 altfel
 */
int modifica_adresa(Repo *repo, char *adresa, int id);

/**
 * Functie care modifica pretul unei oferte
 * @param repo Repo-ul in care se face modificarea
 * @param pret Noul pret
 * @param id Id-ul ofertei care va fi modificata
 * @return 1 daca pretul a fost modificat, 0 altfel
 */
int modifica_pret(Repo *repo, int pret, int id);

/**
 * Functie care modifica suprafata unei oferte
 * @param repo Repo-ul in care se face modificarea
 * @param suprafata Noua suprafata
 * @param id Id-ul ofertei care va fi modificata
 * @return 1 daca suprafata a fost modificata, 0 altfel
 */
int modifica_suprafata(Repo *repo, int suprafata, int id);

/**
 * Functie care sterge o oferta din repo
 * @param repo Repo-ul din care se sterge oferta
 * @param id Id-ul ofertei care va fi stearsa
 * @return 1 daca oferta a fost stearsa, 0 altfel
 */
int sterge_oferta(Repo *repo, int id);

#endif // REPO_OFERTE_H
