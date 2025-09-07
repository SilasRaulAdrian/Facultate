#include "../domain/oferta.h"
#include "../repo/repo_oferte.h"
#include "../service/service.h"
#include <string.h>
#include <assert.h>
#include <stdlib.h>
#include <stdio.h>

void teste_domain() {
    Oferta *oferta = creeaza_oferta(1, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    assert(strcmp(oferta->tip, "teren") == 0);
    assert(oferta->pret == 100520);
    assert(strcmp(oferta->adresa, "Strada Mihai Viteazu 124") == 0);
    assert(oferta->suprafata == 1521);

    Oferta *copie = copie_oferta(oferta);
    assert(strcmp(copie->tip, "teren") == 0);
    assert(copie->pret == 100520);
    assert(strcmp(copie->adresa, "Strada Mihai Viteazu 124") == 0);
    assert(copie->suprafata == 1521);

    distruge_oferta(copie);
    distruge_oferta(oferta);
}

void teste_repo() {
    Oferta *oferta = creeaza_oferta(1, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    assert(strcmp(oferta->tip, "teren") == 0);
    assert(oferta->pret == 100520);
    assert(strcmp(oferta->adresa, "Strada Mihai Viteazu 124") == 0);
    assert(oferta->suprafata == 1521);
    Repo *repo = creeazaRepo();
    assert(repo->lungime == 0);
    assert(repo->dimensiune == 10);
    adauga_oferta(repo,oferta);
    assert(repo->lungime == 1);
    assert(repo->dimensiune == 10);
    assert(repo->oferte[0]->id == 1);
    assert(strcmp(repo->oferte[0]->tip, "teren") == 0);
    assert(repo->oferte[0]->suprafata == 1521);
    assert(repo->oferte[0]->pret == 100520);
    assert(strcmp(repo->oferte[0]->adresa, "Strada Mihai Viteazu 124") == 0);
    Oferta *oferta2 = creeaza_oferta(2, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta3 = creeaza_oferta(3, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta4 = creeaza_oferta(4, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta5 = creeaza_oferta(5, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta6 = creeaza_oferta(6, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta7 = creeaza_oferta(7, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta8 = creeaza_oferta(8, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta9 = creeaza_oferta(9, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta10 = creeaza_oferta(10, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Oferta *oferta11 = creeaza_oferta(11, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    adauga_oferta(repo, oferta2);
    adauga_oferta(repo, oferta3);
    adauga_oferta(repo, oferta4);
    adauga_oferta(repo, oferta5);
    adauga_oferta(repo, oferta6);
    adauga_oferta(repo, oferta7);
    adauga_oferta(repo, oferta8);
    adauga_oferta(repo, oferta9);
    adauga_oferta(repo, oferta10);
    adauga_oferta(repo, oferta11);
    assert(repo->lungime == 11);
    assert(repo->dimensiune == 20);

    modifica_tip(repo, "apartament", 1);
    assert(strcmp(repo->oferte[0]->tip, "apartament") == 0);

    modifica_adresa(repo, "Strada Iuliu Maniu 12", 1);
    assert(strcmp(repo->oferte[0]->adresa, "Strada Iuliu Maniu 12") == 0);

    modifica_pret(repo, 500000, 1);
    assert(repo->oferte[0]->pret == 500000);

    modifica_suprafata(repo, 4000, 1);
    assert(repo->oferte[0]->suprafata == 4000);

    sterge_oferta(repo, 1);
    assert(repo->lungime == 10);
    assert(sterge_oferta(repo, 1) == 0);

    distruge_oferta(oferta);
    distruge_repo(repo);
}

void teste_service() {
    Oferta *oferta = creeaza_oferta(1, "teren", 1521, "Strada Mihai Viteazu 124", 100520);
    Service *service = creeazaService();
    assert(service->oferte->lungime == 0);
    assert(service->oferte->dimensiune == 10);

    assert(adauga_oferta_service(service,oferta) == 1);

    oferta->id = -3;
    assert(adauga_oferta_service(service,oferta) == 0);
    oferta->id = 1;
    Oferta *oferta2 = creeaza_oferta(1, "apartament", 15211, "Strada Mihai Viteazu 24", 10052);
    assert(adauga_oferta_service(service,oferta2) == 0);
    assert(service->oferte->lungime == 1);

    assert(modifica_tip_service(service, 1, "") == 0);
    assert(modifica_tip_service(service, 0, "teren") == 0);
    assert(modifica_tip_service(service, 1, "masina") == 0);
    assert(modifica_tip_service(service, 2, "teren") == 0);
    assert(modifica_tip_service(service, 1, "apartament") == 1);
    assert(strcmp(service->oferte->oferte[0]->tip, "apartament") == 0);

    assert(modifica_adresa_service(service, 1, "") == 0);
    assert(modifica_adresa_service(service, 0, "bizusa") == 0);
    assert(modifica_adresa_service(service, 2, "baita") == 0);
    assert(modifica_adresa_service(service, 1, "bizusa 12") == 1);
    assert(strcmp(service->oferte->oferte[0]->adresa, "bizusa 12") == 0);

    assert(modifica_pret_service(service, 3, 1000) == 0);
    assert(modifica_pret_service(service, 1, -50000) == 0);
    assert(modifica_pret_service(service, 1, 46000) == 1);
    assert(service->oferte->oferte[0]->pret == 46000);

    assert(modifica_suprafata_service(service, 3, 1000) == 0);
    assert(modifica_suprafata_service(service, 1, 6000) == 1);
    assert(service->oferte->oferte[0]->suprafata == 6000);
    assert(modifica_suprafata_service(service, 1, -7000) == 0);

    assert(sterge_oferta_service(service, 0) == 0);
    assert(sterge_oferta_service(service, 2) == 0);
    assert(sterge_oferta_service(service, 1) == 1);
    assert(service->oferte->lungime == 0);

    assert(adauga_oferta_service(service,oferta) == 1);
    assert(service->oferte->lungime == 1);
    Oferta *oferta3 = creeaza_oferta(3, "casa", 5000, "Strada Calistrat Hogas 6", 50000);
    assert(adauga_oferta_service(service, oferta3) == 1);
    assert(service->oferte->lungime == 2);

    Repo* repo2 = oferte_ordonate_pret(service, 1); // ordonata descrescator
    assert(repo2->oferte[0]->pret == 50000);
    assert(repo2->oferte[1]->pret == 46000);

    distruge_repo(repo2);

    repo2 = oferte_ordonate_pret(service, 0);
    assert(repo2->oferte[0]->pret == 46000);
    assert(repo2->oferte[1]->pret == 50000);

    distruge_repo(repo2);

    Repo* repo3 = oferte_ordonate_tip(service, 1);
    assert(strcmp(repo3->oferte[0]->tip, "casa") == 0);
    assert(strcmp(repo3->oferte[1]->tip, "apartament") == 0);

    distruge_repo(repo3);

    repo3 = oferte_ordonate_tip(service, 0);
    assert(strcmp(repo3->oferte[0]->tip, "apartament") == 0);
    assert(strcmp(repo3->oferte[1]->tip, "casa") == 0);

    distruge_repo(repo3);

    Repo *repo4 = oferte_filtrate_tip(service, "teren");
    assert(repo4->lungime == 0);

    distruge_repo(repo4);

    repo4 = oferte_filtrate_tip(service, "casa");
    assert(repo4->lungime == 1);
    assert(strcmp(repo4->oferte[0]->tip, "casa") == 0);

    distruge_repo(repo4);

    Repo *repo_invalid = oferte_filtrate_tip(service, "invalid");
    assert(repo_invalid == NULL);
    distrugeService(service);
    distruge_oferta(oferta2);
}

void teste_undo_service() {
    Service *service = creeazaService();

    Oferta *oferta1 = creeaza_oferta(1, "teren", 1000, "Strada 1", 10000);
    Oferta *oferta2 = creeaza_oferta(2, "casa", 2000, "Strada 2", 20000);

    adauga_oferta_service(service, oferta1);
    adauga_oferta_service(service, oferta2);

    assert(service->oferte->lungime == 2);

    undo_service(service);
    assert(service->oferte->lungime == 1);

    undo_service(service);
    assert(service->oferte->lungime == 0);

    undo_service(service);
    assert(service->oferte->lungime == 0);

    distrugeService(service);
}

void run_all_tests() {
    teste_domain();
    teste_repo();
    teste_service();
    teste_undo_service();
}