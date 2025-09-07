#include "console.h"
#include <iostream>
#include <map>

void UI::afiseazaMeniu() const {
    std::cout << "Meniu:\n";
    std::cout << "1. Adauga carte\n";
    std::cout << "2. Sterge carte\n";
    std::cout << "3. Modifica titlu\n";
    std::cout << "4. Modifica autor\n";
    std::cout << "5. Modifica gen\n";
    std::cout << "6. Modifica an aparitie\n";
    std::cout << "7. Afiseaza carti\n";
    std::cout << "8. Cauta carte\n";
    std::cout << "9. Filtreaza carti dupa titlu\n";
    std::cout << "10. Filtreaza carti dupa an aparitie\n";
    std::cout << "11. Sorteaza carti dupa titlu in functie de mod (0 - crescator, 1 - descrescator)\n";
    std::cout << "12. Sorteaza carti dupa autor in functie de mod (0 - crescator, 1 - descrescator)\n";
    std::cout << "13. Sorteaza carti dupa gen in functie de mod (0 - crescator, 1 - descrescator)\n";
    std::cout << "14. Sorteaza carti dupa anul aparitiei in functie de mod (0 - crescator, 1 - descrescator)\n";
    std::cout << "15. Goleste cosul\n";
    std::cout << "16. Adauga carte in cos dupa titlu\n";
    std::cout << "17. Genereaza cos aleatoriu\n";
    std::cout << "18. Export cos in fisier CSV\n";
    std::cout << "19. Afiseaza cartile in functie de gen\n";
    std::cout << "20. Undo\n";
    std::cout << "0. Iesire\n";
}

void UI::adaugaCarte() {
    std::string titlu, autor, gen;
    int id, an_aparitie;

    std::cout << "Introduceti id-ul: ";
    std::cin >> id;
    std::cout << "Introduceti titlul: ";
    std::cin >> titlu;
    std::cout << "Introduceti autorul: ";
    std::cin >> autor;
    std::cout << "Introduceti genul: ";
    std::cin >> gen;
    std::cout << "Introduceti anul aparitiei: ";
    std::cin >> an_aparitie;

    service.adaugaCarte(id, titlu, autor, gen, an_aparitie);
    std::cout << "Carte adaugata cu succes!\n";
}

void UI::stergeCarte() {
    int id;

    std::cout << "Introduceti id-ul cartii: ";
    std::cin >> id;

    if (service.stergeCarte(id)) {
        std::cout << "Carte stearsa cu succes!\n";
    } else {
        std::cout << "Carte nu a fost gasita!\n";
    }
}

void UI::modificaTitlu() {
    int id;
    std::string newTitlu;

    std::cout << "Introduceti id-ul cartii: ";
    std::cin >> id;

    std::cout << "Introduceti noul titlu: ";
    std::cin >> newTitlu;

    if (service.modificaTitlu(id, newTitlu)) {
        std::cout << "Titlu modificat cu succes!\n";
    } else {
        std::cout << "Carte nu a fost gasita!\n";
    }
}

void UI::modificaAutor() {
    int id;
    std::string newAutor;

    std::cout << "Introduceti id-ul cartii: ";
    std::cin >> id;

    std::cout << "Introduceti noul autor: ";
    std::cin >> newAutor;

    if (service.modificaAutor(id, newAutor)) {
        std::cout << "Autor modificat cu succes!\n";
    } else {
        std::cout << "Carte nu a fost gasita!\n";
    }
}

void UI::modificaGen() {
    int id;
    std::string newGen;

    std::cout << "Introduceti id-ul cartii: ";
    std::cin >> id;

    std::cout << "Introduceti noul gen: ";
    std::cin >> newGen;

    if (service.modificaGen(id, newGen)) {
        std::cout << "Gen modificat cu succes!\n";
    } else {
        std::cout << "Carte nu a fost gasita!\n";
    }
}

void UI::modificaAnAparitie() {
    int id, newAnAparitie;

    std::cout << "Introduceti id-ul cartii: ";
    std::cin >> id;

    std::cout << "Introduceti noul an de aparitie: ";
    std::cin >> newAnAparitie;

    if (service.modificaAnAparitie(id, newAnAparitie)) {
        std::cout << "An de aparitie modificat cu succes!\n";
    } else {
        std::cout << "Carte nu a fost gasita!\n";
    }
}

