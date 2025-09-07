#ifndef MELODII_2_GEN_CIRCLE_WIDGET_H
#define MELODII_2_GEN_CIRCLE_WIDGET_H

#include "../service/melodii_service.h"
#include <QWidget>

class GenCircleWidget : public QWidget {
    Q_OBJECT

private:
    ServiceMelodii& service;

public:
    /**
     * Constructorul clasei
     * @param srv ServiceMelodii
     * @param parent QWidget
     */
    GenCircleWidget(ServiceMelodii& srv, QWidget* parent = nullptr) : QWidget(parent), service(srv) {
        setMinimumHeight(200);
    }

protected:
    /**
     * Redefinirea metodei paintEvent pentru a desena cercurile
     * @param event QPaintEvent
     */
    void paintEvent(QPaintEvent* event) override;
};

#endif //MELODII_2_GEN_CIRCLE_WIDGET_H
