#ifndef TICTACTOE_JOC_H
#define TICTACTOE_JOC_H

#include <QString>
#include <QChar>

class Joc {
private:
    int id;
    int dim;
    QString tabla;
    QChar jucator;
    QString stare;

public:
    Joc(int id, int dim, const QString& tabla, const QChar& jucator, const QString& stare);

    int getId() const;
    int getDim() const;
    QString getTabla() const;
    QChar getJucator() const;
    QString getStare() const;
    void setId(int newId);
    void setDim(int newDim);
    void setTabla(const QString& newTabla);
    void setJucator(const QChar& newJucator);
    void setStare(const QString& newStare);
};

#endif //TICTACTOE_JOC_H
