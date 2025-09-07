#include "wheels_widget.h"
#include <QPainter>
#include <QMouseEvent>

WheelsWidget::WheelsWidget(QWidget* parent) : QWidget(parent) {}

void WheelsWidget::setRoti(int r, int id) {
    roti = r;
    tractorId = id;
    update();
}

void WheelsWidget::paintEvent(QPaintEvent*) {
    QPainter p(this);
    p.setBrush(Qt::black);
    int cx = 10;
    int cy = height() / 2 - 10;
    for (int i = 0; i < roti; ++i) {
        p.drawEllipse(cx, cy, 20, 20);
        cx += 30;
    }
}

void WheelsWidget::mousePressEvent(QMouseEvent* event) {
    int cx = 10;
    int cy = height() / 2 - 10;
    for (int i = 0; i < roti; ++i) {
        QRect r(cx, cy, 20, 20);
        if (r.contains(event->pos())) {
            emit clickedOnWheel(tractorId);
            break;
        }
        cx += 30;
    }
}