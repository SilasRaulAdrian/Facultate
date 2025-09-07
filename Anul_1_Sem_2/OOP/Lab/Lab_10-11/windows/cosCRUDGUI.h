#ifndef LAB_6_7_COSCRUDGUI_H
#define LAB_6_7_COSCRUDGUI_H

#include <QWidget>
#include <QPushButton>
#include <QVBoxLayout>
#include <QMainWindow>
#include <QListView>
#include "../service/carte_service.h"
#include "../model_view/cos_model.h"
#include "../observer/observer.h"

class CosCRUDGUI : public QMainWindow, public Observer {
    Q_OBJECT
private:
    CarteService &service;
    CosModel *model;
    QListView *lista;
    QMainWindow *mainWindow;
    QWidget *central;

public:
    /**
     * Constructorul clasei CosCRUDGUI
     * @param service Referinta la serviciul de carti
     * @param parent Parintele ferestrei principale
     */
    explicit CosCRUDGUI(CarteService &service, QMainWindow *parent = nullptr);

    /**
     * Destructorul clasei CosCRUDGUI
     */
    ~CosCRUDGUI();

    /**
     * Functie care updateaza UI-ul
     */
    void update() override;

    /**
     * Functie care face setup-ul UI-ului
     */
    void setUp();

signals:
    /**
     * Semnal care se emite atunci cand cosul este modificat
     */
    void cosModificat();

protected:
    /**
     * Functie care inchide fereastra
     * @param event Evenimentul de inchidere
     */
    void closeEvent(QCloseEvent *event) override;
};

#endif //LAB_6_7_COSCRUDGUI_H
