#include <QApplication>
#include "repository/apartament_repo.h"
#include "service/apartamente_service.h"
#include "ui/main_window.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    ApartamentRepo repo("apartamente.txt");
    ApartamentService service(repo);
    MainWindow w(service);
    w.show();

    return a.exec();
}
