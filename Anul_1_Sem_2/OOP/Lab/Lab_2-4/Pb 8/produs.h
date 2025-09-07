#ifndef PRODUS_H
#define PRODUS_H

typedef struct {
    int id;
    char *tip;
    char *producator;
    char *model;
    float pret;
    int cantitate;
} Produs;

/**
 * Functie care creeaza un produs
 * @param id ID-ul produsului
 * @param tip Tipul produsului
 * @param producator Producatorul produsului
 * @param model Modelul produsului
 * @param pret Pretul produsului
 * @param cantitate Cantitatea produsului
 * @return Produsul creat
 */
Produs *creeazaProdus(int id, char *tip, char *producator, char *model, float pret, int cantitate);

/**
 * Functie care distruge un produs
 * @param produs Produsul de distrus
 */
void distrugeProdus(Produs *produs);

/**
 * Functie care copiaza un produs
 * @param produs Produsul de copiat
 * @return Copia produsului
 */
Produs *copieProdus(Produs *produs);

#endif // PRODUS_H