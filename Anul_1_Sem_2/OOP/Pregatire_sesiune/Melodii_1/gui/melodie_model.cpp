//
// Created by Raul on 23.06.2025.
//
#include "melodie_model.h"

MelodiiModel::MelodiiModel(ServiceMelodii &srv, QObject *parent) :
        QAbstractTableModel(parent), service(srv) {
    actualizeazaModel();
}

int MelodiiModel::rowCount(const QModelIndex &parent) const {
    return static_cast<int>(melodii.size());
}

int MelodiiModel::columnCount(const QModelIndex &parent) const {
    return 5;
}

QVariant MelodiiModel::data(const QModelIndex &index, int role) const {
    if (!index.isValid() || role != Qt::DisplayRole)
        return {};

    const auto& melodie = melodii.at(index.row());

    switch (index.column()) {
        case 0: return melodie.getId();
        case 1: return melodie.getTitlu();
        case 2: return melodie.getArtist();
        case 3: return melodie.getRank();
        case 4: return service.countRank(melodie.getRank());
        default: return {};
    }
}

QVariant MelodiiModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if (role != Qt::DisplayRole || orientation != Qt::Horizontal)
        return {};

    switch (section) {
        case 0: return "ID";
        case 1: return "Titlu";
        case 2: return "Artist";
        case 3: return "Rank";
        case 4: return "Nr. cu acelasi rank";
        default: return {};
    }
}

void MelodiiModel::actualizeazaModel() {
    beginResetModel();
    melodii = service.getAllSortate();
    endResetModel();
}

Melodie MelodiiModel::getMelodie(const QModelIndex &index) const {
    if (!index.isValid() || index.row() >= static_cast<int>(melodii.size()))
        throw std::runtime_error("Index invalid!");
    return melodii.at(index.row());
}