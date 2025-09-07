#include <QApplication>
#include "gui/main_window.h"
#include "repository/joc_repo.h"
#include "service/joc_service.h"
#include "validator/validator.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    RepoJoc repo("jocuri.txt");
    Validator val;
    ServiceJoc service(repo, val);
    MainWindow w(service);
    w.show();
    return a.exec();
}
