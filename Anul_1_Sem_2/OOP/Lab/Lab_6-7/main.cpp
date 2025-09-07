#include <cassert>
#include "repository/carte_repo.h"
#include "service/carte_service.h"
#include "ui/console.h"
#include "tests/tests.h"
#include "vector_dinamic/lista.h"
#include <vector>

/*
  Functie folosit in teste
  primeste vector prin valoare -> copy constructor
  returneaza prin valoare -> copy constructor sau move constructor
*/
template <typename MyVector>
MyVector testCopyIterate(MyVector v) {
    int an_total = 0;
    for (auto el : v) {
        an_total += el.getAnAparitie();
    }
    Carte c { 1,"ion","liviu", "bac", an_total };
    v.push_back(c);
    return v;
}

template <typename MyVector>
void addCarte(MyVector& v, int cate) {
    for (int i = 0; i < cate; i++) {
        Carte c{ i + 1, std::to_string(i) + "_titlu", std::to_string(i) + "_autor", std::to_string(i) + "_gen",i + 10 };
        v.push_back(c);
    }
}

/*
Testare constructori / assignment
E template pentru a refolosi la diferite variante de VectorDinamic din proiect
*/
template <typename MyVector>
void testCreateCopyAssign() {
    MyVector v;//default constructor
    addCarte(v, 100);
    assert(v.getSize() == 100);
    assert(v[50].getTitlu() == "50_titlu");

    MyVector v2{ v };//constructor de copiere
    assert(v2.getSize() == 100);
    assert(v2[50].getTitlu() == "50_titlu");

    MyVector v3;//default constructor
    v3 = v;//operator=   assignment
    assert(v3.getSize() == 100);
    assert(v3[50].getTitlu() == "50_titlu");

    auto v4 = testCopyIterate(v3);
    assert(v4.getSize() == 101);
    assert(v4[50].getTitlu() == "50_titlu");
}

/*
  Test pentru move constructor si move assgnment
*/
template <typename MyVector>
void testMoveConstrAssgnment() {
    std::vector<MyVector> v;
    //adaugam un vector care este o variabila temporara
    //se v-a apela move constructor
    v.push_back(MyVector{});

    //inseram, la fel se apeleaza move costructor de la vectorul nostru
    v.insert(v.begin(),MyVector{});

    assert(v.size() == 2);

    MyVector v2;
    addCarte(v2, 50);

    v2 = MyVector{};//move assignment

    assert(v2.getSize() == 0);

}
template <typename MyVector>
void testAll1() {
    testCreateCopyAssign<MyVector>();
    testMoveConstrAssgnment<MyVector>();
}

int main() {
    testAll1<Lista<Carte>>();
    testAll();
    CarteRepo repo;
    CarteService service(repo);
    UI ui(service);
    ui.run();

    return 0;
}
