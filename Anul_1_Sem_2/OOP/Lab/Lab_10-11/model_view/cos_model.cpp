#include "cos_model.h"

int CosModel::rowCount(const QModelIndex&) const {
    return service.getAllCos().size();
}

QVariant CosModel::data(const QModelIndex &index, int role) const {
    if (role == Qt::DisplayRole) {
        const auto& c = service.getAllCos().at(index.row());
        return QString("[%1] Titlu: %2, Autor: %3, Gen: %4, An aparitie: %5;")
                .arg(c.getId())
                .arg(QString::fromStdString(c.getTitlu()))
                .arg(QString::fromStdString(c.getAutor()))
                .arg(QString::fromStdString(c.getGen()))
                .arg(c.getAnAparitie());
    }
    return QVariant();
}

void CosModel::update() {
    beginResetModel();
    endResetModel();
}