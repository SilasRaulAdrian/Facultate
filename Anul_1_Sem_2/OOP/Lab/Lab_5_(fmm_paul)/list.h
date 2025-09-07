#ifndef LAB_5__FMM_PAUL__LIST_H
#define LAB_5__FMM_PAUL__LIST_H

typedef void *TElem;

typedef void (*Distruge)(TElem);

typedef TElem (*Copiaza)(TElem);

typedef struct {
    TElem *elems;
    int lg;
    int cap;
    Distruge distruge;
    Copiaza copiaza;
} List;

/**
 * Functie care creeaza o lista
 * @param distruge Functie de distrugere a elementelor
 * @param copiaza Functie de copiere a elementelor
 * @return Lista creata
 */
List *creeazaLista(Distruge distruge, Copiaza copiaza);

/**
 * Functie care distruge o lista
 * @param list Lista de distrus
 */
void distrugeLista(List *list);

/**
 * Functie care copiaza o lista
 * @param list Lista de copiat
 * @return Copia listei
 */
List *copiazaLista(List *list);

/**
 * Functie care afla lungimea unei liste
 * @param list Lista de analizat
 * @return Lungimea listei
 */
int lungime(List *list);

/**
 * Functie care adauga un element la sfarsitul listei
 * @param list Lista de modificat
 * @param elem Elementul de adaugat
 */
void add(List *list, TElem elem);

/**
 * Functie care sterge un element de pe o pozitie data
 * @param list Lista de modificat
 * @param pozitie Pozitia de pe care se sterge elementul
 * @return Elementul sters
 */
TElem pop(List *list);

#endif //LAB_5__FMM_PAUL__LIST_H
