#ifndef LAB_6_7_LISTA_H
#define LAB_6_7_LISTA_H

#include <stdexcept>

template <typename T>
class Lista {
private:
    T *elemente;
    int lungime;
    int capacitate;

public:
    using value_type = T;

    /**
     * Constructorul clasei Lista
     * @param cap Capacitatea initiala a listei
     */
    Lista(int cap = 10) : lungime(0), capacitate(cap) {
        elemente = new T[capacitate];
    }

    /**
     * Destructorul clasei Lista
     */
    ~Lista() {
        delete[] elemente;
    }

    /**
     * Constructor de copiere
     * @param other Lista de copiat
     */
    Lista(const Lista &other) : lungime(other.lungime), capacitate(other.capacitate) {
        elemente = new T[capacitate];
        for (int i = 0; i < lungime; ++i) {
            elemente[i] = other.elemente[i];
        }
    }

    /**
     * Operator de atribuire
     * @param other Lista de copiat
     * @return Lista curenta
     */
    Lista& operator=(const Lista &other) {
        if (this != &other) {
            delete[] elemente;
            lungime = other.lungime;
            capacitate = other.capacitate;
            elemente = new T[capacitate];
            for (int i = 0; i < lungime; ++i) {
                elemente[i] = other.elemente[i];
            }
        }
        return *this;
    }

    /**
     * Redimensioneaza lista pentru a avea o capacitate mai mare
     */
    void redimensioneaza() {
        capacitate *= 2;
        T *nou = new T[capacitate];
        for (int i = 0; i < lungime; ++i) {
            nou[i] = elemente[i];
        }
        delete[] elemente;
        elemente = nou;
    }

    /**
     * Adauga un element in lista
     * @param element Elementul de adaugat
     */
    void push_back(const T &element) {
        if (exista(element)) {
            throw std::runtime_error("Elementul exista deja in lista.");
        }

        if (lungime == capacitate) {
            redimensioneaza();
        }

        elemente[lungime++] = element;
    }

    /**
     * Verifica daca un element exista in lista
     * @param element Elementul de cautat
     * @return True daca elementul exista, false altfel
     */
    bool exista(const T &element) const {
        for (int i = 0; i < lungime; ++i) {
            if (elemente[i] == element) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sterge un element din lista
     * @param element Elementul de sters
     * @return True daca elementul a fost sters, false altfel
     */
    bool sterge(const T &element) {
        for (int i = 0; i < lungime; ++i) {
            if (elemente[i] == element) {
                for (int j = i; j < lungime - 1; ++j) {
                    elemente[j] = elemente[j + 1];
                }
                --lungime;
                return true;
            }
        }
        throw std::runtime_error("Elementul nu a fost gasit in lista.");
    }

    /**
     * Returneaza dimensiunea listei
     * @return Dimensiunea listei
     */
    int getSize() const {
        return lungime;
    }

    /**
     * Suprascrierea operatorului de indexare
     * @param index Indexul elementului
     * @return Elementul de la indexul dat
     */
    const T& operator[](int index) const {
        if (index < 0 || index >= lungime) {
            throw std::out_of_range("Index invalid.");
        }
        return elemente[index];
    }

    /**
     * Inceputul listei
     * @return Pointer la inceputul listei
     */
    const T* begin() const {
        return elemente;
    }

    /**
     * Sfarsitul listei
     * @return Pointer la sfarsitul listei
     */
    const value_type* end() const {
        return elemente + lungime;
    }

    /**
     * Inceputul listei
     * @return Pointer la inceputul listei
     */
    T* begin() {
        return elemente;
    }

    /**
     * Sfarsitul listei
     * @return Pointer la sfarsitul listei
     */
    T* end() {
        return elemente + lungime;
    }

    /**
     * Getter pentru vectorul de elemente
     * @return Vectorul de elemente
     */
    T* getVector() {
        return elemente;
    }
};

#endif //LAB_6_7_LISTA_H
