#include "mainwindow.h"
#include <QVBoxLayout>
#include <QLabel>
#include <QMessageBox>
#include <iostream>
#include <QWidget>
#include <QFont>

MainWindow::MainWindow(CarteService& srv, QWidget *parent)
        : QMainWindow(parent), service(srv) {
    setupUI();
    cosWindow = nullptr;
}

MainWindow::~MainWindow() {}

void MainWindow::setupUI() {
    centralWidget = new QWidget(this);
    setCentralWidget(centralWidget);
    setWindowTitle("Biblioteca");
    resize(1000, 600);

    auto* mainLayout = new QHBoxLayout;
    auto* stangaLayout = new QVBoxLayout;

    // Adauga
    stangaLayout->addWidget(new QLabel("ID:"));
    idEdit = new QLineEdit;
    stangaLayout->addWidget(idEdit);

    stangaLayout->addWidget(new QLabel("Titlu:"));
    titluEdit = new QLineEdit;
    stangaLayout->addWidget(titluEdit);

    stangaLayout->addWidget(new QLabel("Autor:"));
    autorEdit = new QLineEdit;
    stangaLayout->addWidget(autorEdit);

    stangaLayout->addWidget(new QLabel("Gen:"));
    genEdit = new QLineEdit;
    stangaLayout->addWidget(genEdit);

    stangaLayout->addWidget(new QLabel("An:"));
    anEdit = new QLineEdit;
    stangaLayout->addWidget(anEdit);

    addButton = new QPushButton("Adauga");
    stangaLayout->addWidget(addButton);

    // Sterge
    stangaLayout->addWidget(new QLabel("ID pentru stergere:"));
    idStergereEdit = new QLineEdit;
    stangaLayout->addWidget(idStergereEdit);

    deleteButton = new QPushButton("Sterge");
    stangaLayout->addWidget(deleteButton);

    // Modifica
    stangaLayout->addWidget(new QLabel("ID pentru modificare:"));
    idModificareEdit = new QLineEdit;
    stangaLayout->addWidget(idModificareEdit);

    stangaLayout->addWidget(new QLabel("Titlu nou:"));
    titluModificareEdit = new QLineEdit;
    stangaLayout->addWidget(titluModificareEdit);

    stangaLayout->addWidget(new QLabel("Autor nou:"));
    autorModificareEdit = new QLineEdit;
    stangaLayout->addWidget(autorModificareEdit);

    stangaLayout->addWidget(new QLabel("Gen nou:"));
    genModificareEdit = new QLineEdit;
    stangaLayout->addWidget(genModificareEdit);

    stangaLayout->addWidget(new QLabel("An nou:"));
    anModificareEdit = new QLineEdit;
    stangaLayout->addWidget(anModificareEdit);

    updateButton = new QPushButton("Modifica");
    stangaLayout->addWidget(updateButton);

    mainLayout->addLayout(stangaLayout, 2);

    // Filtrare
    secondLayout = new QVBoxLayout;
    filtruTitluEdit = new QLineEdit;
    filtruAnEdit = new QLineEdit;
    filtruTitluButton = new QPushButton("Filtreaza dupa titlu");
    filtruAnButton = new QPushButton("Filtreaza dupa an");

    secondLayout->addWidget(new QLabel("Titlu:"));
    secondLayout->addWidget(filtruTitluEdit);
    secondLayout->addWidget(filtruTitluButton);
    secondLayout->addWidget(new QLabel("An:"));
    secondLayout->addWidget(filtruAnEdit);
    secondLayout->addWidget(filtruAnButton);

    mainLayout->addLayout(secondLayout, 2);

    // Sortare
    sortareComboBox = new QComboBox;
    sortareComboBox->addItems({"Titlu", "Autor", "Gen", "An"});

    sortareModComboBox = new QComboBox;
    sortareModComboBox->addItems({"Crescator", "Descrescator"});

    sortareButton = new QPushButton("Sorteaza");

    secondLayout->addWidget(new QLabel("Sortare dupa:"));
    secondLayout->addWidget(sortareComboBox);
    secondLayout->addWidget(new QLabel("Mod:"));
    secondLayout->addWidget(sortareModComboBox);
    secondLayout->addWidget(sortareButton);

    // Undo
    undoButton = new QPushButton("Undo");
    secondLayout->addWidget(undoButton);

    // Afisare carti
    afisareCartiButton = new QPushButton("Afiseaza carti");
    secondLayout->addWidget(afisareCartiButton);

    // Lista carti
    listaCarti = new QListWidget;
    mainLayout->addWidget(listaCarti, 6);
    QFont font;
    font.setPointSizeF(12);
    listaCarti->setFont(font);

    // Carti pe gen
    cartiPeGenButton = new QPushButton("Afiseaza carti pe gen");
    secondLayout->addWidget(cartiPeGenButton);

    // Cos
    auto *cosButton = new QPushButton("Cos de cumparaturi");
    secondLayout->addWidget(cosButton);

    auto *btnCosCRUD = new QPushButton("Cos CRUD");
    secondLayout->addWidget(btnCosCRUD);

    auto *btnCosReadOnly = new QPushButton("Cos Read Only");
    secondLayout->addWidget(btnCosReadOnly);

    // Iesire
    exitButton = new QPushButton("Iesire");
    secondLayout->addWidget(exitButton);

    centralWidget->setLayout(mainLayout);

    // Conectari
    connect(addButton, &QPushButton::clicked, this, &MainWindow::adaugaCarte);
    connect(deleteButton, &QPushButton::clicked, this, &MainWindow::stergeCarte);
    connect(updateButton, &QPushButton::clicked, this, &MainWindow::modificaCarte);
    connect(filtruTitluButton, &QPushButton::clicked, this, &MainWindow::filtreazaDupaTitlu);
    connect(filtruAnButton, &QPushButton::clicked, this, &MainWindow::filtreazaDupaAn);
    connect(sortareButton, &QPushButton::clicked, this, &MainWindow::sorteazaCarti);
    connect(afisareCartiButton, &QPushButton::clicked, this, &MainWindow::loadCarti);
    connect(undoButton, &QPushButton::clicked, this, &MainWindow::undo);
    connect(exitButton, &QPushButton::clicked, this, &MainWindow::confirmExit);
    connect(cartiPeGenButton, &QPushButton::clicked, this, &MainWindow::afiseazaCartiPeGen);
    connect(cosButton, &QPushButton::clicked, this, [this]() {
        if (!cosWindow) {
            cosWindow = new CosWindow(service, this);
            QObject::connect(cosWindow, &CosWindow::cosModificat, this, &MainWindow::updateButoaneGen);
            QObject::connect(cosWindow, &CosWindow::cosModificat, this, &MainWindow::loadCarti);
        }
        this->hide();
        cosWindow->show();
    });
    connect(btnCosCRUD, &QPushButton::clicked, this, [this]() {
        cosCRUDGUI = new CosCRUDGUI(service, this);
        QObject::connect(cosCRUDGUI, &CosCRUDGUI::cosModificat, this, &MainWindow::updateButoaneGen);
        QObject::connect(cosCRUDGUI, &CosCRUDGUI::cosModificat, this, &MainWindow::loadCarti);
        cosCRUDGUI->show();
    });
    connect(btnCosReadOnly, &QPushButton::clicked, this, [this]() {
        cosReadOnlyGUI = new CosReadOnlyGUI(service, this);
        QObject::connect(cosReadOnlyGUI, &CosReadOnlyGUI::cosModificat, this, &MainWindow::updateButoaneGen);
        QObject::connect(cosReadOnlyGUI, &CosReadOnlyGUI::cosModificat, this, &MainWindow::loadCarti);
        cosReadOnlyGUI->show();
    });

    loadCarti();
    updateButoaneGen();
}


