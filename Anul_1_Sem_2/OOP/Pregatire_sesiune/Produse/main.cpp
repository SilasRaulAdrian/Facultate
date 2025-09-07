#include <QApplication>
#include "repository/produse_repo.h"
#include "service/produse_service.h"
#include "validator/validator.h"
#include "gui/main_window.h"
#include "tests/tests.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    testAll();
    RepoProduse repo("produse.txt");
    ValidatorProduse validator;
    ServiceProduse service(repo, validator);
    MainWindow w(service);
    w.show();
    w.resize(800, 600);

    return a.exec();
}
