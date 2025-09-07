#include "joc_model.h"

JocModel::JocModel(ServiceJoc &srv) : service(srv) {
    service.getAllSorted();
}

int JocModel::rowCount(const QModelIndex &parent) const {
    return static_cast<int>(jocuri.size());
}

int JocModel::columnCount(const QModelIndex &parent) const {
    return 5;
}

QVariant JocModel::data(const QModelIndex &index, int role) const {
    if (role == Qt::DisplayRole) {
        Joc j = jocuri[index.row()];
        switch (index.column()) {
            case 0: return QString::number(j.getId());
            case 1: return QString::number(j.getDim());
            case 2: return j.getTabla();
            case 3: {
                if (j.getJucator() == 'X')
                    return "X";
                else
                    return "O";
            }
            case 4: return j.getStare();
        }
    }
    return QVariant();
}

QVariant JocModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if (role == Qt::DisplayRole && orientation == Qt::Horizontal)
    {
        switch (section) {
            case 0: return QString("ID");
            case 1: return QString("Dimensiune");
            case 2: return QString("Tabla");
            case 3: return QString("Jucator Curent");
            case 4: return QString("Stare");
        }
    }
    return QVariant();
}

void JocModel::refresh() {
    emit layoutChanged();
}

void JocModel::setJocuri(const std::vector <Joc> &jocuri) {
    this->jocuri = jocuri;
    auto topLeft = createIndex(0, 0);
    auto bottomRight = createIndex(rowCount(), columnCount());
    emit dataChanged(topLeft, bottomRight);
}