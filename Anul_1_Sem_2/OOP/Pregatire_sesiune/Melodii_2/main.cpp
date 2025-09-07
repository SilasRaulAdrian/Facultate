#include <QApplication>
#include "repository/melodii_repo.h"
#include "service/melodii_service.h"
#include "gui/main_window.h"
#include "validator/validator.h"
#include "tests/tests.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    testAll();
    RepoMelodii repo("melodii.txt");
    ValidatorMelodie val;
    ServiceMelodii service(repo, val);
    MainWindow w(service);
    w.show();
    w.resize(800, 600);

    return a.exec();
}
