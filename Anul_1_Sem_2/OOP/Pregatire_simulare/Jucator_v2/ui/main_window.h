#ifndef JUCATOR_V1_MAIN_WINDOW_H
#define JUCATOR_V1_MAIN_WINDOW_H

#include "../service/jucator_service.h"
#include <QMainWindow>
#include <QTableWidget>
#include <QLineEdit>
#include <QPushButton>
#include <QRadioButton>
#include <QSlider>
#include <QLabel>
#include <QButtonGroup>
#include <QVBoxLayout>

class MainWindow : public QMainWindow {
Q_OBJECT
private:
    JucatorService &service;

    QTableWidget* tabelJucatori;
    QLineEdit* lineNume;
    QLineEdit* lineTara;

    QButtonGroup* groupTari;
    QVBoxLayout* layoutTari;

    QPushButton* btnAdauga;
    QPushButton* btnGenereazaMeci;

    QSlider* sliderPuncte;
    QLabel* labelPuncte;

    void setupUI();
    void populateazaTabel(const std::vector<Jucator>& jucatori);
    void setupRadioButtons();

private slots:
    void onAdaugaJucator();
    void onGenereazaMeci();
    void onPuncteChanged(int value);
    void onFiltruTaraToggled(bool checked);

public:
    MainWindow(JucatorService& service, QWidget* parent = nullptr);
};

#endif //JUCATOR_V1_MAIN_WINDOW_H