#include "../domain/carte.h"
#include "../repository/carte_repo.h"
#include "../service/carte_service.h"
#include "../exceptions/exceptii.h"
#include <cassert>
#include <iostream>
#include <string>
#include <random>
#include <fstream>

void testDomain() {
    // Test Constructor
    Carte carte(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    Carte carteDefault;
    assert(carte.getId() == 1);
    assert(carte.getTitlu() == "Ion");
    assert(carte.getAutor() == "Liviu Rebreanu");
    assert(carte.getGen() == "Roman");
    assert(carte.getAnAparitie() == 1920);

    // Test Getters
    assert(carte.getId() == 1);
    assert(carte.getTitlu() == "Ion");
    assert(carte.getAutor() == "Liviu Rebreanu");
    assert(carte.getGen() == "Roman");
    assert(carte.getAnAparitie() == 1920);

    // Test Setters
    carte.setTitlu("Morometii");
    carte.setAutor("Marin Preda");
    carte.setGen("Drama");
    carte.setAnAparitie(1955);

    assert(carte.getTitlu() == "Morometii");
    assert(carte.getAutor() == "Marin Preda");
    assert(carte.getGen() == "Drama");
    assert(carte.getAnAparitie() == 1955);

    std::cout << "Testele pentru domeniu au trecut cu succes!\n";
}

void testRepo() {
    CarteRepo repo;
    Carte carte(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    Carte carte2(2, "Morometii", "Marin Preda", "Drama", 1955);

    // Test Adauga Carte
    repo.adaugaCarte(carte);
    repo.adaugaCarte(carte2);
    assert(repo.getCarti().getSize() == 2);

    try {
        repo.adaugaCarte(carte);
    } catch (const RepoException &e) {
        std::cout << "Exceptie prinsa la adaugare duplicat: " << e.what() << std::endl;
        assert(repo.getCarti().getSize() == 2);
    }

    // Test Sterge Carte
    repo.stergeCarte(1);
    assert(repo.getCarti().getSize() == 1);
    try {
        repo.stergeCarte(10);
    } catch (const RepoException &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
        assert(repo.getCarti().getSize() == 1);
    }

    // Test Modifica Titlu
    repo.modificaTitlu(2, "Morometii 2");
    assert(repo.getCarti()[0].getTitlu() == "Morometii 2");
    try {
        repo.modificaTitlu(10, "Morometii 3");
    } catch (const RepoException &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
        assert(repo.getCarti()[0].getTitlu() == "Morometii 2");
    }

    // Test Modifica Autor
    repo.modificaAutor(2, "Marin Preda 2");
    assert(repo.getCarti()[0].getAutor() == "Marin Preda 2");
    try {
        repo.modificaAutor(10, "Marin Preda 3");
    } catch (const RepoException &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
        assert(repo.getCarti()[0].getAutor() == "Marin Preda 2");
    }

    // Test Modifica Gen
    repo.modificaGen(2, "Drama 2");
    assert(repo.getCarti()[0].getGen() == "Drama 2");
    try {
        repo.modificaGen(10, "Drama 3");
    } catch (const RepoException &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
        assert(repo.getCarti()[0].getGen() == "Drama 2");
    }

    // Test Modifica An Aparitie
    repo.modificaAnAparitie(2, 1956);
    assert(repo.getCarti()[0].getAnAparitie() == 1956);
    try {
        repo.modificaAnAparitie(10, 1957);
    } catch (const RepoException &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
        assert(repo.getCarti()[0].getAnAparitie() == 1956);
    }

    // Test Cauta Carte
    assert(repo.cautaCarte("Morometii 2").getTitlu() == "Morometii 2");
    try {
        repo.cautaCarte("Morometii 3");
    } catch (const RepoException &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
        assert(repo.cautaCarte("Morometii 2").getTitlu() == "Morometii 2");
    }

    std::cout << "Testele pentru repository au trecut cu succes!\n";
}

void testService() {
    CarteRepo repo;
    CarteService service(repo);

    service.adaugaCarte(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    service.adaugaCarte(2, "Morometii", "Marin Preda", "Drama", 1955);
    service.adaugaCarte(3, "Baltagul", "Mihail Sadoveanu", "Mister", 1930);
    assert(service.getCarti().getSize() == 3);

    try {
        service.adaugaCarte(-1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    } catch (const ValidationException &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
        assert(service.getCarti().getSize() == 3);
    }

    try {
        service.getTitluById(10);
    } catch (const std::runtime_error &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
        assert(service.getCarti().getSize() == 3);
    }

    Lista<Carte> filtrateTitlu = service.filtreazaDupaTitlu("Baltagul");
    assert(filtrateTitlu.getSize() == 1);
    assert(filtrateTitlu[0].getTitlu() == "Baltagul");

    Lista<Carte> filtrareFaraRezultat = service.filtreazaDupaTitlu("Carte Necunoscuta");
    assert(filtrareFaraRezultat.getSize() == 0);

    Lista<Carte> filtrateAn = service.filtreazaDupaAn(1930);
    assert(filtrateAn.getSize() == 1);
    assert(filtrateAn[0].getTitlu() == "Baltagul");

    Lista<Carte> filtrareAnInexistent = service.filtreazaDupaAn(1800);
    assert(filtrareAnInexistent.getSize() == 0);

    Lista<Carte> sortateTitluCrescator = service.sorteazaDupaTitlu(0);
    assert(sortateTitluCrescator.getSize() == 3);
    assert(sortateTitluCrescator[0].getTitlu() == "Baltagul");

    Lista<Carte> sortateTitluDescrescator = service.sorteazaDupaTitlu(1);
    assert(sortateTitluDescrescator.getSize() == 3);
    assert(sortateTitluDescrescator[0].getTitlu() == "Morometii");

    Lista<Carte> sortateAutor = service.sorteazaDupaAutor(0);
    assert(sortateAutor[0].getAutor() == "Liviu Rebreanu");

    Lista<Carte> sortateAutorDescrescator = service.sorteazaDupaAutor(1);
    assert(sortateAutorDescrescator[0].getAutor() == "Mihail Sadoveanu");

    Lista<Carte> sortateAn = service.sorteazaDupaAnulAparitiei(0);
    assert(sortateAn[0].getAnAparitie() == 1920);

    Lista<Carte> sortateAnDescrescator = service.sorteazaDupaAnulAparitiei(1);
    assert(sortateAnDescrescator[0].getAnAparitie() == 1955);

    Lista<Carte> sortateGen = service.sorteazaDupaGen(0);
    assert(sortateGen[0].getGen() == "Drama");

    Lista<Carte> sortateGenDescrescator = service.sorteazaDupaGen(1);
    assert(sortateGenDescrescator[0].getGen() == "Roman");

    assert(service.cautaCarte("Morometii").getTitlu() == "Morometii");
    try {
        service.cautaCarte("Carte Inexistenta");
    } catch (const RepoException &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
        assert(service.cautaCarte("Morometii").getTitlu() == "Morometii");
    }

    service.stergeCarte(1);
    assert(service.getCarti().getSize() == 2);

    service.modificaTitlu(2, "Morometii 2");
    assert(service.getCarti()[0].getTitlu() == "Morometii 2");

    service.modificaAutor(2, "Marin Preda 2");
    assert(service.getCarti()[0].getAutor() == "Marin Preda 2");

    service.modificaGen(2, "Drama 2");
    assert(service.getCarti()[0].getGen() == "Drama 2");

    service.modificaAnAparitie(2, 1956);
    assert(service.getCarti()[0].getAnAparitie() == 1956);

    service.adaugaCarte(100, "Test Carte", "Autor Test", "Gen Test", 2020);
    assert(service.getCarti().getSize() == 3);
    service.undo();
    assert(service.getCarti().getSize() == 2);

    int undoCount = 0;
    try {
        while (true) {
            service.undo();
            undoCount++;
        }
    } catch (const std::runtime_error& e) {
        std::cout << "S-au efectuat " << undoCount << " undo-uri inainte de exceptie: " << e.what() << std::endl;
        assert(undoCount > 0);
    }

    std::cout << "Testele pentru service au trecut cu succes!\n";
}

void testLista() {
    Lista<Carte> listaCarti;

    Carte carte1(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    Carte carte2(2, "Morometii", "Marin Preda", "Drama", 1955);
    Carte carte3(3, "Enigma Otiliei", "George Calinescu", "Roman", 1938);
    Carte carte4(4, "Mizerabilii", "Victor Hugo", "Drama", 1862);

    listaCarti.push_back(carte1);
    listaCarti.push_back(carte2);
    listaCarti.push_back(carte3);
    listaCarti.push_back(carte4);

    Lista<Carte> listaCarti2;
    listaCarti2 = listaCarti;
    assert(listaCarti2.getSize() == 4);
    assert(listaCarti2[0].getTitlu() == "Ion");
    assert(listaCarti2[1].getTitlu() == "Morometii");
    assert(listaCarti2[2].getTitlu() == "Enigma Otiliei");
    assert(listaCarti2[3].getTitlu() == "Mizerabilii");

    listaCarti = listaCarti;
    assert(listaCarti.getSize() == 4);

    assert(listaCarti[0].getTitlu() == "Ion");
    assert(listaCarti[1].getTitlu() == "Morometii");
    assert(listaCarti[2].getTitlu() == "Enigma Otiliei");
    assert(listaCarti[3].getTitlu() == "Mizerabilii");

    assert(listaCarti.exista(carte1) == true);
    assert(listaCarti.exista(carte2) == true);
    assert(listaCarti.exista(carte3) == true);
    assert(listaCarti.exista(carte4) == true);
    assert(listaCarti.exista(Carte(5, "Nonexistent", "Autor", "Gen", 2020)) == false);

    for (int i = 5; i < 15; ++i) {
        listaCarti.push_back(Carte(i, "Carte " + std::to_string(i), "Autor " + std::to_string(i), "Gen", 2000 + i));
    }
    assert(listaCarti.getSize() == 14);

    assert(listaCarti.sterge(carte1) == true);
    assert(listaCarti.getSize() == 13);

    try {
        listaCarti.sterge(Carte(100, "Nonexistent", "Autor", "Gen", 2020));
    } catch (const std::runtime_error &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
    }

    try {
        listaCarti[14];
    } catch (const std::out_of_range &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
    }

    try {
        listaCarti.push_back(carte2);
    } catch (const std::runtime_error &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
    }

    assert(listaCarti.getSize() == 13);

    std::cout << "Testele pentru lista de carti au trecut cu succes!\n";
}

void testCos() {
    CosInchirieri cos;
    assert(cos.getSizeCos() == 0);

    Carte carte1(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    Carte carte2(2, "Morometii", "Marin Preda", "Drama", 1955);

    cos.adaugaCarte(carte1);
    cos.adaugaCarte(carte2);
    assert(cos.getSizeCos() == 2);
    assert(cos.getAllCos()[0].getTitlu() == "Ion");
    assert(cos.getAllCos()[1].getTitlu() == "Morometii");

    cos.golesteCos();
    assert(cos.getSizeCos() == 0);

    Lista<Carte> carti;
    carti.push_back(carte1);
    carti.push_back(carte2);

    cos.genereazaCos(2, carti);
    assert(cos.getSizeCos() == 2);

    const std::string numeFisier = "../cos.csv";
    cos.exportCsv(numeFisier);

    std::ifstream f(numeFisier);
    assert(f.is_open());

    std::string linie;
    int nrLinii = 0;
    while (std::getline(f, linie)) {
        ++nrLinii;
    }
    f.close();
    assert(nrLinii == 2);

    std::remove(numeFisier.c_str());

    bool exceptiePrinsa = false;
    try {
        cos.genereazaCos(5, carti);
    } catch (const std::runtime_error &e) {
        exceptiePrinsa = true;
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
    }
    assert(exceptiePrinsa == true);

    exceptiePrinsa = false;
    try {
        cos.genereazaCos(0, carti);
    } catch (const std::runtime_error &e) {
        exceptiePrinsa = true;
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
    }
    assert(exceptiePrinsa == true);

    std::cout << "Testele pentru cos au trecut cu succes!\n";
}

void testGolesteCos() {
    CarteRepo repo;
    CarteService service(repo);

    service.adaugaCarte(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    service.adaugaCarte(2, "Morometii", "Marin Preda", "Drama", 1955);

    service.golesteCos();
    assert(service.getSizeCos() == 0);

    std::cout << "Testul pentru golesteCos a trecut cu succes!\n";
}

void testAdaugaCarteCos() {
    CarteRepo repo;
    CarteService service(repo);

    service.adaugaCarte(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    service.adaugaCarte(2, "Morometii", "Marin Preda", "Drama", 1955);

    service.adaugaCarteDupaTitlu("Ion");
    assert(service.getSizeCos() == 1);

    try {
        service.adaugaCarteDupaTitlu("Carte Inexistentă");
    } catch (const RepoException &e) {
        std::cout << "Exceptie prinsa: " << e.what() << std::endl;
    }

    std::cout << "Testul pentru adaugaCarteDupaTitlu a trecut cu succes!\n";
}

void testGenereazaCosSiExportCsv() {
    CarteRepo repo;
    CarteService service(repo);

    Lista<Carte> carti;
    carti.push_back(Carte(1, "Ion", "Liviu Rebreanu", "Roman", 1920));
    carti.push_back(Carte(2, "Morometii", "Marin Preda", "Drama", 1955));

    service.genereazaCos(2, carti);
    assert(service.getSizeCos() == 2);

    const std::string numeFisier = "../cos.csv";
    service.exportCsv(numeFisier);
    std::ifstream fisier(numeFisier);
    assert(fisier.is_open());

    std::string linie;
    int count = 0;
    while (std::getline(fisier, linie)) {
        count++;
    }
    std::cout << "Numarul de linii din fisier: " << count << std::endl;
    assert(count == 2);
    fisier.close();

    std::cout << "Testul pentru genereazaCos si exportCsv a trecut cu succes!\n";
}

void testGetSizeCos() {
    CarteRepo repo;
    CarteService service(repo);

    service.adaugaCarte(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    service.adaugaCarte(2, "Morometii", "Marin Preda", "Drama", 1955);

    assert(service.getSizeCos() == 0);

    service.adaugaCarteDupaTitlu("Ion");
    assert(service.getSizeCos() == 1);

    service.adaugaCarteDupaTitlu("Morometii");
    assert(service.getSizeCos() == 2);

    std::cout << "Testul pentru getSizeCos a trecut cu succes!\n";
}

void testGetAllCos() {
    CarteRepo repo;
    CarteService service(repo);

    service.adaugaCarte(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    service.adaugaCarte(2, "Morometii", "Marin Preda", "Drama", 1955);

    service.adaugaCarteDupaTitlu("Ion");
    service.adaugaCarteDupaTitlu("Morometii");

    const auto& cartiCos = service.getAllCos();
    assert(cartiCos.size() == 2);

    std::cout << "Testul pentru getAllCos a trecut cu succes!\n";
}

void testGetCartiPeGen() {
    CarteRepo repo;
    CarteService service(repo);

    service.adaugaCarte(1, "Ion", "Liviu Rebreanu", "Roman", 1920);
    service.adaugaCarte(2, "Morometii", "Marin Preda", "Drama", 1955);
    service.adaugaCarte(3, "Enigma Otiliei", "George Calinescu", "Roman", 1938);
    service.adaugaCarte(4, "Mizerabilii", "Victor Hugo", "Drama", 1862);

    auto cartiPeGen = service.getCartiPeGen();
    assert(cartiPeGen["Roman"].size() == 2);
    assert(cartiPeGen["Drama"].size() == 2);

    std::cout << "Testul pentru getCartiPeGen a trecut cu succes!\n";
}

void testAll() {
    std::cout << "Se ruleaza testele...\n";
    testLista();
    testDomain();
    testRepo();
    testService();
    testCos();
    testGolesteCos();
    testAdaugaCarteCos();
    testGenereazaCosSiExportCsv();
    testGetSizeCos();
    testGetAllCos();
    testGetCartiPeGen();
    std::cout << "Toate testele au trecut cu succes!\n";
}