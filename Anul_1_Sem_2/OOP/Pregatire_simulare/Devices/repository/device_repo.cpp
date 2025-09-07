#include "device_repo.h"
#include <QFile>
#include <QTextStream>

DeviceRepo::DeviceRepo(const QString &filename) : filename(filename) {
    loadFromFile();
}

void DeviceRepo::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.\n");
    }
    QTextStream in(&file);

    while(!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 5) continue;

        QString tip = tokens[0];
        QString model = tokens[1];
        int an = tokens[2].toInt();
        QString culoare = tokens[3];
        double pret = tokens[4].toDouble();

        devices.emplace_back(tip, model, an, culoare, pret);
    }

    file.close();
}

const std::vector<Device> &DeviceRepo::getAll() const {
    return devices;
}