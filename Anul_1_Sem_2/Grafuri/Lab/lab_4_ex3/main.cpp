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

void construiesteCoduri(Nod* radacina, string cod, unordered_map<char, string>& coduriHuffman) {
    if (!radacina)
        return;
    if (!radacina->stanga && !radacina->dreapta)
        coduriHuffman[radacina->caracter] = cod;
    construiesteCoduri(radacina->stanga, cod + "0", coduriHuffman);
    construiesteCoduri(radacina->dreapta, cod + "1", coduriHuffman);
}

void elibereazaArbore(Nod* radacina) {
    if (!radacina) return;
    elibereazaArbore(radacina->stanga);
    elibereazaArbore(radacina->dreapta);
    delete radacina;
}

int main(int argc, char* argv[]) {
    if (argc < 3) {
        cerr << "Utilizare: p3 <fisier_intrare> <fisier_iesire>\n";
        return 1;
    }

    ifstream intrare(argv[1]);
    ofstream iesire(argv[2]);

    string text((istreambuf_iterator<char>(intrare)), istreambuf_iterator<char>());

    unordered_map<char, int> frecvente;
    for (char caracter : text)
        frecvente[caracter]++;

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
    unordered_map<char, string> coduriHuffman;
    construiesteCoduri(radacina, "", coduriHuffman);

    vector<char> alfabet;
    for (auto& pereche : frecvente)
        alfabet.push_back(pereche.first);
    sort(alfabet.begin(), alfabet.end());

    iesire << alfabet.size() << "\n";
    for (char caracter : alfabet)
        iesire << caracter << " " << frecvente[caracter] << "\n";

    string textCodificat;
    for (char caracter : text)
        textCodificat += coduriHuffman[caracter];

    iesire << textCodificat << "\n";

    elibereazaArbore(radacina);
    return 0;
}
