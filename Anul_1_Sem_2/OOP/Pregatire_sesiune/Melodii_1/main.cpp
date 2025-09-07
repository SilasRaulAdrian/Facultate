#include <QApplication>
#include "gui/main_window.h"
#include "repository/melodie_repo.h"
#include "service/melodie_service.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    RepoMelodii repo("melodii.txt");
    ServiceMelodii service(repo);
    MainWindow w(service);
    w.show();

    return a.exec();
}
