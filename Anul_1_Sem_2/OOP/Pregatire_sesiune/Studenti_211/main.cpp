#include <QApplication>
#include "gui/main_window.h"
#include "repository/student_repo.h"
#include "service/student_service.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    RepoStudent repo("studenti.txt");
    ServiceStudent service(repo);
    MainWindow w(service);
    w.show();

    return a.exec();
}
