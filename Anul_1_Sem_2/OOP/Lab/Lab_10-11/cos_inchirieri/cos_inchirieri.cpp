#include "cos_inchirieri.h"
#include <fstream>
#include <algorithm>
#include <random>

void CosInchirieri::golesteCos() {
    cos.clear();
    notify();
}

void CosInchirieri::adaugaCarte(const Carte& carte) {
    cos.push_back(carte);
    notify();
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

    notify();
}

void CosInchirieri::exportCsv(const std::string &numeFisier) const {
    std::ofstream fisier(numeFisier);

    for (const auto &carte : this->getAllCos()) {
        fisier << carte.getId() << "," << carte.getTitlu() << "," << carte.getAutor() << "," << carte.getGen() << "," << carte.getAnAparitie() << "\n";
    }

    fisier.close();
}

void CosInchirieri::exportHtml(const std::string &numeFisier) const {
    std::ofstream fisier(numeFisier);

    fisier << "<!DOCTYPE html>\n<html>\n<head>\n<title>Cos Inchirieri</title>\n";
    fisier << "<style>table, th, td { border: 1px solid black; border-collapse: collapse; padding: 5px; }</style>\n";
    fisier << "</head>\n<body>\n";
    fisier << "<h2>Lista carti in cos</h2>\n";
    fisier << "<table>\n<tr><th>ID</th><th>Titlu</th><th>Autor</th><th>Gen</th><th>An Aparitie</th></tr>\n";

    for (const auto &carte: cos) {
        fisier << "<tr>";
        fisier << "<td>" << carte.getId() << "</td>";
        fisier << "<td>" << carte.getTitlu() << "</td>";
        fisier << "<td>" << carte.getAutor() << "</td>";
        fisier << "<td>" << carte.getGen() << "</td>";
        fisier << "<td>" << carte.getAnAparitie() << "</td>";
        fisier << "</tr>\n";
    }

    fisier << "</table>\n</body>\n</html>";
    fisier.close();
}

const std::vector<Carte> &CosInchirieri::getAllCos() const {
    return cos;
}

int CosInchirieri::getSizeCos() const {
    return cos.size();
}

void CosInchirieri::addObserver(Observer* observer) {
    observers.push_back(observer);
}

void CosInchirieri::removeObserver(Observer* observer) {
    observers.erase(std::remove(observers.begin(), observers.end(), observer), observers.end());
}

void CosInchirieri::notify() {
    for (Observer* observer : observers) {
        observer->update();
    }
}