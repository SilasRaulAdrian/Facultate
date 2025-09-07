#include <QApplication>
#include "repository/task_repo.h"
#include "service/task_service.h"
#include "gui/main_window.h"
#include "validator/validator.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    RepoTask repo("taskuri.txt");
    Validator val;
    ServiceTask service(repo, val);
    MainWindow w(service);
    w.show();

    return a.exec();
}
