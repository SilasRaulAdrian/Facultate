#include "tests.h"
#include "../domain/produs.h"
#include "../repository/produse_repo.h"
#include "../service/produse_service.h"
#include "../validator/validator.h"
#include <iostream>
#include <fstream>

void testDomain() {
    Produs p(1, "Lapte", "aliment", 5.5);

    assert(p.getId() == 1);
    assert(p.getNume() == "Lapte");
    assert(p.getTip() == "aliment");
    assert(p.getPret() == 5.5);

    p.setNume("Lapte integral");
    assert(p.getNume() == "Lapte integral");

    p.setTip("aliment nou");
    assert(p.getTip() == "aliment nou");

    p.setPret(10.0);
    assert(p.getPret() == 10.0);

    std::cout << "Testele pentru domain (Produs) au trecut.\n";
}

void testRepo() {
    std::ofstream out("test_produse.txt");
    out << "1,Lapte,aliment,5.5\n";
    out << "2,Pix,papetarie,2.2\n";
    out.close();

    RepoProduse repo("test_produse.txt");
    auto all = repo.getAll();
    assert(all.size() == 2);
    assert(all[0].getNume() == "Lapte");

    repo.adauga(Produs{3, "Apa", "bautura", 3.0});
    auto newAll = repo.getAll();
    assert(newAll.size() == 3);
    assert(newAll[2].getTip() == "bautura");

    std::cout << "Testele pentru repo (RepoProduse) au trecut.\n";
}

void testService() {
    std::ofstream out("test_produse.txt");
    out << "1,Lapte,aliment,5.5\n";
    out << "2,Pix,papetarie,2.2\n";
    out.close();

    RepoProduse repo("test_produse.txt");
    ValidatorProduse validator;
    ServiceProduse service(repo, validator);

    assert(service.getAllSortat().size() == 2);
    assert(service.getAllSortat()[0].getPret() <= service.getAllSortat()[1].getPret());

    service.adaugaProdus(3, "Apa", "bautura", 3.0);
    assert(service.getAllSortat().size() == 3);

    assert(service.countByTip("aliment") == 1);
    assert(service.countByTip("bautura") == 1);

    std::vector<QString> tipuri = service.getTipuri();
    assert(tipuri.size() >= 2);

    service.setPretFiltru(50);
    assert(service.getPretFiltru() == 50);

    std::cout << "Testele pentru service (ServiceProduse) au trecut.\n";
}

void testValidator() {
    ValidatorProduse validator;
    std::vector<int> existingIds = {1, 2, 3};

    try {
        validator.valideaza(4, "Cana", "bucatarie", 15.0, existingIds);
    } catch (...) {
        assert(false);
    }

    try {
        validator.valideaza(5, "   ", "bucatarie", 15.0, existingIds);
        assert(false);
    } catch (const Validator& v) {}

    try {
        validator.valideaza(6, "Ceas", "accesorii", 0.5, existingIds);
        assert(false);
    } catch (const Validator& v) {}

    try {
        validator.valideaza(1, "Produs duplicat", "diverse", 10.0, existingIds);
        assert(false);
    } catch (const Validator& v) {}

    std::cout << "Testele pentru validator (ValidatorProduse) au trecut.\n";
}

void testAll() {
    std::cout << "Rulam toate testele pentru Produse...\n";
    testDomain();
    testRepo();
    testService();
    testValidator();
    std::cout << "Toate testele pentru Produse au trecut cu succes!\n";
}
