package org.example.repos.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class Page<E> {
    private final Iterable<E> entities;
    private final int entitiesCount;

}
