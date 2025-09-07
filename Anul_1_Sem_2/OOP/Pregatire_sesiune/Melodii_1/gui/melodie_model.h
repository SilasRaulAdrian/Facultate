//
// Created by Raul on 23.06.2025.
//

#ifndef MELODII_1_MELODIE_MODEL_H
#define MELODII_1_MELODIE_MODEL_H

#include <QAbstractTableModel>
#include "../domain/melodie.h"
#include "../service/melodie_service.h"
#include <vector>

class MelodiiModel : public QAbstractTableModel {
    Q_OBJECT

private:
    ServiceMelodii& service;
    std::vector<Melodie> melodii;

public:
    explicit MelodiiModel(ServiceMelodii& srv, QObject* parent = nullptr);

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;
    int columnCount(const QModelIndex& parent = QModelIndex()) const override;

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;
    QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override;

    void actualizeazaModel();
    Melodie getMelodie(const QModelIndex& index) const;
};

#endif //MELODII_1_MELODIE_MODEL_H
