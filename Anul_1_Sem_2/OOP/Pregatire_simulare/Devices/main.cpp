#include <QApplication>
#include "ui/main_window.h"
#include "repository/device_repo.h"
#include "service/device_service.h"
#include "teste/teste.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    testAll();
    DeviceRepo repo("devices.txt");
    DeviceService service(repo);
    MainWindow w(service);
    w.show();

    return a.exec();
}
