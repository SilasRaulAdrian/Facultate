#ifndef TRACTOARE_TRACTOR_H
#define TRACTOARE_TRACTOR_H

#include <QString>

class Tractor {
private:
    int id;
    QString denumire;
    QString tip;
    int numarRoti;

public:
    /**
     * Constructorul clasei
     * @param id int, id-ul tractorului
     * @param denumire QString, denumirea tractorului
     * @param tip QString, tipul tractorului
     * @param numarRoti int, nr de roti al tractorului
     */
    Tractor(int id, const QString& denumire, const QString& tip, int numarRoti);

    /**
     * Getter pt id
     * @return int, id-ul tractorului
     */
    int getId() const;

    /**
     * Getter pt denumire
     * @return QString, denumirea tractorului
     */
    QString getDenumire() const;

    /**
     * Getter pt tip
     * @return QString, tipul tractorului
     */
    QString getTip() const;

    /**
     * Getter pt numarRoti
     * @return int, nr de roti al tractorului
     */
    int getNumarRoti() const;

    /**
     * Setter pt numarRoti
     * @param nr int, noul nr de roti al tractorului
     */
    void setNumarRoti(int nr);
};

#endif //TRACTOARE_TRACTOR_H
