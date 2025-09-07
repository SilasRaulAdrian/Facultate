#ifndef TRACTOARE_WHEELS_WIDGET_H
#define TRACTOARE_WHEELS_WIDGET_H

#include <QWidget>

class WheelsWidget : public QWidget {
    Q_OBJECT
private:
    int roti = 0;
    int tractorId = -1;

public:
    /**
     * Constructorul clasei
     * @param parent QWidget
     */
    WheelsWidget(QWidget* parent = nullptr);

    /**
     * Seteaza nr de roti si id-ul tractorului
     * @param r int, numarul de roti
     * @param id int, id-ul tractorului
     */
    void setRoti(int r, int id);

signals:
    /**
     * Semnal emis cand se da click pe o roata
     * @param id int, id-ul tractorului
     */
    void clickedOnWheel(int id);

protected:
    /**
     * Suprascrierea metodei paintEvent
     * @param event QPaintEvent
     */
    void paintEvent(QPaintEvent*) override;

    /**
     * Suprascrierea metodei mousePressEvent
     * @param event QMouseEvent
     */
    void mousePressEvent(QMouseEvent* event) override;
};

#endif //TRACTOARE_WHEELS_WIDGET_H
