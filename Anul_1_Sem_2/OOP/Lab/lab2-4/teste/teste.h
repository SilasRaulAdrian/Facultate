#ifndef TESTE_H
#define TESTE_H

/**
 * Functie care testeaza functionalitatile din domeniu.
 */
void teste_domain();

/**
 * Functie care testeaza functionalitatile din repository.
 */
void teste_repo();

/**
 * Functie care testeaza functionalitatile din service.
 */
void teste_service();

/**
 * Functie care testeaza functionalitatile de undo din service.
 */
void teste_undo_service();

/**
 * Functie care ruleaza toate testele.
 */
void run_all_tests();

#endif // TESTE_H
