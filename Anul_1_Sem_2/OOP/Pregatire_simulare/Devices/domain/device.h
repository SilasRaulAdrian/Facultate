//
// Created by Raul on 16.05.2025.
//

#ifndef DEVICES_DEVICE_H
#define DEVICES_DEVICE_H

#include <QString>

class Device {
private:
    QString tip;
    QString model;
    int an;
    QString culoare;
    double pret;

public:
    Device(const QString &tip, const QString &model, int an, const QString &culoare, double pret) :
        tip(tip), model(model), an(an), culoare(culoare), pret(pret) {}

    QString getTip() const;
    QString getModel() const;
    int getAn() const;
    QString getCuloare() const;
    double getPret() const;
};

#endif //DEVICES_DEVICE_H
