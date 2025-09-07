#ifndef PRODUSE_PRODUS_H
#define PRODUSE_PRODUS_H

#include <QString>

class Produs {
private:
    int id;
    QString nume;
    QString tip;
    double pret;

public:
    /**
     * Constructorul clasei
     * @param id int, id-ul produsului
     * @param nume QString, numele produsului
     * @param tip QString, tipul produsului
     * @param pret double, pretul produsului
     */
    Produs(int id, const QString& nume, const QString& tip, double pret);

    /**
     * Getter pt id
     * @return int, id-ul produsului
     */
    int getId() const;

    /**
     * Getter pt nume
     * @return QString, numele produsului
     */
    const QString getNume() const;

    /**
     * Getter pt tip
     * @return QString, tipul produsului
     */
    const QString getTip() const;

    /**
     * Getter pt pret
     * @return double, pretul produsului
     */
    double getPret() const;

    /**
     * Setter pt nume
     * @param newNume QString, noul nume al produsului
     */
    void setNume(const QString& newNume);

    /**
     * Setter pt tip
     * @param newTip QString, noul tip al produsului
     */
    void setTip(const QString& newTip);

    /**
     * Setter pt pret
     * @param newPret double, noul pret al produsului
     */
    void setPret(double newPret);
};

#endif //PRODUSE_PRODUS_H
