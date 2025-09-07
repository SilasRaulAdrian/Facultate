#ifndef SERVICE_H
#define SERVICE_H

#include "list.h"
#include "produs.h"

typedef struct {
    List *produse;
} Service;

/**
 * Functie care creeaza un service
 * @return Service-ul creat
 */
Service *creeazaService();

/**
 * Functie care distruge un service
 * @param service Service-ul de distrus
 */
void distrugeService(Service *service);

/**
 * Functie care adauga un produs
 * @param service Service-ul de modificat
 * @param produs Produsul de adaugat
 * @return 1 daca produsul a fost adaugat, 0 altfel
 */
int adaugaProdus(Service *service, Produs *produs);

/**
 * Functie care sterge un produs
 * @param service Service-ul de modificat
 * @param id ID-ul produsului de sters
 * @return 1 daca produsul a fost sters, 0 altfel
 */
int stergeProdus(Service *service, int id);

/**
 * Functie care modifica pretul unui produs dupa ID
 * @param service Service-ul de modificat
 * @param id ID-ul produsului de modificat
 * @param pret Noul pret al produsului
 * @return 1 daca pretul a fost modificat, 0 altfel
 */
int modificaPret(Service *service, int id, float pret);

/**
 * Functie care modifica cantitatea unui produs dupa ID
 * @param service Service-ul de modificat
 * @param id ID-ul produsului de modificat
 * @param cantitate Noua cantitate a produsului
 * @return 1 daca cantitatea a fost modificata, 0 altfel
 */
int modificaCantitate(Service *service, int id, int cantitate);

/**
 * Functie care ordoneaza produsele ordonate dupa pret crescator sau descrescator in functie de mod
 * @param service Service-ul de afisat
 * @param mod 1 pentru ordonare crescatoare, 0 pentru ordonare descrescatoare
 * @return Lista de produse ordonate
 */
List *ordoneazaProduseDupaPret(Service *service, int mod);

/**
 * Functie care ordoneaza produsele dupa cantitate crescator sau descrescator in functie de mod
 * @param service Service-ul de ordonat
 * @param mod 1 pentru ordonare crescatoare, 0 pentru ordonare descrescatoare
 * @return Lista de produse ordonate
 */
List *ordoneazaProduseDupaCantitate(Service *service, int mod);

/**
 * Functie care filtreaza produsele care au cantitatea data
 * @param service Service-ul de afisat
 * @param cantitate Cantitatea dupa care se filtreaza produsele
 * @return Lista de produse filtrate
 */
List *filtreazaProduseDupaCantitate(Service *service, int cantitate);

#endif // SERVICE_H