void MainWindow::adaugaCarte() {
    try {
        int id = idEdit->text().toInt();
        std::string titlu = titluEdit->text().toStdString();
        std::string autor = autorEdit->text().toStdString();
        std::string gen = genEdit->text().toStdString();
        int an = anEdit->text().toInt();

        service.adaugaCarte(id, titlu, autor, gen, an);
        QMessageBox::information(this, "Succes", "Carte adaugata!");
        loadCarti();
        updateButoaneGen();
    } catch (const std::exception& e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
    idEdit->clear();
    titluEdit->clear();
    autorEdit->clear();
    genEdit->clear();
    anEdit->clear();
}

void MainWindow::stergeCarte() {
    try {
        int id = idStergereEdit->text().toInt();
        service.stergeCarte(id);
        QMessageBox::information(this, "Succes", "Carte stearsa!");
        loadCarti();
        updateButoaneGen();
    } catch (const std::exception& e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
    idStergereEdit->clear();
}

void MainWindow::modificaCarte() {
    try {
        int id = idModificareEdit->text().toInt();
        std::string titlu = titluModificareEdit->text().toStdString();
        std::string autor = autorModificareEdit->text().toStdString();
        std::string gen = genModificareEdit->text().toStdString();
        int an = anModificareEdit->text().toInt();

        bool okTitlu = service.modificaTitlu(id, titlu);
        bool okAutor = service.modificaAutor(id, autor);
        bool okGen = service.modificaGen(id, gen);
        bool okAn = service.modificaAnAparitie(id, an);

        if (okTitlu || okAutor || okGen || okAn) {
            QMessageBox::information(this, "Succes", "Carte modificata!");
        } else {
            QMessageBox::warning(this, "Eroare", "Nu s-a putut modifica cartea (ID inexistent?)");
        }

        loadCarti();
        updateButoaneGen();
    } catch (const std::exception& e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
    idModificareEdit->clear();
    titluModificareEdit->clear();
    autorModificareEdit->clear();
    genModificareEdit->clear();
    anModificareEdit->clear();
}

void MainWindow::loadCarti() {
    listaCarti->clear();
    for (const auto& c : service.getCarti()) {
        QString itemText = QString("[%1] Titlu: %2, Autor: %3, Gen: %4, An aparitie: %5;")
                .arg(c.getId())
                .arg(QString::fromStdString(c.getTitlu()))
                .arg(QString::fromStdString(c.getAutor()))
                .arg(QString::fromStdString(c.getGen()))
                .arg(c.getAnAparitie());
        listaCarti->addItem(itemText);
    }
}

void MainWindow::filtreazaDupaTitlu() {
    std::string titlu = filtruTitluEdit->text().toStdString();
    auto cartiFiltrate = service.filtreazaDupaTitlu(titlu);
    listaCarti->clear();
    for (const auto& c : cartiFiltrate) {
        QString itemText = QString("[%1] Titlu: %2, Autor: %3, Gen: %4, An aparitie: %5;")
                .arg(c.getId())
                .arg(QString::fromStdString(c.getTitlu()))
                .arg(QString::fromStdString(c.getAutor()))
                .arg(QString::fromStdString(c.getGen()))
                .arg(c.getAnAparitie());
        listaCarti->addItem(itemText);
    }
    filtruTitluEdit->clear();
}

void MainWindow::filtreazaDupaAn() {
    try {
        int an = filtruAnEdit->text().toInt();
        auto cartiFiltrate = service.filtreazaDupaAn(an);
        listaCarti->clear();
        for (const auto& c : cartiFiltrate) {
            QString itemText = QString("[%1] Titlu: %2, Autor: %3, Gen: %4, An aparitie: %5;")
                    .arg(c.getId())
                    .arg(QString::fromStdString(c.getTitlu()))
                    .arg(QString::fromStdString(c.getAutor()))
                    .arg(QString::fromStdString(c.getGen()))
                    .arg(c.getAnAparitie());
            listaCarti->addItem(itemText);
        }
    } catch (const std::exception& e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
    filtruAnEdit->clear();
}

void MainWindow::sorteazaCarti() {
    int mod = sortareModComboBox->currentIndex();
    int tip = sortareComboBox->currentIndex();

    Lista<Carte> cartiSortate;
    switch (tip) {
        case 0:
            cartiSortate = service.sorteazaDupaTitlu(mod);
            break;
        case 1:
            cartiSortate = service.sorteazaDupaAutor(mod);
            break;
        case 2:
            cartiSortate = service.sorteazaDupaGen(mod);
            break;
        case 3:
            cartiSortate = service.sorteazaDupaAnulAparitiei(mod);
            break;
        default:
            QMessageBox::warning(this, "Eroare", "Tip de sortare invalid");
            return;
    }

    listaCarti->clear();
    for (const auto& c : cartiSortate) {
        QString itemText = QString("[%1] Titlu: %2, Autor: %3, Gen: %4, An aparitie: %5;")
                .arg(c.getId())
                .arg(QString::fromStdString(c.getTitlu()))
                .arg(QString::fromStdString(c.getAutor()))
                .arg(QString::fromStdString(c.getGen()))
                .arg(c.getAnAparitie());
        listaCarti->addItem(itemText);
    }
}

void MainWindow::undo() {
    try {
        service.undo();
        loadCarti();
        updateButoaneGen();
    } catch (const std::exception& e) {
        QMessageBox::warning(this, "Eroare", e.what());
    }
}

void MainWindow::confirmExit() {
    if (QMessageBox::question(this, "Iesire", "Sigur doriti sa iesiti?") == QMessageBox::Yes) {
        close();
    }
}

void MainWindow::afiseazaCartiPeGen() {
    auto mapGen = service.getCartiPeGen();

    listaCarti->clear();
    for (const auto& [gen, carti] : mapGen) {
        QString itemText = QString("Gen: %1\n").arg(QString::fromStdString(gen));
        for (const auto& c : carti) {
            itemText += QString("[%1] Titlu: %2, Autor: %3, An aparitie: %4;\n")
                    .arg(c.getId())
                    .arg(QString::fromStdString(c.getTitlu()))
                    .arg(QString::fromStdString(c.getAutor()))
                    .arg(c.getAnAparitie());
        }
        listaCarti->addItem(itemText);
    }
}

void MainWindow::updateButoaneGen() {
    auto carti = service.getCarti();

    QMap<QString, int> genCount;
    for (const auto& c : carti) {
        QString gen = QString::fromStdString(c.getGen());
        genCount[gen]++;
    }

    QList<QString> genuriDeSters;
    for (auto it = butoaneGen.begin(); it != butoaneGen.end(); ++it) {
        if (!genCount.contains(it.key())) {
            delete it.value();
            genuriDeSters.append(it.key());
        }
    }

    for (const auto& gen : genuriDeSters) {
        butoaneGen.remove(gen);
    }

    for (const auto& gen : genCount.keys()) {
        if (!butoaneGen.contains(gen)) {
            QPushButton* btn = new QPushButton(gen);
            secondLayout->addWidget(btn);
            butoaneGen[gen] = btn;

            connect(btn, &QPushButton::clicked, this, [this, gen]() {
                int count = 0;
                for (const auto& c : service.getCarti()) {
                    if (QString::fromStdString(c.getGen()) == gen) {
                        count++;
                    }
                }
                QMessageBox::information(this, "Carti pe gen",
                                         QString("Exista %1 carte/carti cu genul '%2'").arg(count).arg(gen));
            });
        }
    }
}