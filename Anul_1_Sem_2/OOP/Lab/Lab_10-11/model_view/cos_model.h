#ifndef LAB_6_7_COS_MODEL_H
#define LAB_6_7_COS_MODEL_H

#include <QAbstractListModel>
#include "../service/carte_service.h"

class CosModel : public QAbstractListModel {
    Q_OBJECT
private:
    CarteService &service;

public:
    /**
     * Constructorul clasei CosTableModel
     * @param service Referinta la serviciul de carti
     */
    CosModel(CarteService &service) : service(service) {}

    /**
     * Functie care returneaza numarul de randuri
     * @param parent Indexul parintei (neutilizat)
     * @return Numarul de randuri
     */
    int rowCount(const QModelIndex &parent = QModelIndex()) const override;

    /**
     * Functie care returneaza datele pentru un index specificat
     * @param index Indexul pentru care se cer datele
     * @param role Rolul pentru care se cer datele
     * @return Datele pentru indexul specificat si rolul dat
     */
    QVariant data(const QModelIndex &index, int role) const override;

    /**
     * Functie care actualizeaza modelul
     */
    void update();
};

#endif //LAB_6_7_COS_MODEL_H
