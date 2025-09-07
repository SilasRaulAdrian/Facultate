#include "carte_service.h"
#include "../exceptions/exceptii.h"
#include <algorithm>

void CarteService::adaugaCarte(int id, const std::string &titlu, const std::string &autor, const std::string &gen, int an_aparitie) {
    if (id <= 0 || titlu.empty() || autor.empty() || gen.empty() || an_aparitie <= 0) {
        throw ValidationException("Datele introduse sunt invalide.");
    }
    Carte carte(id, titlu, autor, gen, an_aparitie);
    repo.adaugaCarte(carte);
    undoActions.push_back(std::make_unique<UndoAdaugaCarte>(repo, carte));
}

bool CarteService::stergeCarte(int id) {
    Carte c = repo.cautaCarte(getTitluById(id));
    bool rezultat = repo.stergeCarte(c.getId());
    if (rezultat)
        undoActions.push_back(std::make_unique<UndoStergeCarte>(repo, c));
    return rezultat;
}

bool CarteService::modificaTitlu(int id, const std::string &newTitlu) {
    Carte carteVeche = repo.cautaCarte(getTitluById(id));
    bool rezultat = repo.modificaTitlu(id, newTitlu);
    if (rezultat) {
        undoActions.push_back(std::make_unique<UndoModificaCarte>(repo, carteVeche, Carte(id, newTitlu, carteVeche.getAutor(), carteVeche.getGen(), carteVeche.getAnAparitie())));
    }
    return rezultat;
}

bool CarteService::modificaAutor(int id, const std::string &newAutor) {
    Carte carteVeche = repo.cautaCarte(getTitluById(id));
    bool rezultat = repo.modificaAutor(id, newAutor);
    if (rezultat) {
        undoActions.push_back(std::make_unique<UndoModificaCarte>(repo, carteVeche, Carte(id, carteVeche.getTitlu(), newAutor, carteVeche.getGen(), carteVeche.getAnAparitie())));
    }
    return rezultat;
}

bool CarteService::modificaGen(int id, const std::string &newGen) {
    Carte carteVeche = repo.cautaCarte(getTitluById(id));
    bool rezultat = repo.modificaGen(id, newGen);
    if (rezultat) {
        undoActions.push_back(std::make_unique<UndoModificaCarte>(repo, carteVeche, Carte(id, carteVeche.getTitlu(), carteVeche.getAutor(), newGen, carteVeche.getAnAparitie())));
    }
    return rezultat;
}

bool CarteService::modificaAnAparitie(int id, int newAnAparitie) {
    Carte carteVeche = repo.cautaCarte(getTitluById(id));
    bool rezultat = repo.modificaAnAparitie(id, newAnAparitie);
    if (rezultat) {
        undoActions.push_back(std::make_unique<UndoModificaCarte>(repo, carteVeche, Carte(id, carteVeche.getTitlu(), carteVeche.getAutor(), carteVeche.getGen(), newAnAparitie)));
    }
    return rezultat;
}

Lista<Carte> &CarteService::getCarti() {
    return repo.getCarti();
}

Carte CarteService::cautaCarte(const std::string &titlu) const {
    return repo.cautaCarte(titlu);
}

Lista<Carte> CarteService::filtreazaDupaTitlu(const std::string &titlu) const {
    Lista<Carte> rezultat;
    auto &carti = repo.getCarti();

    std::copy_if(carti.begin(), carti.end(), std::back_inserter(rezultat), [&titlu](const Carte &carte) {
        return carte.getTitlu() == titlu;
    });

    return rezultat;
}

Lista<Carte> CarteService::filtreazaDupaAn(int an) const{
    Lista<Carte> rezultat;
    auto &carti = repo.getCarti();

    std::copy_if(carti.begin(), carti.end(), std::back_inserter(rezultat), [an](const Carte &carte) {
        return carte.getAnAparitie() == an;
    });

    return rezultat;
}

