package org.example.repository;

import org.example.model.paging.Page;
import org.example.model.paging.Pageable;

public interface PagingRepository<T> {
    Page<T> findAllOnPage(Pageable pageable);
}
