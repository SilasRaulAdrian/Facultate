#ifndef SERVICE_H
#define SERVICE_H
#include "../repo/repo_oferte.h"
#include "../validator/validator.h"

typedef struct service {
    Repo *oferte;
    Repo **undo_stack;
    int undo_count;
    int undo_capacity;
}Service;

/**
 * Functie care creeaza un service
 * @return Service-ul creat
 */
Service *creeazaService();

/**
 * Functie care distruge un service
 * @param service Service-ul care va fi distrus
 */
void distrugeService(Service *service);

/**
 * Functie care adauga o oferta in service
 * @param service Service-ul in care se adauga oferta
 * @param oferta Oferta care va fi adaugata
 * @return 1 daca oferta a fost adaugata, 0 altfel
 */
int adauga_oferta_service(Service *service, Oferta *oferta);

/**
 * Functie care modifica tipul unei oferte
 * @param service Service-ul in care se face modificarea
 * @param tip Noul tip
 * @param id Id-ul ofertei care va fi modificata
 * @return 1 daca tipul a fost modificat, 0 altfel
 */
int modifica_tip_service(Service *service, int id, char *tip);

/**
 * Functie care modifica adresa unei oferte
 * @param service Service-ul in care se face modificarea
 * @param adresa Noua adresa
 * @param id Id-ul ofertei care va fi modificata
 * @return 1 daca adresa a fost modificata, 0 altfel
 */
int modifica_adresa_service(Service *service, int id, char *adresa);

/**
 * Functie care modifica pretul unei oferte
 * @param service Service-ul in care se face modificarea
 * @param pret Noul pret
 * @param id Id-ul ofertei care va fi modificata
 * @return 1 daca pretul a fost modificat, 0 altfel
 */
int modifica_pret_service(Service *service, int id, int pret);

/**
 * Functie care modifica suprafata unei oferte
 * @param service Service-ul in care se face modificarea
 * @param suprafata Noua suprafata
 * @param id Id-ul ofertei care va fi modificata
 * @return 1 daca suprafata a fost modificata, 0 altfel
 */
int modifica_suprafata_service(Service *service, int id, int suprafata);

/**
 * Functie care sterge o oferta din service
 * @param service Service-ul din care se sterge oferta
 * @param id Id-ul ofertei care va fi stearsa
 * @return 1 daca oferta a fost stearsa, 0 altfel
 */
int sterge_oferta_service(Service *service, int id);

/**
 * Returneaza un repository cu ofertele ordonate dupa pret.
 *
 * @param service Pointer catre service.
 * @param mod Mod de sortare (0 - crescator, 1 - descrescator).
 * @return Pointer catre repository-ul sortat.
 */
Repo *oferte_ordonate_pret(Service *service, int mod);

/**
 * Functie care returneaza toate ofertele din service ordonate dupa tip
 * @param service Service-ul din care se iau ofertele
 * @param mod 1 daca se doreste ordonare descrescatoare, 0 altfel
 * @return Lista de oferte
 */
Repo *oferte_ordonate_tip(Service *service, int mod);

/**
 * Functie care returneaza toate ofertele din service filtrate dupa tip
 * @param service Service-ul din care se iau ofertele
 * @param tip Tipul dupa care se filtreaza
 * @return Lista de oferte
 */
Repo *oferte_filtrate_tip(Service *service, char *tip);

/**
 * Functie care face undo la ultima operatie.
 * @param service Pointer catre service.
 */
void undo_service(Service *service);

#endif // SERVICE_H
