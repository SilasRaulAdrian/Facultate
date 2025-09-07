#include <QApplication>
#include "repository/concert_repo.h"
#include "service/concert_service.h"
#include "ui/main_window.h"

int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    ConcertRepo repo("concerte.txt");
    ConcertService service(repo);
    MainWindow w(service);
    w.show();

    return a.exec();
}
