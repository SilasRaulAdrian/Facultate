#include "cos_inchirieri.h"
#include <fstream>
#include <algorithm>
#include <random>

void CosInchirieri::golesteCos() {
    cos.clear();
}

void CosInchirieri::adaugaCarte(const Carte& carte) {
    cos.push_back(carte);
}

void CosInchirieri::genereazaCos(int numarCarti, Lista<Carte> &carti) {
    golesteCos();

    if (numarCarti > carti.getSize()) {
        throw std::runtime_error("Numarul de carti dorit este mai mare decat numarul de carti disponibile.");
    }
    if (numarCarti <= 0) {
        throw std::runtime_error("Numarul de carti dorit trebuie sa fie mai mare decat 0.");
    }

    Lista<Carte> copieCarti = carti;
    std::shuffle(copieCarti.begin(), copieCarti.end(), std::default_random_engine(std::random_device{}()));

    for (int i = 0; i < numarCarti; ++i) {
        cos.push_back(copieCarti[i]);
    }
}

void CosInchirieri::exportCsv(const std::string &numeFisier) const {
    std::ofstream fisier(numeFisier);

    for (const auto &carte : this->getAllCos()) {
        fisier << carte.getId() << "," << carte.getTitlu() << "," << carte.getAutor() << "," << carte.getGen() << "," << carte.getAnAparitie() << "\n";
    }

    fisier.close();
}

const std::vector<Carte> &CosInchirieri::getAllCos() const {
    return cos;
}

int CosInchirieri::getSizeCos() const {
    return cos.size();
}