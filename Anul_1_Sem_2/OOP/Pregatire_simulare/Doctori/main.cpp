#include <QApplication>
#include <QPushButton>
#include "ui/main_window.h"
#include "repository/doctor_repo.h"
#include "service/doctor_service.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    DoctorRepo repo("doctori.txt");
    DoctorService service(repo);
    MainWindow w(service);
    w.show();

    return a.exec();
}
