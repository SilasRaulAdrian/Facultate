#ifndef LAB_5__FMM_PAUL__SERVICE_H
#define LAB_5__FMM_PAUL__SERVICE_H

#include "list.h"
#include "oferte.h"

typedef struct {
    List *oferte;
    List *undoList;
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
int adaugaOferta(Service *service, Oferta *oferta);

/**
 * Functie care modifica tipul unei oferte
 * @param service Service-ul in care se face modificarea
 * @param id Id-ul ofertei care va fi modificata
 * @param tip Noul tip
 * @return 1 daca tipul a fost modificat, 0 altfel
 */
int modificaTip(Service *service, int id, char *tip);

/**
 * Functie care modifica adresa unei oferte
 * @param service Service-ul in care se face modificarea
 * @param id Id-ul ofertei care va fi modificata
 * @param adresa Noua adresa
 * @return 1 daca adresa a fost modificata, 0 altfel
 */
int modificaAdresa(Service *service, int id, char *adresa);

/**
 * Functie care modifica suprafata unei oferte
 * @param service Service-ul in care se face modificarea
 * @param id Id-ul ofertei care va fi modificata
 * @param suprafata Noua suprafata
 * @return 1 daca suprafata a fost modificata, 0 altfel
 */
int modificaSuprafata(Service *service, int id, int suprafata);

/**
 * Functie care modifica pretul unei oferte
 * @param service Service-ul in care se face modificarea
 * @param id Id-ul ofertei care va fi modificata
 * @param pret Noul pret
 * @return 1 daca pretul a fost modificat, 0 altfel
 */
int modificaPret(Service *service, int id, int pret);

/**
 * Functie care sterge o oferta din service
 * @param service Service-ul din care se sterge oferta
 * @param id Id-ul ofertei care va fi stearsa
 * @return 1 daca oferta a fost stearsa, 0 altfel
 */
int stergeOferta(Service *service, int id);

/**
 * Functie care ordoneaza ofertele dupa pret crescator sau descrescator in functie de mod
 * @param service Service-ul in care se face ordonarea
 * @param mod 1 pentru ordonare crescatoare, 0 pentru ordonare descrescatoare
 * @return Lista de oferte ordonata
 */
List *oferteOrdonateDupaPret(Service *service, int mod);

/**
 * Functie care ordoneaza ofertele dupa tip crescator sau descrescator in functie de mod
 * @param service Service-ul in care se face ordonarea
 * @param mod 1 pentru ordonare crescatoare, 0 pentru ordonare descrescatoare
 * @return Lista de oferte ordonata
 */
List *oferteOrdonateDupaTip(Service *service, int mod);

/**
 * Functie care filtreaza ofertele dupa tip
 * @param service Service-ul in care se face filtrarea
 * @param tip Tipul dupa care se filtreaza
 * @return Lista de oferte filtrata
 */
List *oferteFiltrateDupaTip(Service *service, char *tip);

/**
 * Functie care filtreaza ofertele dupa suprafata
 * @param service Service-ul in care se face filtrarea
 * @param suprafata Suprafata dupa care se filtreaza
 * @return Lista de oferte filtrata
 */
List *oferteFiltrateDupaSuprafata(Service *service, int suprafata);

/**
 * Functie care face undo la ultima operatie
 * @param service Service-ul in care se face undo
 * @return 1 daca undo a fost facut, 0 altfel
 */
int undo(Service *service);

/**
 * Functie care verifica daca un id exista deja
 * @param service Service-ul in care se face verificarea
 * @param id Id-ul de verificat
 * @return 1 daca id-ul exista, 0 altfel
 */
int idExists(Service *service, int id);

#endif //LAB_5__FMM_PAUL__SERVICE_H
