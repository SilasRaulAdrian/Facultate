#include "tests.h"
#include "../domain/melodie.h"
#include "../repository/melodii_repo.h"
#include "../service/melodii_service.h"
#include "../validator/validator.h"
#include <iostream>
#include <fstream>

void testDomain() {
    Melodie m(1, "Imagine", "John Lennon", "rock");

    assert(m.getId() == 1);
    assert(m.getTitlu() == "Imagine");
    assert(m.getArtist() == "John Lennon");
    assert(m.getGen() == "rock");

    m.setTitlu("Imagine (Remastered)");
    assert(m.getTitlu() == "Imagine (Remastered)");

    m.setGen("pop");
    assert(m.getGen() == "pop");

    std::cout << "Testele pentru domain au trecut.\n";
}

void testRepo() {
    std::ofstream out("test_melodii.txt");
    out << "1,Imagine,John Lennon,rock\n";
    out << "2,Billie Jean,Michael Jackson,pop\n";
    out.close();

    RepoMelodii repo("test_melodii.txt");

    auto all = repo.getAll();
    assert(all.size() == 2);
    assert(all[0].getTitlu() == "Imagine");

    int newId = repo.genereazaId();
    assert(newId == 3);

    repo.adauga(Melodie{newId, "Shape of You", "Ed Sheeran", "pop"});
    assert(repo.getAll().size() == 3);

    repo.sterge(1);
    auto afterDelete = repo.getAll();
    assert(afterDelete.size() == 2);
    for (const auto& m : afterDelete) {
        assert(m.getId() != 1);
    }

    std::cout << "Testele pentru repo au trecut.\n";
}

void testService() {
    std::ofstream out("test_melodii.txt");
    out << "1,Imagine,John Lennon,rock\n";
    out << "2,Billie Jean,Michael Jackson,pop\n";
    out.close();

    RepoMelodii repo("test_melodii.txt");
    ValidatorMelodie validator;
    ServiceMelodii srv(repo, validator);

    srv.adaugaMelodie("Shape of You", "Ed Sheeran", "pop");
    auto all = srv.getAllSortate();
    assert(all.size() == 3);
    assert(all[0].getArtist() <= all[1].getArtist());

    srv.stergeMelodie(1);
    assert(srv.getAllSortate().size() == 2);

    assert(srv.countArtist("Ed Sheeran") == 1);
    assert(srv.countGen("pop") == 2);

    std::cout << "Testele pentru service au trecut.\n";
}

void testValidator() {
    ValidatorMelodie validator;

    Melodie m{1, "Song", "Artist", "rock"};
    validator.valideaza(m);

    try {
        validator.valideaza(Melodie{-1, "Song", "Artist", "pop"});
        assert(false);
    } catch (std::exception&) {}

    try {
        validator.valideaza(Melodie{1, "", "Artist", "pop"});
        assert(false);
    } catch (std::exception&) {}

    try {
        validator.valideaza(Melodie{1, "Song", "", "pop"});
        assert(false);
    } catch (std::exception&) {}

    try {
        validator.valideaza(Melodie{1, "Song", "Artist", "metal"});
        assert(false);
    } catch (std::exception&) {}

    std::cout << "Testele pentru validator au trecut.\n";
}

void testAll() {
    std::cout << "Rulam toate testele...\n";
    testDomain();
    testRepo();
    testService();
    testValidator();
    std::cout << "Toate testele au trecut cu succes!\n";
}
