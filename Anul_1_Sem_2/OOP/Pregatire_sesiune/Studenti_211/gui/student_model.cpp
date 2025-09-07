//
// Created by Raul on 24.06.2025.
//
#include "student_model.h"
#include <algorithm>

StudentModel::StudentModel(ServiceStudent &srv, QObject *parent) : QAbstractTableModel(parent), service(srv) {
    actualizeazaModel();
}

void StudentModel::sortByVarsta() {
    std::sort(studenti.begin(), studenti.end(), [](const Student &a, const Student &b){
        return a.getVarsta() < b.getVarsta();
    });
}

int StudentModel::rowCount(const QModelIndex &parent) const {
    return static_cast<int>(studenti.size());
}

int StudentModel::columnCount(const QModelIndex &parent) const {
    return 4;
}

QVariant StudentModel::data(const QModelIndex &index, int role) const {
    if (!index.isValid() || index.row() >= studenti.size()) return {};

    const Student& s = studenti[index.row()];

    if (role == Qt::DisplayRole) {
        switch (index.column()) {
            case 0: return s.getNrMatricol();
            case 1: return s.getNume();
            case 2: return s.getVarsta();
            case 3: return s.getFacultate();
        }
    } else if (role == Qt::BackgroundRole) {
        QString f = s.getFacultate();
        if (f == "info") return QBrush(Qt::lightGray);
        if (f == "mate") return QBrush(Qt::cyan);
        if (f == "ai") return QBrush(Qt::yellow);
        if (f == "mate-info") return QBrush(Qt::green);
    }

    return {};
}

QVariant StudentModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if (orientation == Qt::Horizontal && role == Qt::DisplayRole) {
        switch (section) {
            case 0: return "Nr. Matricol";
            case 1: return "Nume";
            case 2: return "Varsta";
            case 3: return "Facultate";
        }
    }

    return {};
}

void StudentModel::actualizeazaModel() {
    beginResetModel();
    studenti = service.getAll();
    sortByVarsta();
    endResetModel();
}

Student StudentModel::getStudent(int row) const {
    if (row < 0 || row >= static_cast<int>(studenti.size()))
        throw std::runtime_error("Index invalid");

    return studenti[row];
}