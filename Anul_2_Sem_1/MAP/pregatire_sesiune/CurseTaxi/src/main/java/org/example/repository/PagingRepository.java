package org.example.repository;

import java.util.List;

public interface PagingRepository<ID, E> extends Repository<ID, E> {
    List<E> findPage(int limit, int offset);
}
