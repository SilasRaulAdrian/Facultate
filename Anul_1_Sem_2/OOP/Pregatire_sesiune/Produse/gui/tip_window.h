#ifndef PRODUSE_TIP_WINDOW_H
#define PRODUSE_TIP_WINDOW_H

#include <QWidget>
#include <QLabel>
#include <QVBoxLayout>
#include "../service/produse_service.h"

class TipWindow : public QWidget, public Observer {
    Q_OBJECT
private:
    QString tip;
    ServiceProduse& service;
    QLabel* label;

public:
    /**
     * Constructorul clasei
     * @param tip QString, tipul produsului
     * @param srv ServiceProduse
     * @param parent QWidget
     */
    TipWindow(const QString& tip, ServiceProduse& srv, QWidget* parent = nullptr);

    /**
     * Actualizeaza eticheta cu numarul de produse de acest tip
     */
    void update() override;
};

#endif //PRODUSE_TIP_WINDOW_H
