#include <cassert>
#include <iostream>
#include <fstream>
#include "../domain/device.h"
#include "../repository/device_repo.h"
#include "../service/device_service.h"

void testDomain() {
    Device d("telefon", "iPhone 14", 2023, "rosu", 5500.0);
    assert(d.getTip() == "telefon");
    assert(d.getModel() == "iPhone 14");
    assert(d.getAn() == 2023);
    assert(d.getCuloare() == "rosu");
    assert(d.getPret() == 5500.0);

    std::cout << "testDomain a trecut cu succes!\n";
}

void testRepo() {
    std::ofstream out("test_devices.txt");
    out << "telefon,Samsung Galaxy,2022,albastru,3700\n";
    out << "laptop,Dell XPS,2021,negru,6000\n";
    out.close();

    DeviceRepo repo("test_devices.txt");
    auto all = repo.getAll();
    assert(all.size() == 2);
    assert(all[0].getModel() == "Samsung Galaxy");
    assert(all[1].getPret() == 6000);

    std::cout << "testRepo a trecut cu succes!\n";
}

void testService() {
    std::ofstream out("test_devices.txt");
    out << "telefon,Samsung Galaxy,2022,rosu,3700\n";
    out << "laptop,Dell XPS,2021,negru,6000\n";
    out << "tableta,iPad,2023,galben,5000\n";
    out.close();

    DeviceRepo repo("test_devices.txt");
    DeviceService service(repo);

    auto all = service.getAll();
    assert(all.size() == 3);

    auto sortedByModel = service.sortByModel();
    assert(sortedByModel[0].getModel() == "Dell XPS");
    assert(sortedByModel[1].getModel() == "iPad");

    auto sortedByPrice = service.sortByPret();
    assert(sortedByPrice[0].getPret() == 3700);
    assert(sortedByPrice[2].getPret() == 6000);

    std::cout << "testService a trecut cu succes!\n";
}

void testAll() {
    std::cout << "Ruleaza testele...\n";
    testDomain();
    testRepo();
    testService();
    std::cout << "Toate testele au trecut cu succes!\n";
}
