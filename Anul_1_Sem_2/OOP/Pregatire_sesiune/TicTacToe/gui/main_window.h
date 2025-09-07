#ifndef TICTACTOE_MAIN_WINDOW_H
#define TICTACTOE_MAIN_WINDOW_H

#include <QWidget>
#include <QPushButton>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QFormLayout>
#include <QLineEdit>
#include <QMessageBox>
#include <QTableView>
#include "joc_model.h"
#include "../service/joc_service.h"

class MainWindow : public QWidget {
private:
    ServiceJoc& service;
    QTableView* tabel = new QTableView;
    JocModel* model;

    std::vector<QPushButton*> butoane;

    QVBoxLayout* mainLayout = new QVBoxLayout;
    QHBoxLayout* layoutSus = new QHBoxLayout;
    QVBoxLayout* layoutJos = new QVBoxLayout;

    QPushButton* butonAdaugare = new QPushButton{ "Adauga" };
    QPushButton* butonModificare = new QPushButton{ "Modifica" };

    QLineEdit* txtDim = new QLineEdit;
    QLineEdit* txtTabla = new QLineEdit;
    QLineEdit* txtJucator = new QLineEdit;

    QLineEdit* txtID = new QLineEdit;
    QLineEdit* txtDimensiune = new QLineEdit;
    QLineEdit* txtTablaJoc = new QLineEdit;
    QLineEdit* txtJucatorCurent = new QLineEdit;
    QLineEdit* txtStare = new QLineEdit;

    QVBoxLayout* creareTabla(int dim, const QString& tabla);

    void initGUI();
    void initConnect();
    void loadData(const std::vector<Joc>& jocuri);

public:
    MainWindow(ServiceJoc& srv) : service(srv) {
        model = new JocModel(srv);
        tabel->setModel(model);
        initGUI();
        initConnect();
        loadData(srv.getAllSorted());
    }
};

#endif //TICTACTOE_MAIN_WINDOW_H
