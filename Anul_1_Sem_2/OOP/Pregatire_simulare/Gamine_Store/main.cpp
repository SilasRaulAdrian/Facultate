#include <QApplication>
#include "repository/joc_repo.h"
#include "service/joc_service.h"
#include "ui/main_window.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    JocRepo repo("jocuri.txt");
    JocService service(repo);
    MainWindow w(service);
    w.show();

    return a.exec();
}