void UI::afiseazaCarti() const {
    const Lista<Carte> &carti = service.getCarti();

    if (carti.getSize() == 0) {
        std::cout << "Nu exista carti!\n";
        return;
    }

    for (int i = 0; i < carti.getSize(); ++i) {
        std::cout << carti[i].getId() << ". " << carti[i].getTitlu() << " - " << carti[i].getAutor() << " - " << carti[i].getGen() << " - " << carti[i].getAnAparitie() << "\n";
    }
}

void UI::cautaCarte() const {
    std::string titlu;

    std::cout << "Introduceti titlul cartii: ";
    std::cin >> titlu;

    Carte carte = service.cautaCarte(titlu);

    if (titlu != carte.getTitlu()) {
        std::cout << "Carte nu a fost gasita!\n";
    } else {
        std::cout << carte.getId() << ". " << carte.getTitlu() << " - " << carte.getAutor() << " - " << carte.getGen() << " - " << carte.getAnAparitie() << "\n";
    }
}

void UI::filtreazaDupaTitlu() const {
    Lista<Carte> cartiFiltrate;
    std::string titlu;

    std::cout << "Introduceti titlul dupa care doriti sa filtrati: ";
    std::cin >> titlu;

    cartiFiltrate = service.filtreazaDupaTitlu(titlu);

    if (cartiFiltrate.getSize() == 0) {
        std::cout << "Nu exista carti cu titlul specificat!\n";
        return;
    }

    for (int i = 0; i < cartiFiltrate.getSize(); ++i) {
        std::cout << cartiFiltrate[i].getId() << ". " << cartiFiltrate[i].getTitlu() << " - " << cartiFiltrate[i].getAutor() << " - " << cartiFiltrate[i].getGen() << " - " << cartiFiltrate[i].getAnAparitie() << "\n";
    }
}

void UI::filtreazaDupaAnulAparitiei() const {
    Lista<Carte> cartiFiltrate;
    int an;

    std::cout << "Introduceti anul dupa care doriti sa filtrati: ";
    std::cin >> an;

    cartiFiltrate = service.filtreazaDupaAn(an);

    if (cartiFiltrate.getSize() == 0) {
        std::cout << "Nu exista carti cu anul specificat!\n";
        return;
    }

    for (int i = 0; i < cartiFiltrate.getSize(); ++i) {
        std::cout << cartiFiltrate[i].getId() << ". " << cartiFiltrate[i].getTitlu() << " - " << cartiFiltrate[i].getAutor() << " - " << cartiFiltrate[i].getGen() << " - " << cartiFiltrate[i].getAnAparitie() << "\n";
    }
}

void UI::sorteazaDupaTitlu() const {
    int mod;

    std::cout << "Introduceti modul de sortare (0 - crescator, 1 - descrescator): ";
    std::cin >> mod;

    Lista<Carte> cartiSortate = service.sorteazaDupaTitlu(mod);

    for (int i = 0; i < cartiSortate.getSize(); ++i) {
        std::cout << cartiSortate[i].getId() << ". " << cartiSortate[i].getTitlu() << " - " << cartiSortate[i].getAutor() << " - " << cartiSortate[i].getGen() << " - " << cartiSortate[i].getAnAparitie() << "\n";
    }
}

void UI::sorteazaDupaAutor() const {
    int mod;

    std::cout << "Introduceti modul de sortare (0 - crescator, 1 - descrescator): ";
    std::cin >> mod;

    Lista<Carte> cartiSortate = service.sorteazaDupaAutor(mod);

    for (int i = 0; i < cartiSortate.getSize(); ++i) {
        std::cout << cartiSortate[i].getId() << ". " << cartiSortate[i].getTitlu() << " - " << cartiSortate[i].getAutor() << " - " << cartiSortate[i].getGen() << " - " << cartiSortate[i].getAnAparitie() << "\n";
    }
}

