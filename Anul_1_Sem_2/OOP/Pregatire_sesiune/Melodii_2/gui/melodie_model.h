#ifndef MELODII_2_MELODIE_MODEL_H
#define MELODII_2_MELODIE_MODEL_H

#include <QAbstractTableModel>
#include "../service/melodii_service.h"

class MelodieModel : public QAbstractTableModel {
    Q_OBJECT

private:
    ServiceMelodii& service;
    std::vector<Melodie> melodii;

public:
    /**
     * Constructorul clasei
     * @param serv ServiceMelodii
     */
    MelodieModel(ServiceMelodii& serv);

    /**
     * Suprascrierea metodei rowCount
     * @param parent QModelIndex
     * @return int, numarul de randuri
     */
    int rowCount(const QModelIndex& parent = QModelIndex()) const override;

    /**
     * Suprascrierea metodei columnCount
     * @param parent QModelIndex
     * @return int, numarul de coloane
     */
    int columnCount(const QModelIndex& parent = QModelIndex()) const override;

    /**
     * Suprascrierea metodei data
     * @param index QModelIndex
     * @param role int
     * @return QVariant
     */
    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

    /**
     * Suprascrierea metodei headerData
     * @param section int
     * @param orientation Qt::Orientation
     * @param role int
     * @return QVariant
     */
    QVariant headerData(int section, Qt::Orientation orientation, int role) const override;

    /**
     * Metoda care actualizeaza modelul
     */
    void actualizeazaModel();
};

#endif //MELODII_2_MELODIE_MODEL_H
