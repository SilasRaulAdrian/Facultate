#ifndef LAB_6_7_COS_WINDOW_H
#define LAB_6_7_COS_WINDOW_H

#include <QWidget>
#include <QVBoxLayout>
#include <QLabel>
#include <QPushButton>
#include <QMainWindow>
#include <QLineEdit>
#include <QListWidget>
#include <QTableWidget>
#include "../service/carte_service.h"

class CosWindow : public QMainWindow {
    Q_OBJECT
private:
    QWidget *central;
    QPushButton *backButton;
    QMainWindow *mainWindow;
    CarteService &service;
    QVBoxLayout *stangaLayout;
    QVBoxLayout *dreaptaLayout;
    QTableWidget* tabelaCartiCos;

    // Pentru adaugare carti in cos
    QLineEdit *titluEdit;
    QPushButton *adaugaCarteButton;

    // Pentru golirea cosului
    QPushButton *golesteCosButton;

    // Pentru generarea cosului
    QLineEdit *numarCartiEdit;
    QPushButton *genereazaCosButton;

    // Pentru export
    QPushButton *exportCsvButton;
    QPushButton *exportHtmlButton;
    QLineEdit *numeFisierEdit;
    QLineEdit *numeFisierHtmlEdit;

    // Pentru afisarea numarului de carti
    QPushButton *afiseazaNumarCartiButton;

    /**
     * Functie care face setup-ul UI-ului
     */
    void setupUI();

    /**
     * Functie care afiseaza toate cartile din cos
     */
    void loadCos();

signals:
    /**
     * Semnal care se emite atunci cand cosul este modificat
     */
    void cosModificat();

public:
    /**
     * Constructorul clasei CosWindow
     * @param parent Parintele ferestrei
     * @param srv Referinta la serviciul de carti
     */
    explicit CosWindow(CarteService &srv, QMainWindow *parent = nullptr);

protected:
    /**
     * Functie care inchide fereastra
     * @param event Evenimentul de inchidere
     */
    void closeEvent(QCloseEvent *event) override;

private slots:
    /**
     * Functie care se apeleaza la apasarea butonului de inapoi
     */
    void onBackButtonClicked();

    /**
     * Functie care adauga o carte in cos
     */
    void adaugaCarteInCos();

    /**
     * Functie care goleste cosul
     */
    void golesteCos();

    /**
     * Functie care genereaza cosul
     */
    void genereazaCos();

    /**
     * Functie care exporta cosul in CSV
     */
    void exportCsv();

    /**
     * Functie care exporta cosul in HTML
     */
    void exportHtml();

    /**
     * Functie care afiseaza numarul de carti din cos
     */
    void afiseazaNumarCarti();
};

#endif //LAB_6_7_COS_WINDOW_H
