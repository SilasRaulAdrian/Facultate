#include "carte_repo.h"
#include "../exceptions/exceptii.h"
#include <algorithm>

void CarteRepo::adaugaCarte(const Carte &carte) {
    auto it = std::find_if(carti.begin(), carti.end(), [&carte](const Carte &c) {
        return c.getId() == carte.getId();
    });

    if (it != carti.end()) {
        throw RepoException("Cartea cu id-ul " + std::to_string(carte.getId()) + " exista deja.");
    }

    carti.push_back(carte);
}

bool CarteRepo::stergeCarte(int id) {
    auto it = std::find_if(carti.begin(), carti.end(), [=](const Carte &c) {
        return c.getId() == id;
    });

    if (it != carti.end()) {
        carti.sterge(*it);
        return true;
    }

    throw RepoException("Cartea cu id-ul " + std::to_string(id) + " nu exista.");
}

bool CarteRepo::modificaTitlu(int id, const std::string &newTitlu) {
    for (auto& carte : carti) {
        if (carte.getId() == id) {
            carte.setTitlu(newTitlu);
            return true;
        }
    }

    throw RepoException("Cartea cu id-ul " + std::to_string(id) + " nu exista.");
}

bool CarteRepo::modificaAutor(int id, const std::string &newAutor) {
    for (auto& carte : carti) {
        if (carte.getId() == id) {
            carte.setAutor(newAutor);
            return true;
        }
    }

    throw RepoException("Cartea cu id-ul " + std::to_string(id) + " nu exista.");
}

bool CarteRepo::modificaGen(int id, const std::string &newGen) {
    for (auto& carte : carti) {
        if (carte.getId() == id) {
            carte.setGen(newGen);
            return true;
        }
    }

    throw RepoException("Cartea cu id-ul " + std::to_string(id) + " nu exista.");
}

bool CarteRepo::modificaAnAparitie(int id, int newAnAparitie) {
    for (auto& carte : carti) {
        if (carte.getId() == id) {
            carte.setAnAparitie(newAnAparitie);
            return true;
        }
    }

    throw RepoException("Cartea cu id-ul " + std::to_string(id) + " nu exista.");
}

Lista<Carte> &CarteRepo::getCarti() {
    return carti;
}

Carte CarteRepo::cautaCarte(const std::string &titlu) const {
    auto it = std::find_if(carti.begin(), carti.end(), [&](const Carte &c) {
        return c.getTitlu() == titlu;
    });

    if (it != carti.end()) {
        return *it;
    }

    throw RepoException("Cartea cu titlul " + titlu + " nu exista.");
}