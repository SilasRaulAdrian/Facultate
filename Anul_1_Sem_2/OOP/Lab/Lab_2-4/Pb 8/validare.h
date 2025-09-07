#ifndef VALIDARE_H
#define VALIDARE_H

#include "produs.h"

/**
 * Functie care valideaza un id
 * @param id Id-ul care va fi validat
 * @return 1 daca id-ul este valid, 0 altfel
 */
int validareId(int id);

/**
 * Functie care valideaza un tip
 * @param tip Tipul care va fi validat
 * @return 1 daca tipul este valid, 0 altfel
 */
int validareTip(char *tip);

/**
 * Functie care valideaza un producator
 * @param producator Producatorul care va fi validat
 * @return 1 daca producatorul este valid, 0 altfel
 */
int validareProducator(char *producator);

/**
 * Functie care valideaza un model
 * @param model Modelul care va fi validat
 * @return 1 daca modelul este valid, 0 altfel
 */
int validareModel(char *model);

/**
 * Functie care valideaza un pret
 * @param pret Pretul care va fi validat
 * @return 1 daca pretul este valid, 0 altfel
 */
int validarePret(float pret);

/**
 * Functie care valideaza o cantitate
 * @param cantitate Cantitatea care va fi validata
 * @return 1 daca cantitatea este valida, 0 altfel
 */
int validareCantitate(int cantitate);

/**
 * Functie care valideaza un produs
 * @param produs Produsul de validat
 * @return 1 daca produsul este valid, 0 altfel
 */
int validareProdus(Produs *produs);

#endif // VALIDARE_H