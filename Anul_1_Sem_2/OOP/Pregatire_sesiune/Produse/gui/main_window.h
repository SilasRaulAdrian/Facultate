#ifndef PRODUSE_MAIN_WINDOW_H
#define PRODUSE_MAIN_WINDOW_H

#include <QMainWindow>
#include <QTableView>
#include <QLineEdit>
#include <QPushButton>
#include <QSlider>
#include <QLabel>
#include <QHBoxLayout>
#include <QVBoxLayout>
#include <QMessageBox>
#include "../service/produse_service.h"
#include "produs_model.h"
#include "tip_window.h"

class MainWindow : public QMainWindow, public Observer {
    Q_OBJECT

private:
    ServiceProduse& service;

    ProdusModel* model;
    QTableView* tableView;

    QLineEdit* editId;
    QLineEdit* editNume;
    QLineEdit* editTip;
    QLineEdit* editPret;

    QPushButton* btnAdd;

    QSlider* sliderPret;
    QLabel* lblFilter;

    std::vector<TipWindow*> tipWindows;

    /**
     * Initializeaza GUI
     */
    void initUI();

    /**
     * Conecteaza semnalele la sloturi
     */
    void connectSignals();

public:
    /**
     * Constructorul clasei
     * @param srv ServiceProduse
     * @param parent QWidget
     */
    MainWindow(ServiceProduse& srv, QWidget* parent = nullptr);

    /**
     * Destructorul clasei
     */
    ~MainWindow();

    /**
     * Actualizeaza GUI-ul
     */
    void update() override;

private slots:
    /**
     * Slot pentru adaugarea unui produs
     */
    void onAddClicked();

    /**
     * Slot pentru actualizarea filtrului de pret
     * @param value int, valoarea slider-ului
     */
    void onSliderChanged(int value);
};


#endif //PRODUSE_MAIN_WINDOW_H
