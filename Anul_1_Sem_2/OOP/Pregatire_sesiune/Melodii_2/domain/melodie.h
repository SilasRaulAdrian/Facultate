#ifndef MELODII_2_MELODIE_H
#define MELODII_2_MELODIE_H

#include <QString>

class Melodie {
private:
    int id;
    QString titlu;
    QString artist;
    QString gen;

public:
    /**
     * Constructorul clasei
     * @param id int, id-ul melodiei
     * @param titlu QString, titlul melodiei
     * @param artist QString, artistul melodiei
     * @param gen QString, genul melodiei
     */
    Melodie(int id, const QString& titlu, const QString& artist, const QString& gen);

    /**
     * Getter pt id
     * @return int, id-ul melodiei
     */
    int getId() const;

    /**
     * Getter pt titlu
     * @return QString, titlul melodiei
     */
    const QString& getTitlu() const;

    /**
     * Getter pt artist
     * @return QString, artistul melodiei
     */
    const QString& getArtist() const;

    /**
     * Getter pt gen
     * @return QString, genul melodiei
     */
    const QString& getGen() const;

    /**
     * Setter pt titlu
     * @param newTitlu QString, noul titlu al melodiei
     */
    void setTitlu(const QString& newTitlu);

    /**
     * Setter pt gen
     * @param newGen QString, noul gen al melodiei
     */
    void setGen(const QString& newGen);
};

#endif //MELODII_2_MELODIE_H
