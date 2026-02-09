package org.example.model.paging;

/**
 * Clasa pentru paginare
 */
public class Pageable {
    private final int pageNumber;
    private final int pageSize;

    /**
     * Constructor pentru clasa Pageable
     * @param pageNumber int, numarul paginii
     * @param pageSize int, dimensiunea paginii
     */
    public Pageable(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * Getter pentru numarul paginii
     * @return int, numarul paginii
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Getter pentru dimensiunea paginii
     * @return int, dimensiunea paginii
     */
    public int getPageSize() {
        return pageSize;
    }
}
