package org.example.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<ID, E> {
    Optional<E> findOne(ID id);
    List<E> findAll();
    E save(E entity);
    E update(E entity);
    Optional<E> delete(ID id);
}
