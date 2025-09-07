#include "cos_window.h"
#include <QPushButton>
#include <QCloseEvent>
#include <QMessageBox>
#include <QBoxLayout>

CosWindow::CosWindow(CarteService& srv, QMainWindow *parent) : QMainWindow(parent), mainWindow(parent), service(srv) {
    setupUI();
}

void CosWindow::setupUI() {
    setWindowTitle("Cos");
    resize(1000, 600);

    central = new QWidget(this);
    this->setCentralWidget(central);

    QHBoxLayout *layout = new QHBoxLayout(central);
    stangaLayout = new QVBoxLayout;
    dreaptaLayout = new QVBoxLayout;

    backButton = new QPushButton("Inapoi la Biblioteca");
    stangaLayout->addWidget(backButton);

    // Adaugare carte in cos
    titluEdit = new QLineEdit;
    adaugaCarteButton = new QPushButton("Adauga carte in cos");
    stangaLayout->addWidget(new QLabel("Titlu:"));
    stangaLayout->addWidget(titluEdit);
    stangaLayout->addWidget(adaugaCarteButton);

    // Golire cos
    golesteCosButton = new QPushButton("Goleste cos");
    stangaLayout->addWidget(golesteCosButton);

    // Generare cos
    numarCartiEdit = new QLineEdit;
    genereazaCosButton = new QPushButton("Genereaza cos");
    stangaLayout->addWidget(new QLabel("Numar carti pentru generare:"));
    stangaLayout->addWidget(numarCartiEdit);
    stangaLayout->addWidget(genereazaCosButton);

    // Export CSV
    numeFisierEdit = new QLineEdit;
    exportCsvButton = new QPushButton("Export CSV");
    stangaLayout->addWidget(new QLabel("Nume fisier CSV:"));
    stangaLayout->addWidget(numeFisierEdit);
    stangaLayout->addWidget(exportCsvButton);

    // Export HTML
    numeFisierHtmlEdit = new QLineEdit;
    exportHtmlButton = new QPushButton("Export HTML");
    stangaLayout->addWidget(new QLabel("Nume fisier HTML:"));
    stangaLayout->addWidget(numeFisierHtmlEdit);
    stangaLayout->addWidget(exportHtmlButton);

    // Afisare numar carti
    afiseazaNumarCartiButton = new QPushButton("Afiseaza numar carti din cos");
    stangaLayout->addWidget(afiseazaNumarCartiButton);

    layout->addLayout(stangaLayout, 2);

    tabelaCartiCos = new QTableWidget;
    tabelaCartiCos->setColumnCount(5);
    tabelaCartiCos->setHorizontalHeaderLabels({"ID", "Titlu", "Autor", "Gen", "Anul aparitiei"});
    dreaptaLayout->addWidget(tabelaCartiCos);
    QFont font;
    font.setPointSizeF(12);
    tabelaCartiCos->setFont(font);
    layout->addLayout(dreaptaLayout, 6);

    central->setLayout(layout);

    connect(backButton, &QPushButton::clicked, this, &CosWindow::onBackButtonClicked);
    connect(adaugaCarteButton, &QPushButton::clicked, this, &CosWindow::adaugaCarteInCos);
    connect(golesteCosButton, &QPushButton::clicked, this, &CosWindow::golesteCos);
    connect(genereazaCosButton, &QPushButton::clicked, this, &CosWindow::genereazaCos);
    connect(exportCsvButton, &QPushButton::clicked, this, &CosWindow::exportCsv);
    connect(exportHtmlButton, &QPushButton::clicked, this, &CosWindow::exportHtml);
    connect(afiseazaNumarCartiButton, &QPushButton::clicked, this, &CosWindow::afiseazaNumarCarti);
}

void CosWindow::adaugaCarteInCos() {
    std::string titlu = titluEdit->text().toStdString();
    try {
        service.adaugaCarteDupaTitlu(titlu);
        QMessageBox::information(this, "Succes", "Carte adaugata in cos!");
        emit cosModificat();
        loadCos();
    } catch (const std::exception &e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
    titluEdit->clear();
}

void CosWindow::loadCos() {
    tabelaCartiCos->clear();
    tabelaCartiCos->setRowCount(0);
    for (const auto& c : service.getAllCos()) {
        tabelaCartiCos->setHorizontalHeaderLabels({"ID", "Titlu", "Autor", "Gen", "Anul aparitiei"});
        int row = tabelaCartiCos->rowCount();
        tabelaCartiCos->insertRow(row);
        tabelaCartiCos->setItem(row, 0, new QTableWidgetItem(QString::number(c.getId())));
        tabelaCartiCos->setItem(row, 1, new QTableWidgetItem(QString::fromStdString(c.getTitlu())));
        tabelaCartiCos->setItem(row, 2, new QTableWidgetItem(QString::fromStdString(c.getAutor())));
        tabelaCartiCos->setItem(row, 3, new QTableWidgetItem(QString::fromStdString(c.getGen())));
        tabelaCartiCos->setItem(row, 4, new QTableWidgetItem(QString::number(c.getAnAparitie())));
    }
}

void CosWindow::golesteCos() {
    service.golesteCos();
    emit cosModificat();
    loadCos();
    QMessageBox::information(this, "Succes", "Cos golit!");
}

void CosWindow::genereazaCos() {
    try {
        int numarCarti = numarCartiEdit->text().toInt();
        Lista<Carte> &carti = service.getCarti();
        service.genereazaCos(numarCarti, carti);
        emit cosModificat();
        QMessageBox::information(this, "Succes", "Cos generat!");
        loadCos();
    } catch (const std::exception &e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
    numarCartiEdit->clear();
}

void CosWindow::exportCsv() {
    std::string numeFisier = numeFisierEdit->text().toStdString();
    try {
        service.exportCsv(numeFisier);
        QMessageBox::information(this, "Succes", "Cos exportat in CSV!");
    } catch (const std::exception &e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
    numarCartiEdit->clear();
}

void CosWindow::exportHtml() {
    std::string numeFisier = numeFisierHtmlEdit->text().toStdString();
    try {
        service.exportHtml(numeFisier);
        QMessageBox::information(this, "Succes", "Cos exportat in HTML!");
    } catch (const std::exception &e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
    numeFisierHtmlEdit->clear();
}

void CosWindow::afiseazaNumarCarti() {
    int numarCarti = service.getSizeCos();
    QMessageBox::information(this, "Numar carti", QString("Numarul de carti din cos este: %1").arg(numarCarti));
}

void CosWindow::onBackButtonClicked() {
    if (mainWindow) {
        mainWindow->show();
    }
    this->close();
}

void CosWindow::closeEvent(QCloseEvent *event) {
    if (mainWindow) {
        mainWindow->show();
    }
    event->accept();
}