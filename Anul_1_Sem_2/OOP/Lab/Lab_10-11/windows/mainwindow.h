#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QTableWidget>
#include <QLineEdit>
#include <QPushButton>
#include <QListWidget>
#include <QComboBox>
#include <QLabel>
#include <QMap>
#include "../service/carte_service.h"
#include "cos_window.h"
#include "cosCRUDGUI.h"
#include "cosReadOnlyGUI.h"

class MainWindow : public QMainWindow {
    Q_OBJECT

private:
    CarteService& service;
    QWidget *centralWidget;
    QPushButton *exitButton;
    CosWindow *cosWindow = nullptr;
    CosCRUDGUI *cosCRUDGUI = nullptr;
    CosReadOnlyGUI *cosReadOnlyGUI = nullptr;

    QPushButton *cartiPeGenButton;
    QVBoxLayout *secondLayout;
    QMap<QString, QPushButton*> butoaneGen;

    // Pentru adaugare
    QLineEdit *idEdit, *titluEdit, *autorEdit, *genEdit, *anEdit;
    QPushButton *addButton;

    // Pentru stergere
    QLineEdit *idStergereEdit;
    QPushButton *deleteButton;

    // Pentru modificare
    QLineEdit *idModificareEdit, *titluModificareEdit, *autorModificareEdit, *genModificareEdit, *anModificareEdit;
    QPushButton *updateButton;

    // Lista
    QListWidget *listaCarti;

    // Pentru filtrari
    QLineEdit *filtruTitluEdit, *filtruAnEdit;
    QPushButton *filtruTitluButton, *filtruAnButton;

    // Pentru sortari
    QComboBox *sortareComboBox, *sortareModComboBox;
    QPushButton *sortareButton;

    // Pentru afisare
    QPushButton *afisareCartiButton;

    // Pentru undo
    QPushButton *undoButton;

    /**
     * Functie care face setup-ul UI-ului
     */
    void setupUI();

    /**
     * Functie care incarca cartile in lista
     */
    void loadCarti();

private slots:
            /**
             * Functie care adauga o carte
             */
            void adaugaCarte();

            /**
             * Functie care sterge o carte
             */
            void stergeCarte();

            /**
             * Functie care modifica o carte
             */
            void modificaCarte();

            /**
             * Functie care filtreaza cartile dupa titlu
             */
            void filtreazaDupaTitlu();

            /**
             * Functie care filtreaza cartile dupa an
             */
            void filtreazaDupaAn();

            /**
             * Functie care sorteaza cartile
             */
            void sorteazaCarti();

            /**
             * Functie care face undo
             */
            void undo();

            /**
             * Functie care confirma iesirea din aplicatie
             */
            void confirmExit();

            /**
             * Functie care afiseaza cartile pe gen
             */
            void afiseazaCartiPeGen();

            /**
             * Functie care adauga/sterge butoanele cu genurile
             */
            void updateButoaneGen();

public:
    /**
     * Constructorul clasei MainWindow
     * @param srv Referinta la serviciul de carti
     * @param parent Parintele ferestrei principale
     */
    MainWindow(CarteService& srv, QWidget *parent = nullptr);

    /**
     * Destructorul clasei MainWindow
     */
    ~MainWindow();
};

#endif // MAINWINDOW_H
