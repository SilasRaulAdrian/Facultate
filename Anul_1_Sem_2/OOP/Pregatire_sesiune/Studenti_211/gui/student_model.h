//
// Created by Raul on 24.06.2025.
//

#ifndef STUDENTI_211_STUDENT_MODEL_H
#define STUDENTI_211_STUDENT_MODEL_H

#include <QAbstractTableModel>
#include <QBrush>
#include "../service/student_service.h"

class StudentModel : public QAbstractTableModel {
    Q_OBJECT
private:
    ServiceStudent& service;
    std::vector<Student> studenti;

    void sortByVarsta();

public:
    StudentModel(ServiceStudent& srv, QObject* parent = nullptr);
    int rowCount(const QModelIndex& parent = QModelIndex()) const override;
    int columnCount(const QModelIndex& parent = QModelIndex()) const override;
    QVariant data(const QModelIndex& index, int role) const override;
    QVariant headerData(int section, Qt::Orientation orientation, int role) const override;
    void actualizeazaModel();
    Student getStudent(int row) const;
};

#endif //STUDENTI_211_STUDENT_MODEL_H
