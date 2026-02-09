package org.example.model.paging;

import java.util.List;

/**
 * Clasa generica Page care reprezinta o pagina de elemente
 * @param <T> tipul elementelor din pagina
 */
public class Page<T> {
    private final List<T> elementsOnPage;
    private final int elementCount;

    /**
     * Constructor pentru clasa Page
     * @param elementsOnPage lista de elemente din pagina
     * @param elementCount numarul total de elemente
     */
    public Page(List<T> elementsOnPage, int elementCount) {
        this.elementsOnPage = elementsOnPage;
        this.elementCount = elementCount;
    }

    /**
     * Getter pentru lista de elemente din pagina
     * @return List<T>, lista de elemente din pagina
     */
    public List<T> getElementsOnPage() {
        return elementsOnPage;
    }

    /**
     * Getter pentru numarul total de elemente
     * @return int, numarul total de elemente
     */
    public int getElementCount() {
        return elementCount;
    }
}
