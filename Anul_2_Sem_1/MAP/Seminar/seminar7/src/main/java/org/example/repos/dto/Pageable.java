package org.example.repos.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class Pageable {
    private final int pageNumber;
    private final int pageSize;
}
