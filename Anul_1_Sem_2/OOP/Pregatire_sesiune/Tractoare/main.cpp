#include <QApplication>
#include "gui/main_window.h"
#include "repository/tractor_repo.h"
#include "service/tractor_service.h"
#include "tests/tests.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    testAll();
    RepoTractor repo("tractoare.txt");
    ServiceTractor service(repo);

    MainWindow window(service);
    window.resize(800, 600);
    window.show();

    return a.exec();
}
