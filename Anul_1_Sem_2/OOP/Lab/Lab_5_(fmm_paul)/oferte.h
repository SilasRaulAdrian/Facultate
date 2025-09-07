#ifndef LAB_5__FMM_PAUL__OFERTE_H
#define LAB_5__FMM_PAUL__OFERTE_H

typedef struct oferta {
    int id;
    char *tip;
    int suprafata;
    char *adresa;
    int pret;
}Oferta;

/**
 * Functie care creeaza o oferta
 * @param id Id-ul ofertei
 * @param tip Tipul ofertei
 * @param suprafata Suprafata ofertei
 * @param adresa Adresa ofertei
 * @param pret Pretul ofertei
 * @return Oferta creata
 */
Oferta *creeazaOferta(int id, char *tip, int suprafata, char *adresa, int pret);

/**
 * Functie care distruge o oferta
 * @param oferta Oferta care va fi distrusa
 */
void distrugeOferta(Oferta *oferta);

/**
 * Functie care creeaza o copie a unei oferte
 * @param oferta Oferta care va fi copiata
 * @return Copia ofertei
 */
Oferta* copiazaOferta(Oferta *oferta);

#endif //LAB_5__FMM_PAUL__OFERTE_H
