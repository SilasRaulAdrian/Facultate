#ifndef TRACTOARE_TRACTOR_MODEL_H
#define TRACTOARE_TRACTOR_MODEL_H

#include <QAbstractTableModel>
#include "../service/tractor_service.h"

class TractorModel : public QAbstractTableModel {
private:
    ServiceTractor& service;
    std::vector<Tractor> tractoare;
    QMap<QString, int> countByType;
    QString highlightedTip;

public:
    /**
     * Constructorul clasei
     * @param service ServiceTractor
     * @param parent QObject
     */
    TractorModel(ServiceTractor& service, QObject* parent = nullptr);

    /**
     * Actualizeaza modelul
     */
    void reload();

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
     * Functie care acceseaza un tractor la un rand specificat
     * @param row int, randul de accesat
     * @return Tractor, tractorul de la randul specificat
     */
    Tractor getTractorAt(int row) const;

    /**
     * Functie care seteaza tipul de tractor care va fi evidentiat
     * @param tip QString, tipul de tractor
     */
    void setHighlightedTip(const QString& tip);
};


#endif //TRACTOARE_TRACTOR_MODEL_H
