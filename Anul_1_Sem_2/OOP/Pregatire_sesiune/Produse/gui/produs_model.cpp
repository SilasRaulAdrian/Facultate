#include "produs_model.h"
#include <QBrush>
#include <QColor>

ProdusModel::ProdusModel(ServiceProduse &srv) : service(srv) {
    produse = service.getAllSortat();
    pretFiltru = service.getPretFiltru();
    service.addObserver(this);
}

int ProdusModel::rowCount(const QModelIndex &parent) const {
    return static_cast<int>(produse.size());
}

int ProdusModel::columnCount(const QModelIndex &parent) const {
    return 5;
}

QVariant ProdusModel::data(const QModelIndex &index, int role) const {
    if (!index.isValid())
        return QVariant();

    const Produs& p = produse[index.row()];

    if (role == Qt::DisplayRole) {
        switch (index.column()) {
            case 0: return p.getId();
            case 1: return p.getNume();
            case 2: return p.getTip();
            case 3: return p.getPret();
            case 4: {
                int cnt = 0;
                QString nume = p.getNume().toLower();
                for (QChar c : nume)
                    if (QString("aeiou").contains(c))
                        cnt++;
                return cnt;
            }
        }
    } else if (role == Qt::BackgroundRole) {
        if (p.getPret() <= pretFiltru)
            return QBrush(Qt::red);
    }

    return QVariant();
}

QVariant ProdusModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if (orientation == Qt::Horizontal && role == Qt::DisplayRole) {
        switch (section) {
            case 0: return "ID";
            case 1: return "Nume";
            case 2: return "Tip";
            case 3: return "Pret";
            case 4: return "Nr. vocale";
        }
    }
    return QVariant();
}

void ProdusModel::update() {
    produse = service.getAllSortat();
    pretFiltru = service.getPretFiltru();
    beginResetModel();
    endResetModel();
}