Lista<Carte> CarteService::sorteazaDupaTitlu(int mod) {
    Lista<Carte> listaSortata = repo.getCarti();
    auto* v = listaSortata.getVector();

    std::sort(v, v + listaSortata.getSize(), [mod](const Carte &a, const Carte &b) {
        if (mod == 1) {
            return a.getTitlu() > b.getTitlu();
        } else {
            return a.getTitlu() < b.getTitlu();
        }
    });

    return listaSortata;
}

Lista<Carte> CarteService::sorteazaDupaAutor(int mod) {
    Lista<Carte> listaSortata = repo.getCarti();
    auto* v = listaSortata.getVector();

    std::sort(v, v + listaSortata.getSize(), [mod](const Carte &a, const Carte &b) {
        if (mod == 1) {
            return a.getAutor() > b.getAutor();
        } else {
            return a.getAutor() < b.getAutor();
        }
    });

    return listaSortata;
}

Lista<Carte> CarteService::sorteazaDupaAnulAparitiei(int mod) {
    Lista<Carte> listaSortata = repo.getCarti();
    auto* v = listaSortata.getVector();

    std::sort(v, v + listaSortata.getSize(), [mod](const Carte &a, const Carte &b) {
        if (mod == 1) {
            return a.getAnAparitie() > b.getAnAparitie();
        } else {
            return a.getAnAparitie() < b.getAnAparitie();
        }
    });

    return listaSortata;
}

Lista<Carte> CarteService::sorteazaDupaGen(int mod) {
    Lista<Carte> listaSortata = repo.getCarti();
    auto* v = listaSortata.getVector();

    std::sort(v, v + listaSortata.getSize(), [mod](const Carte &a, const Carte &b) {
        if (mod == 1) {
            return a.getGen() > b.getGen();
        } else {
            return a.getGen() < b.getGen();
        }
    });

    return listaSortata;
}

void CarteService::golesteCos() {
    const auto& carti = cos.getAllCos();
    for (const auto& carte : carti) {
        repo.adaugaCarte(carte);
    }
    cos.golesteCos();
    notify();
}

void CarteService::adaugaCarteDupaTitlu(const std::string &titlu) {
    const auto& carte = cautaCarte(titlu);
    cos.adaugaCarte(carte);
    repo.stergeCarte(carte.getId());
    notify();
}

void CarteService::genereazaCos(int numarCarti, Lista<Carte> &carti) {
    cos.genereazaCos(numarCarti, carti);
    for (const auto& carte : cos.getAllCos()) {
        repo.stergeCarte(carte.getId());
    }
    notify();
}

void CarteService::exportCsv(const std::string &numeFisier) const {
    cos.exportCsv(numeFisier);
}

void CarteService::exportHtml(const std::string &numeFisier) const {
    cos.exportHtml(numeFisier);
}

int CarteService::getSizeCos() const {
    return cos.getSizeCos();
}

const std::vector<Carte>& CarteService::getAllCos() const {
    return cos.getAllCos();
}

std::map<std::string, std::vector<Carte>> CarteService::getCartiPeGen() const {
    std::map<std::string, std::vector<Carte>> cartiPeGen;
    const auto &carti = repo.getCarti();

    for (const auto &carte : carti) {
        cartiPeGen[carte.getGen()].push_back(carte);
    }

    return cartiPeGen;
}

void CarteService::undo() {
    if (undoActions.empty()) {
        throw std::runtime_error("Nu exista actiuni de undo disponibile.");
    }

    undoActions.back()->doUndo();
    undoActions.pop_back();
}

std::string CarteService::getTitluById(int id) const {
    for (const auto &carte : repo.getCarti()) {
        if (carte.getId() == id) {
            return carte.getTitlu();
        }
    }

    throw std::runtime_error("Cartea cu id-ul " + std::to_string(id) + " nu a fost gasita.");
}

void CarteService::addObserver(Observer* observer) {
    cos.addObserver(observer);
}

void CarteService::removeObserver(Observer* observer) {
    cos.removeObserver(observer);
}

void CarteService::notify() {
    cos.notify();
    for (auto& observer : observers) {
        observer->update();
    }
}