#include "tractor_model.h"
#include <QString>
#include <QBrush>

TractorModel::TractorModel(ServiceTractor& service, QObject* parent)
        : QAbstractTableModel(parent), service(service) {
    reload();
}

void TractorModel::reload() {
    beginResetModel();
    tractoare = service.getAllSorted();
    countByType = service.countByType();
    endResetModel();
}

int TractorModel::rowCount(const QModelIndex&) const {
    return static_cast<int>(tractoare.size());
}

int TractorModel::columnCount(const QModelIndex&) const {
    return 5;
}

QVariant TractorModel::data(const QModelIndex& index, int role) const {
    if (!index.isValid() || index.row() >= static_cast<int>(tractoare.size()))
        return {};

    const auto& t = tractoare[index.row()];

    if (role == Qt::DisplayRole) {
        switch (index.column()) {
            case 0: return t.getId();
            case 1: return t.getDenumire();
            case 2: return t.getTip();
            case 3: return t.getNumarRoti();
            case 4: return countByType.value(t.getTip());
        }
    }

    if (role == Qt::BackgroundRole && t.getTip() == highlightedTip)
        return QBrush(Qt::red);

    return {};
}

QVariant TractorModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if (role != Qt::DisplayRole || orientation != Qt::Horizontal) return {};

    switch (section) {
        case 0: return "ID";
        case 1: return "Denumire";
        case 2: return "Tip";
        case 3: return "Nr Roti";
        case 4: return "Nr Tipuri";
    }
    return {};
}

Tractor TractorModel::getTractorAt(int row) const {
    return tractoare[row];
}

void TractorModel::setHighlightedTip(const QString& tip) {
    highlightedTip = tip;
    emit dataChanged(index(0, 0), index(rowCount() - 1, columnCount() - 1));
}

