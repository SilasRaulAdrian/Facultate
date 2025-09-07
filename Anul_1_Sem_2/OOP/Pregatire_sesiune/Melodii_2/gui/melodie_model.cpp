#include "melodie_model.h"

MelodieModel::MelodieModel(ServiceMelodii &serv) : service(serv) {
    service.getAllSortate();
}

int MelodieModel::rowCount(const QModelIndex &parent) const {
    return (int)melodii.size();
}

int MelodieModel::columnCount(const QModelIndex &parent) const {
    return 6;
}

QVariant MelodieModel::data(const QModelIndex &index, int role) const {
    if (role == Qt::DisplayRole) {
        const auto& m = melodii[index.row()];
        switch (index.column()) {
            case 0: return m.getId();
            case 1: return m.getTitlu();
            case 2: return m.getArtist();
            case 3: return m.getGen();
            case 4: return service.countArtist(m.getArtist());
            case 5: return service.countGen(m.getGen());
        }
    }
    return {};
}

QVariant MelodieModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if (role == Qt::DisplayRole && orientation == Qt::Horizontal) {
        switch (section) {
            case 0: return "ID";
            case 1: return "Titlu";
            case 2: return "Artist";
            case 3: return "Gen";
            case 4: return "Nr. melodii acelasi artist";
            case 5: return "Nr. melodii acelasi gen";
        }
    }
    return QAbstractTableModel::headerData(section, orientation, role);
}

void MelodieModel::actualizeazaModel() {
    beginResetModel();
    melodii = service.getAllSortate();
    endResetModel();
}