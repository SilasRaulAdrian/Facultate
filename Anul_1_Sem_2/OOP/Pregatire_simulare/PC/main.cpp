#include <QApplication>
#include "ui/main_window.h"
#include "repository/placa_de_baza_repo.h"
#include "repository/procesor_repo.h"
#include "service/pc_service.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    ProcesorRepo procesorRepo("procesoare.txt");
    PlacaDeBazaRepo placaDeBazaRepo("placi_de_baza.txt");
    PCService service(procesorRepo, placaDeBazaRepo);
    MainWindow w(service);
    w.show();

    return a.exec();
}
