#ifndef VALIDATOR_H
#define VALIDATOR_H
#include "../domain/oferta.h"

/**
 * Functie care valideaza un id
 * @param id Id-ul care va fi validat
 * @return 1 daca id-ul este valid, 0 altfel
 */
int validare_id(int id);

/**
 * Functie care valideaza un tip
 * @param tip Tipul care va fi validat
 * @return 1 daca tipul este valid, 0 altfel
 */
int validare_tip(char *tip);

/**
 * Functie care valideaza o suprafata
 * @param suprafata Suprafata care va fi validata
 * @return 1 daca suprafata este valida, 0 altfel
 */
int validare_suprafata(int suprafata);

/**
 * Functie care valideaza o adresa
 * @param adresa Adresa care va fi validata
 * @return 1 daca adresa este valida, 0 altfel
 */
int validare_adresa(char *adresa);

/**
 * Functie care valideaza un pret
 * @param pret Pretul care va fi validat
 * @return 1 daca pretul este valid, 0 altfel
 */
int validare_pret(int pret);

/**
 * Functie care valideaza o oferta
 * @param oferta Oferta care va fi validata
 * @return 1 daca oferta este valida, 0 altfel
 */
int validare_oferta(Oferta *oferta);

#endif // VALIDATOR_H
