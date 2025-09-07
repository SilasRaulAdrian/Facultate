#include "tests.h"
#include "../domain/tractor.h"
#include "../repository/tractor_repo.h"
#include "../service/tractor_service.h"
#include <cassert>
#include <fstream>
#include <iostream>
#include <stdexcept>

void testDomain() {
    Tractor t(1, "TractorA", "TIP1", 4);
    assert(t.getId() == 1);
    assert(t.getDenumire() == "TractorA");
    assert(t.getTip() == "TIP1");
    assert(t.getNumarRoti() == 4);
    t.setNumarRoti(6);
    assert(t.getNumarRoti() == 6);
    std::cout << "Testele pentru domain au trecut\n";
}

void testRepo() {
    std::ofstream out("test_tractoare.txt");
    out << "1,TractorA,TIP1,4\n";
    out << "2,TractorB,TIP2,6\n";
    out << "3,TractorC,TIP1,4\n";
    out.close();

    RepoTractor repo("test_tractoare.txt");
    auto tractoare = repo.getAll();
    assert(tractoare.size() == 3);
    assert(tractoare[0].getId() == 1);
    assert(tractoare[1].getTip() == "TIP2");

    Tractor nou(4, "TractorD", "TIP3", 4);
    repo.adauga(nou);
    tractoare = repo.getAll();
    assert(tractoare.size() == 4);

    bool exceptieAruncata = false;
    try {
        repo.adauga(Tractor(4, "TractorE", "TIP4", 4));
    } catch (const std::runtime_error&) {
        exceptieAruncata = true;
    }
    assert(exceptieAruncata);

    repo.modifica(Tractor(2, "TractorB", "TIP2", 8));
    tractoare = repo.getAll();
    bool found = false;
    for (auto& t : tractoare) {
        if (t.getId() == 2) {
            assert(t.getNumarRoti() == 8);
            found = true;
        }
    }
    assert(found);

    std::cout << "Testele pentru repo au trecut\n";
}

void testService() {
    std::ofstream out("test_tractoare.txt");
    out << "1,TractorA,TIP1,4\n";
    out << "2,TractorB,TIP2,6\n";
    out << "3,TractorC,TIP1,4\n";
    out.close();

    RepoTractor repo("test_tractoare.txt");
    ServiceTractor service(repo);

    auto sorted = service.getAllSorted();
    assert(sorted.size() == 3);
    assert(sorted[0].getDenumire() == "TractorA");
    assert(sorted[1].getDenumire() == "TractorB");
    assert(sorted[2].getDenumire() == "TractorC");

    service.adaugaTractor(4, "TractorD", "TIP1", 4);
    auto all = service.getAllSorted();
    assert(all.size() == 4);

    bool exceptie = false;
    try {
        service.adaugaTractor(5, "", "TIP1", 4);
    } catch (const std::runtime_error&) {
        exceptie = true;
    }
    assert(exceptie);

    exceptie = false;
    try {
        service.adaugaTractor(5, "X", "", 4);
    } catch (const std::runtime_error&) {
        exceptie = true;
    }
    assert(exceptie);

    exceptie = false;
    try {
        service.adaugaTractor(5, "X", "TIP1", 3);
    } catch (const std::runtime_error&) {
        exceptie = true;
    }
    assert(exceptie);

    exceptie = false;
    try {
        service.adaugaTractor(5, "X", "TIP1", 0);
    } catch (const std::runtime_error&) {
        exceptie = true;
    }
    assert(exceptie);

    QMap<QString, int> count = service.countByType();
    assert(count["TIP1"] == 3);
    assert(count["TIP2"] == 1);

    QSet<QString> types = service.getDistinctTypes();
    assert(types.contains("TIP1"));
    assert(types.contains("TIP2"));

    auto before = service.getById(1);
    int beforeRoti = before.getNumarRoti();
    service.decrementRoti(1);
    auto after = service.getById(1);
    assert(after.getNumarRoti() == beforeRoti - 2);

    auto t = service.getById(2);
    assert(t.getId() == 2);

    exceptie = false;
    try {
        service.getById(999);
    } catch (const std::runtime_error&) {
        exceptie = true;
    }
    assert(exceptie);

    std::cout << "Testele pentru service au trecut\n";
}

void testAll() {
    std::cout << "Ruleaza toate testele...\n";
    testDomain();
    testRepo();
    testService();
    std::cout << "Toate testele au trecut cu succes!\n";
}