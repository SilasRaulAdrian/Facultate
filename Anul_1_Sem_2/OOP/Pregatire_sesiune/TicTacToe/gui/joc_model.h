#ifndef TICTACTOE_JOC_MODEL_H
#define TICTACTOE_JOC_MODEL_H

#include "../domain/joc.h"
#include "../service/joc_service.h"
#include <QAbstractTableModel>
#include <vector>

class JocModel : public QAbstractTableModel {
private:
    ServiceJoc& service;
    std::vector<Joc> jocuri;

public:
    JocModel(ServiceJoc& srv);
    int rowCount(const QModelIndex &parent = QModelIndex()) const override;
    int columnCount(const QModelIndex &parent = QModelIndex()) const override;
    QVariant data(const QModelIndex &index, int role) const override;
    QVariant headerData(int section, Qt::Orientation orientation, int role) const override;
    void refresh();
    void setJocuri(const std::vector<Joc>& jocuri);
};

#endif //TICTACTOE_JOC_MODEL_H
