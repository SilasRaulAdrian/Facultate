#include "main_window.h"

QVBoxLayout *MainWindow::creareTabla(int dim, const QString &tabla) {
    QVBoxLayout *coloana = new QVBoxLayout;
    int cnt = 0;
    for (int i = 0; i < dim; ++i) {
        QHBoxLayout *linie = new QHBoxLayout;
        for (int j = 0; j < dim; ++j) {
            QString s;
            s.clear();
            s.push_back(tabla[cnt]);
            if (s == "-")
                s = " ";
            QPushButton* buton = new QPushButton(s);
            cnt++;
            buton->setFixedHeight(75);
            buton->setFixedWidth(75);
            butoane.push_back(buton);
            linie->addWidget(buton);

            if (s == " ") {
                QObject::connect(buton, &QPushButton::clicked, [this, buton]()
                {
                    QString jucator = txtJucatorCurent->text();
                    buton->setText(jucator);

                    int id = txtID->text().toInt();
                    int dim = txtDimensiune->text().toInt();
                    QString tabla = txtTablaJoc->text();

                    for (int k = 0; k < butoane.size(); k++)
                    {
                        if (butoane[k] == buton)
                        {
                            tabla[k] = jucator[0];
                        }
                    }

                    if (txtJucatorCurent->text() == "X")
                    {
                        txtJucatorCurent->setText("O");
                        txtStare->setText("In derulare");
                        txtTablaJoc->setText(tabla);

                        service.modificaJoc(txtID->text().toInt(), txtDimensiune->text().toInt(), tabla, 'O', "InDerulare");
                    }
                    else
                    {
                        txtJucatorCurent->setText("O");
                        txtStare->setText("In derulare");
                        txtTablaJoc->setText(tabla);

                        service.modificaJoc(txtID->text().toInt(), txtDimensiune->text().toInt(), tabla, 'X', "InDerulare");
                    }

                    loadData(service.getAllSorted());
                });
            }
        }
        coloana->addLayout(linie);
    }
    return coloana;
}

void MainWindow::initGUI()
{
    setLayout(mainLayout);

    QVBoxLayout* layoutStanga = new QVBoxLayout;
    QVBoxLayout* layoutDreapta = new QVBoxLayout;

    layoutStanga->addWidget(tabel);

    QFormLayout* layoutTxt = new QFormLayout;
    layoutTxt->addRow("DIM: ", txtDim);
    layoutTxt->addRow("TABLA: ", txtTabla);
    layoutTxt->addRow("JUCATOR: ", txtJucator);

    layoutDreapta->addLayout(layoutTxt);
    layoutDreapta->addWidget(butonAdaugare);

    QFormLayout* layoutModificare = new QFormLayout;
    layoutModificare->addRow("ID: ", txtID);
    layoutModificare->addRow("DIM: ", txtDimensiune);
    layoutModificare->addRow("TABLA: ", txtTablaJoc);
    layoutModificare->addRow("JUCATOR: ", txtJucatorCurent);
    layoutModificare->addRow("STARE: ", txtStare);

    layoutDreapta->addLayout(layoutModificare);
    layoutDreapta->addWidget(butonModificare);

    layoutJos->addLayout(creareTabla(3, "---------"));

    layoutSus->addLayout(layoutStanga);
    layoutSus->addLayout(layoutDreapta);
    mainLayout->addLayout(layoutSus);
    mainLayout->addLayout(layoutJos);
}

void MainWindow::initConnect()
{
    QObject::connect(butonAdaugare, &QPushButton::clicked, [&]()
    {
        int dim = txtDim->text().toInt();
        QString tabla = txtTabla->text();
        QChar jucator = txtJucator->text()[0];

        try
        {
            int id = rand() % 100;
            service.adaugaJoc(id, dim, tabla, jucator, "Neinceput");
            loadData(service.getAllSorted());
        }
        catch (std::exception& e)
        {
            QMessageBox::warning(nullptr, "Date invalide", e.what());
        }
    });

    QObject::connect(butonModificare, &QPushButton::clicked, [&]()
    {
        int id = txtID->text().toInt();
        int dim = txtDimensiune->text().toInt();
        QString tabla = txtTablaJoc->text();
        QChar jucator = txtJucatorCurent->text()[0];
        QString stare = txtStare->text();

        try
        {
            if (stare == "In derulare")
                stare = "InDerulare";
            service.modificaJoc(id, dim, tabla, jucator, stare);
            loadData(service.getAllSorted());
        }
        catch (std::exception& e)
        {
            QMessageBox::warning(nullptr, "Date invalide", e.what());
        }
    });

    QObject::connect(tabel->selectionModel(), &QItemSelectionModel::selectionChanged, [this]()
    {
        if (tabel->selectionModel()->selectedIndexes().isEmpty())
        {
            txtID->setText("");
            txtDimensiune->setText("");
            txtTablaJoc->setText("");
            txtJucatorCurent->setText("");
            txtStare->setText("");
            return;
        }
        int selRow = tabel->selectionModel()->selectedIndexes().at(0).row();
        auto cel1Index = tabel->model()->index(selRow, 1);
        auto cel0Index = tabel->model()->index(selRow, 0);
        auto cel2Index = tabel->model()->index(selRow, 2);
        auto cel3Index = tabel->model()->index(selRow, 3);
        auto cel4Index = tabel->model()->index(selRow, 4);

        auto cel1Value = tabel->model()->data(cel1Index, Qt::DisplayRole).toString();
        auto cel2Value = tabel->model()->data(cel2Index, Qt::DisplayRole).toString();
        auto cel3Value = tabel->model()->data(cel3Index, Qt::DisplayRole).toString();
        auto cel4Value = tabel->model()->data(cel4Index, Qt::DisplayRole).toString();
        auto cel0Value = tabel->model()->data(cel0Index, Qt::DisplayRole).toString();

        txtID->setText(cel0Value);
        txtDimensiune->setText(cel1Value);
        txtTablaJoc->setText(cel2Value);
        txtJucatorCurent->setText(cel3Value);
        txtStare->setText(cel4Value);

        for (auto buton : butoane)
            delete buton;
        butoane.clear();
        layoutJos->addLayout(creareTabla(txtDimensiune->text().toInt(), txtTablaJoc->text()));
    });
}

void MainWindow::loadData(const std::vector<Joc>& jocuri)
{
    model->setJocuri(jocuri);
    model->refresh();
}

