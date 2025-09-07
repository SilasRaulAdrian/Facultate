#ifndef LAB_6_7_COSREADONLYGUI_H
#define LAB_6_7_COSREADONLYGUI_H

#include "../service/carte_service.h"
#include "../observer/observer.h"
#include <QWidget>
#include <QPainter>
#include <QMainWindow>

class CosReadOnlyGUI : public QMainWindow, public Observer {
    Q_OBJECT
private:
    CarteService &service;
    QMainWindow *mainWindow;
    QWidget *central;

public:
    /**
     * Constructorul clasei CosReadOnlyGUI
     * @param service Referinta la serviciul de carti
     * @param parent Parintele ferestrei principale
     */
    explicit CosReadOnlyGUI(CarteService &service, QMainWindow *parent = nullptr);

    /**
     * Destructorul clasei CosReadOnlyGUI
     */
    ~CosReadOnlyGUI();

    /**
     * Functie care updateaza UI-ul
     */
    void update() override;

protected:
    /**
     * Functie care deseneaza UI-ul
     * @param painter Painter-ul folosit pentru desenare
     */
    void paintEvent(QPaintEvent *event) override;

    /**
     * Functie care inchide fereastra
     * @param event Evenimentul de inchidere
     */
    void closeEvent(QCloseEvent *event) override;

signals:
    /**
     * Semnal care se emite atunci cand cosul este modificat
     */
    void cosModificat();
};

#endif //LAB_6_7_COSREADONLYGUI_H
