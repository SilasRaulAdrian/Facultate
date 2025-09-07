#include "gen_circle_widget.h"
#include <QPainter>

void GenCircleWidget::paintEvent(QPaintEvent *) {
    QPainter p(this);

    const std::vector<QString> genuri = {"pop", "rock", "folk", "disco"};
    const std::vector<QPoint> colturi = {
            QPoint(0, 0),
            QPoint(width(), 0),
            QPoint(0, height()),
            QPoint(width(), height())
    };

    for (int i = 0; i < 4; ++i) {
        int nr = service.countGen(genuri[i]);
        QPoint centru = colturi[i];
        for (int r = 10, k = 0; k < nr; ++k, r += 10) {
            p.drawEllipse(centru, r, r);
        }
    }
}