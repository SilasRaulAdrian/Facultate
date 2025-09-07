#include <QApplication>
#include "repository/jucator_repo.h"
#include "service/jucator_service.h"
#include "ui/main_window.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    JucatorRepo repo("jucatori.txt");
    JucatorService service(repo);
    MainWindow w(service);
    w.show();

    return a.exec();
}