void UI::sorteazaDupaAnulAparitiei() const {
    int mod;

    std::cout << "Introduceti modul de sortare (0 - crescator, 1 - descrescator): ";
    std::cin >> mod;

    Lista<Carte> cartiSortate = service.sorteazaDupaAnulAparitiei(mod);

    for (int i = 0; i < cartiSortate.getSize(); ++i) {
        std::cout << cartiSortate[i].getId() << ". " << cartiSortate[i].getTitlu() << " - " << cartiSortate[i].getAutor() << " - " << cartiSortate[i].getGen() << " - " << cartiSortate[i].getAnAparitie() << "\n";
    }
}

void UI::sorteazaDupaGen() const {
    int mod;

    std::cout << "Introduceti modul de sortare (0 - crescator, 1 - descrescator): ";
    std::cin >> mod;

    Lista<Carte> cartiSortate = service.sorteazaDupaGen(mod);

    for (int i = 0; i < cartiSortate.getSize(); ++i) {
        std::cout << cartiSortate[i].getId() << ". " << cartiSortate[i].getTitlu() << " - " << cartiSortate[i].getAutor() << " - " << cartiSortate[i].getGen() << " - " << cartiSortate[i].getAnAparitie() << "\n";
    }
}

void UI::golesteCos() const {
    service.golesteCos();
    std::cout << "Cosul a fost golit cu succes!\n";
}

void UI::adaugaCarteInCos() const {
    std::string titlu;

    std::cout << "Introduceti titlul cartii: ";
    std::cin >> titlu;

    service.adaugaCarteDupaTitlu(titlu);
    std::cout << "Carte adaugata in cos!\n";
}

void UI::genereazaCosAleator() const {
    int numarCarti;

    std::cout << "Introduceti numarul de carti dorit: ";
    std::cin >> numarCarti;

    service.genereazaCos(numarCarti, service.getCarti());
    std::cout << "Cos generat cu succes!\n";
}

void UI::exportaCosInCsv() const {
    std::string numeFisier;

    std::cout << "Introduceti numele fisierului CSV: ";
    std::cin >> numeFisier;

    service.exportCsv(numeFisier);
    std::cout << "Cos exportat in fisierul " << numeFisier << "!\n";
}

void UI::afiseazaCartiDupaGen() const {
    std::map<std::string, std::vector<Carte>> cartiDupaGen = service.getCartiPeGen();

    if (cartiDupaGen.empty()) {
        std::cout << "Nu exista carti!\n";
        return;
    }

    for (const auto &pair : cartiDupaGen) {
        std::cout << "Gen: " << pair.first << "\n";
        for (const auto &carte : pair.second) {
            std::cout << carte.getId() << ". " << carte.getTitlu() << " - " << carte.getAutor() << " - " << carte.getAnAparitie() << "\n";
        }
    }
}

void UI::undo() {
    try {
        service.undo();
        std::cout << "Undo efectuat cu succes!\n";
    } catch (const std::exception &e) {
        std::cout << "Eroare: " << e.what() << "\n";
    }
}

void UI::run() {
    // service.golesteCos();
    while (true) {
        afiseazaMeniu();

        int optiune;
        std::cout << "Introduceti optiunea: ";
        std::cin >> optiune;

        switch (optiune) {
            case 1:
                adaugaCarte();
                break;
            case 2:
                stergeCarte();
                break;
            case 3:
                modificaTitlu();
                break;
            case 4:
                modificaAutor();
                break;
            case 5:
                modificaGen();
                break;
            case 6:
                modificaAnAparitie();
                break;
            case 7:
                afiseazaCarti();
                break;
            case 8:
                cautaCarte();
                break;
            case 9:
                filtreazaDupaTitlu();
                break;
            case 10:
                filtreazaDupaAnulAparitiei();
                break;
            case 11:
                sorteazaDupaTitlu();
                break;
            case 12:
                sorteazaDupaAutor();
                break;
            case 13:
                sorteazaDupaGen();
                break;
            case 14:
                sorteazaDupaAnulAparitiei();
                break;
            case 15:
                golesteCos();
                break;
            case 16:
                adaugaCarteInCos();
                break;
            case 17:
                genereazaCosAleator();
                break;
            case 18:
                exportaCosInCsv();
                break;
            case 19:
                afiseazaCartiDupaGen();
                break;
            case 20:
                undo();
                break;
            case 0:
                std::cout << "Aplicatia s-a inchis!\n";
                return;
            default:
                std::cout << "Optiune invalida!\n";
        }
    }
}

