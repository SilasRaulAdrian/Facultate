//
// Created by Raul on 23.06.2025.
//
#include "main_window.h"
#include <QHBoxLayout>
#include <QMessageBox>
#include <QHeaderView>

void paintBars(QWidget* widget, ServiceMelodii& service) {
    QPainter painter(widget);
    painter.setPen(Qt::black);
    painter.setBrush(Qt::blue);

    int w = widget->width();
    int h = widget->height();
    int barWidth = w / 10;
    int maxCount = 1;

    for (int i = 0; i <= 10; ++i) {
        int c = service.countRank(i);
        if (c > maxCount) maxCount = c;
    }

    for (int i = 0; i <= 10; ++i) {
        int x = i * barWidth;
        int barHeight = (double(service.countRank(i)) / maxCount) * (h - 20);
        painter.drawRect(x, h - barHeight, barWidth - 5, barHeight);
    }
}

class BarWidget : public QWidget {
    ServiceMelodii& srv;
public:
    explicit BarWidget(ServiceMelodii& s, QWidget* parent = nullptr) : QWidget(parent), srv(s) {}

protected:
    void paintEvent(QPaintEvent*) override {
        paintBars(this, srv);
    }
};

MainWindow::MainWindow(ServiceMelodii &srv, QWidget *parent) :
        QMainWindow(parent), service(srv) {
    initGUI();
    connectSignalsSlots();
}

void MainWindow::initGUI() {
    centralWidget = new QWidget;
    this->setCentralWidget(centralWidget);

    auto mainLayout = new QVBoxLayout;
    auto formLayout = new QHBoxLayout;

    // tabel
    model = new MelodiiModel(service);
    table = new QTableView;
    table->setModel(model);
    table->setSelectionBehavior(QAbstractItemView::SelectRows);
    table->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);

    // input titlu + slider rank
    editTitlu = new QLineEdit;
    sliderRank = new QSlider(Qt::Horizontal);
    sliderRank->setRange(0, 10);
    sliderRank->setTickInterval(1);
    sliderRank->setTickPosition(QSlider::TicksBelow);
    labelRankValue = new QLabel("0");
    sliderRank->setValue(0);

    formLayout->addWidget(new QLabel("Titlu:"));
    formLayout->addWidget(editTitlu);
    formLayout->addWidget(new QLabel("Rank:"));
    formLayout->addWidget(sliderRank);
    formLayout->addWidget(labelRankValue);

    // butoane
    btnUpdate = new QPushButton("Update");
    btnDelete = new QPushButton("Delete");

    auto buttonLayout = new QHBoxLayout;
    buttonLayout->addWidget(btnUpdate);
    buttonLayout->addWidget(btnDelete);

    // zona bare
    rankBars = new BarWidget(service);
    rankBars->setMinimumHeight(100);
    rankBars->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);

    mainLayout->addWidget(table);
    mainLayout->addLayout(formLayout);
    mainLayout->addLayout(buttonLayout);
    mainLayout->addWidget(rankBars);

    centralWidget->setLayout(mainLayout);
}

void MainWindow::connectSignalsSlots() {
    connect(table->selectionModel(), &QItemSelectionModel::selectionChanged, this, &MainWindow::updateFieldsFromSelection);

    connect(btnUpdate, &QPushButton::clicked, this, [this]() {
        auto indexes = table->selectionModel()->selectedIndexes();
        if (indexes.isEmpty()) return;

        int row = indexes[0].row();
        int id = model->index(row, 0).data().toInt();
        QString titluNou = editTitlu->text();
        int rankNou = sliderRank->value();

        try {
            service.updateMelodie(id, titluNou, rankNou);
            model->actualizeazaModel();
            updateBarChart();
        }
        catch (std::exception& ex) {
            QMessageBox::warning(this, "Eroare", ex.what());
        }
    });

    connect(btnDelete, &QPushButton::clicked, this, [this]() {
       auto indexes = table->selectionModel()->selectedIndexes();
       if (indexes.isEmpty()) return;

       int row = indexes[0].row();
       int id = model->index(row, 0).data().toInt();

       try {
           service.stergeMelodie(id);
           model->actualizeazaModel();
           updateBarChart();
       } catch (std::exception& ex) {
           QMessageBox::warning(this, "informatie", ex.what());
       }
    });

    connect(sliderRank, &QSlider::valueChanged, this, [this](int value) {
       labelRankValue->setText(QString::number(value));
    });

    connect(model, &QAbstractTableModel::modelReset, this, &MainWindow::updateBarChart);
}

void MainWindow::updateFieldsFromSelection() {
    auto indexes = table->selectionModel()->selectedIndexes();
    if (indexes.isEmpty()) return;

    int row = indexes[0].row();
    QString titlu = model->index(row, 1).data().toString();
    int rank = model->index(row, 3).data().toInt();

    editTitlu->setText(titlu);
    sliderRank->setValue(rank);
}

void MainWindow::updateBarChart() {
    rankBars->update();
}