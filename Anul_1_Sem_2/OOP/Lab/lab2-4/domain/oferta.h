#ifndef OFERTA_H
#define OFERTA_H

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
 * @return Ofera creata
 */
Oferta *creeaza_oferta(int id, char *tip, int suprafata, char *adresa, int pret);

/**
 * Functie care distruge o oferta
 * @param oferta Oferta care va fi distrusa
 */
void distruge_oferta(Oferta *oferta);

/**
 * Functie care creeaza o copie a unei oferte
 * @param oferta Oferta care va fi copiata
 * @return Copia ofertei
 */
Oferta* copie_oferta(Oferta *oferta);

#endif // OFERTA_H
