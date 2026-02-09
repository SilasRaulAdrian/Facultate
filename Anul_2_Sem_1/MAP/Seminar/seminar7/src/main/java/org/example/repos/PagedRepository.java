package org.example.repos;

import org.example.model.Entity;
import org.example.repos.dto.Page;
import org.example.repos.dto.Pageable;

public interface PagedRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}
