#ifndef PRODUSE_PRODUS_MODEL_H
#define PRODUSE_PRODUS_MODEL_H

#include <QAbstractTableModel>
#include "../domain/produs.h"
#include "../service/produse_service.h"

class ProdusModel : public QAbstractTableModel, public Observer {
private:
    ServiceProduse& service;
    std::vector<Produs> produse;
    int pretFiltru = 100;

public:
    /**
     * Constructorul clasei
     * @param srv ServiceProduse
     */
    ProdusModel(ServiceProduse& srv);

    /**
     * Suprascrierea metodei rowCount
     * @param parent QModelIndex
     * @return int, numarul de randuri
     */
    int rowCount(const QModelIndex &parent = QModelIndex()) const override;

    /**
     * Suprascrierea metodei columnCount
     * @param parent QModelIndex
     * @return int, numarul de coloane
     */
    int columnCount(const QModelIndex &parent = QModelIndex()) const override;

    /**
     * Suprascrierea metodei data
     * @param index QModelIndex
     * @param role int
     * @return QVariant
     */
    QVariant data(const QModelIndex &index, int role) const override;

    /**
     * Suprascrierea metodei setData
     * @param index QModelIndex
     * @param value QVariant
     * @param role int
     * @return QVariant
     */
    QVariant headerData(int section, Qt::Orientation orientation, int role) const override;

    /**
     * Actualizeaza modelul
     */
    void update() override;
};

#endif //PRODUSE_PRODUS_MODEL_H
