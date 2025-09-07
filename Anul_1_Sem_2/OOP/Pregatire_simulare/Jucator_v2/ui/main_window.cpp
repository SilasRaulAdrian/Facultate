#include "main_window.h"
#include <QVBoxLayout>
#include <QFormLayout>
#include <QMessageBox>
#include <QGroupBox>
#include <QHeaderView>
#include <QRandomGenerator>

MainWindow::MainWindow(JucatorService &service, QWidget *parent)
        : service(service), QMainWindow(parent) {
    setupUI();
    populateazaTabel(service.getAll());

    resize(800, 600);
}

void MainWindow::setupUI() {
    QWidget* central = new QWidget;
    QVBoxLayout* layoutPrincipal = new QVBoxLayout;

    tabelJucatori = new QTableWidget;
    tabelJucatori->setColumnCount(4);
    tabelJucatori->setHorizontalHeaderLabels({"Rank", "Nume", "Tara", "Puncte"});
    tabelJucatori->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
    layoutPrincipal->addWidget(tabelJucatori);

    QFormLayout* form = new QFormLayout;
    lineNume = new QLineEdit;
    lineTara = new QLineEdit;
    form->addRow("Nume jucator:", lineNume);
    form->addRow("Tara de origine:", lineTara);
    layoutPrincipal->addLayout(form);

    sliderPuncte = new QSlider(Qt::Horizontal);
    sliderPuncte->setRange(1, 100);
    sliderPuncte->setValue(50);
    labelPuncte = new QLabel("Puncte: 50");
    layoutPrincipal->addWidget(labelPuncte);
    layoutPrincipal->addWidget(sliderPuncte);

    btnAdauga = new QPushButton("Adauga jucator");
    layoutPrincipal->addWidget(btnAdauga);

    QGroupBox* groupBoxTari = new QGroupBox("Filtrare dupa tara");
    layoutTari = new QVBoxLayout;
    groupBoxTari->setLayout(layoutTari);
    layoutPrincipal->addWidget(groupBoxTari);

    btnGenereazaMeci = new QPushButton("Genereaza meci");
    layoutPrincipal->addWidget(btnGenereazaMeci);

    connect(btnAdauga, &QPushButton::clicked, this, &MainWindow::onAdaugaJucator);
    connect(btnGenereazaMeci, &QPushButton::clicked, this, &MainWindow::onGenereazaMeci);
    connect(sliderPuncte, &QSlider::valueChanged, this, &MainWindow::onPuncteChanged);

    setupRadioButtons();

    central->setLayout(layoutPrincipal);
    setCentralWidget(central);
}

void MainWindow::populateazaTabel(const std::vector<Jucator> &jucatori) {
    tabelJucatori->setRowCount(jucatori.size());

    for (int i = 0; i < jucatori.size(); ++i) {
        const auto& j = jucatori[i];

        QTableWidgetItem* itemRank = new QTableWidgetItem(QString::number(j.getRanking()));
        QTableWidgetItem* itemNume = new QTableWidgetItem(j.getNume());
        QTableWidgetItem* itemTara = new QTableWidgetItem(j.getTara());
        QTableWidgetItem* itemPuncte = new QTableWidgetItem(QString::number(j.getPuncte()));

        tabelJucatori->setItem(i, 0, itemRank);
        tabelJucatori->setItem(i, 1, itemNume);
        tabelJucatori->setItem(i, 2, itemTara);
        tabelJucatori->setItem(i, 3, itemPuncte);
    }
}

void MainWindow::setupRadioButtons() {
    // Clear existing buttons
    QLayoutItem* child;
    while ((child = layoutTari->takeAt(0))) {
        delete child->widget();
        delete child;
    }

    groupTari = new QButtonGroup(this);

    QRadioButton* radioToate = new QRadioButton("Toate tarile");
    radioToate->setChecked(true);
    groupTari->addButton(radioToate);
    layoutTari->addWidget(radioToate);
    connect(radioToate, &QRadioButton::toggled, this, &MainWindow::onFiltruTaraToggled);

    // Add buttons for each country
    for (const auto& tara : service.getTarileDisponibile()) {
        QRadioButton* radioTara = new QRadioButton(tara);
        groupTari->addButton(radioTara);
        layoutTari->addWidget(radioTara);
        connect(radioTara, &QRadioButton::toggled, this, &MainWindow::onFiltruTaraToggled);
    }
}

void MainWindow::onAdaugaJucator() {
    QString nume = lineNume->text().trimmed();
    QString tara = lineTara->text().trimmed();
    int puncte = sliderPuncte->value();

    if (nume.isEmpty() || tara.isEmpty()) {
        QMessageBox::warning(this, "Eroare", "Numele si tara nu pot fi goale!");
        return;
    }

    try {
        service.adaugaJucator(nume, tara, puncte);
        populateazaTabel(service.getAll());
        setupRadioButtons();

        lineNume->clear();
        lineTara->clear();
        sliderPuncte->setValue(50);
    } catch (std::exception &e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
}

void MainWindow::onGenereazaMeci() {
    try {
        auto [jucator1, jucator2] = service.genereazaMeci();

        QVector<QString> scoruriPosibile = {"6-1", "6-2", "6-3", "6-4", "7-5"};
        QString scor1 = scoruriPosibile[QRandomGenerator::global()->bounded(scoruriPosibile.size())];
        QString scor2 = scoruriPosibile[QRandomGenerator::global()->bounded(scoruriPosibile.size())];

        QString mesaj = QString("[%1] %2 vs. [%3] %4\n\nScor: %5 %6")
                .arg(jucator1.getRanking())
                .arg(jucator1.getNume())
                .arg(jucator2.getRanking())
                .arg(jucator2.getNume())
                .arg(scor1)
                .arg(scor2);

        QMessageBox::information(this, "Meci generat", mesaj);
    } catch (std::exception &e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
}

void MainWindow::onPuncteChanged(int value) {
    labelPuncte->setText(QString("Puncte: %1").arg(value));
}

void MainWindow::onFiltruTaraToggled(bool checked) {
    QRadioButton *checkedButton = qobject_cast<QRadioButton*>(sender());;
    if (!checkedButton || !checked) return;

    QString tara = checkedButton->text();

    if (tara == "Toate tarile")
        populateazaTabel(service.getAll());
    else
        populateazaTabel(service.getJucatoriFiltrati(tara));
}