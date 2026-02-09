package org.example.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clasa generica pentru entitati cu id
 * @param <ID> tipul id-ului
 */
public class Entity<ID> implements Serializable {
    protected ID id;

    /**
     * Getter pentru id
     * @return ID, id-ul entitatii
     */
    public ID getId() {
        return id;
    }

    /**
     * Setter pentru id
     * @param id ID, id-ul entitatii
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * Metoda equals pentru compararea a doua entitati
     * @param o Object, obiectul cu care se compara
     * @return boolean, true daca sunt egale, false altfel
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        return Objects.equals(id, entity.id);
    }

    /**
     * Metoda hashCode pentru generarea hash-ului entitatii
     * @return int, hash-ul entitatii
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
