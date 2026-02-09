package org.example.repository;

import org.example.model.Entity;

import java.util.Optional;

/**
 * Interfata generica pentru un repository
 * @param <ID> Tipul ID-ului entitatii
 * @param <E> Tipul entitatii care extinde User
 */
public interface Repository <ID, E extends Entity<ID>> {
    /**
     * Metoda pentru gasirea unei entitati dupa ID
     * @param id ID-ul entitatii
     * @return Optional<E>, entitatea gasita sau empty daca nu exista
     */
    Optional<E> findById(ID id);

    /**
     * Metoda pentru obtinerea tuturor entitatilor
     * @return Iterable<E>, toate entitatile din repository
     */
    Iterable<E> getAll();

    /**
     * Metoda pentru salvarea unei entitati
     * @param e Entitatea care va fi salvata
     * @return Optional<E>, entitatea salvata sau empty daca nu a fost salvata
     */
    Optional<E> save(E e);

    /**
     * Metoda pentru actualizarea unei entitati
     * @param e Entitatea care va fi actualizata
     * @return Optional<E>, entitatea actualizata sau empty daca nu a fost actualizata
     */
    Optional<E> update(E e);

    /**
     * Metoda pentru stergerea unei entitati dupa ID
     * @param id ID-ul entitatii care va fi stearsa
     * @return Optional<E>, entitatea stearsa sau empty daca nu a fost stearsa
     */
    Optional<E> delete(ID id);
}
