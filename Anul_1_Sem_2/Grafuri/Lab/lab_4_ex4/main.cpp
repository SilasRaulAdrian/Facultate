#include <iostream>
#include <fstream>
#include <unordered_map>
#include <queue>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

struct Nod {
    char caracter;
    int frecventa;
    Nod *stanga, *dreapta;

    Nod(char caracter, int frecventa, Nod* stanga = nullptr, Nod* dreapta = nullptr)
            : caracter(caracter), frecventa(frecventa), stanga(stanga), dreapta(dreapta) {}
};

struct Comparare {
    bool operator()(Nod* a, Nod* b) {
        if (a->frecventa == b->frecventa)
            return a->caracter > b->caracter;
        return a->frecventa > b->frecventa;
    }
};

void elibereazaArbore(Nod* radacina) {
    if (!radacina) return;
    elibereazaArbore(radacina->stanga);
    elibereazaArbore(radacina->dreapta);
    delete radacina;
}

string decodifica(const string& codificat, Nod* radacina) {
    string rezultat;
    Nod* curent = radacina;
    for (char bit : codificat) {
        if (bit == '0')
            curent = curent->stanga;
        else
            curent = curent->dreapta;

        if (!curent->stanga && !curent->dreapta) {
            rezultat += curent->caracter;
            curent = radacina;
        }
    }
    return rezultat;
}

int main(int argc, char* argv[]) {
    if (argc < 3) {
        cerr << "Utilizare: p4 <fisier_intrare> <fisier_iesire>\n";
        return 1;
    }

    ifstream intrare(argv[1]);
    ofstream iesire(argv[2]);

    int nrCaractere;
    intrare >> nrCaractere;
    unordered_map<char, int> frecvente;
    vector<pair<char, int>> listaFrecvente;
    intrare.ignore();

    for (int i = 0; i < nrCaractere; ++i) {
        string linie;
        getline(intrare, linie);
        char caracter = linie[0];
        int frecventa = stoi(linie.substr(2));
        frecvente[caracter] = frecventa;
        listaFrecvente.emplace_back(caracter, frecventa);
    }

    string codBin;
    getline(intrare, codBin);

    priority_queue<Nod*, vector<Nod*>, Comparare> coada;
    for (auto& pereche : frecvente)
        coada.push(new Nod(pereche.first, pereche.second));

    while (coada.size() > 1) {
        Nod* stanga = coada.top(); coada.pop();
        Nod* dreapta = coada.top(); coada.pop();
        char caracterMinim = min(stanga->caracter, dreapta->caracter);
        coada.push(new Nod(caracterMinim, stanga->frecventa + dreapta->frecventa, stanga, dreapta));
    }

    Nod* radacina = coada.top();
    string rezultat = decodifica(codBin, radacina);
    iesire << rezultat << endl;
    elibereazaArbore(radacina);
    return 0;
}
