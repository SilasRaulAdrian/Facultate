#ifndef LAB_6_7_CARTE_H
#define LAB_6_7_CARTE_H

#include <string>
#include <iostream>

class Carte {
private:
    mutable std::string titlu;
    mutable std::string autor;
    mutable std::string gen;
    mutable int an_aparitie;
    int id;

public:
    /**
     * Constructorul clasei Carte
     * @param id Id-ul cartii
     * @param titlu Titlul cartii
     * @param autor Autorul cartii
     * @param gen Genul cartii
     * @param an_aparitie Anul aparitiei cartii
     */
    Carte(int id, const std::string &titlu, const std::string &autor, const std::string &gen, int an_aparitie) :
        id(id), titlu(titlu), autor(autor), gen(gen), an_aparitie(an_aparitie) {}

    /**
     * Constructorul de copiere al clasei Carte
     * @param other Obiectul Carte de copiat
     */
    Carte(const Carte &other) :
        id(other.id), titlu(other.titlu), autor(other.autor), gen(other.gen), an_aparitie(other.an_aparitie) {
        std::cout << "Constructor de copiere apelat\n";
    }

    /**
     * Suprascrierea operatorului de atribuire
     * @param other Obiectul Carte de copiat
     * @return Obiectul curent
     */
    Carte &operator=(const Carte &other) {
        if (this != &other) {
            id = other.id;
            titlu = other.titlu;
            autor = other.autor;
            gen = other.gen;
            an_aparitie = other.an_aparitie;
        }
        std::cout << "Operator de atribuire apelat\n";
        return *this;
    }

    /**
     * Suprascrierea operatorului de comparare
     * @param other Obiectul Carte cu care se compara
     * @return True daca cartile sunt egale, false altfel
     */
    bool operator==(const Carte &other) const {
        return this->id == other.id &&
               this->titlu == other.titlu &&
               this->autor == other.autor &&
               this->gen == other.gen &&
               this->an_aparitie == other.an_aparitie;
    }

    /**
     * Constructorul default al clasei Carte
     */
    Carte() = default;

    /**
     * Destructorul clasei Carte
     */
    ~Carte()  = default;

    /**
     * Getter pentru id
     * @return Id-ul cartii
     */
    const int getId() const;

    /**
     * Getter pentru titlu
     * @return Titlul cartii
     */
    const std::string &getTitlu() const;

    /**
     * Getter pentru autor
     * @return Autorul cartii
     */
    const std::string &getAutor() const;

    /**
     * Getter pentru gen
     * @return Genul cartii
     */
    const std::string &getGen() const;

    /**
     * Getter pentru an_aparitie
     * @return Anul aparitiei cartii
     */
    const int getAnAparitie() const;

    /**
     * Setter pentru titlu
     * @param newTitlu Noul titlu al cartii
     */
    void setTitlu(const std::string &newTitlu) const;

    /**
     * Setter pentru autor
     * @param newAutor Noul autor al cartii
     */
    void setAutor(const std::string &newAutor) const;

    /**
     * Setter pentru gen
     * @param newGen Noul gen al cartii
     */
    void setGen(const std::string &newGen) const;

    /**
     * Setter pentru an_aparitie
     * @param newAnAparitie Noul an de aparitie al cartii
     */
    void setAnAparitie(int newAnAparitie) const;
};

#endif //LAB_6_7_CARTE_H
