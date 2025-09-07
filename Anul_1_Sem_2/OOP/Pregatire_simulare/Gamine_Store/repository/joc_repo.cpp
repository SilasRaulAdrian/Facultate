#include "joc_repo.h"
#include <QFile>
#include <QTextStream>

JocRepo::JocRepo(const QString &filename) : filename(filename) {
    loadFromFile();
}

void JocRepo::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.");
    }
    QTextStream in(&file);

    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 4) continue;

        QString titlu = tokens[0];
        int pret = tokens[1].toInt();
        QString platforma = tokens[2];
        int pegi = tokens[3].toInt();

        jocuri.emplace_back(titlu, pret, platforma, pegi);
    }

    file.close();
}

const std::vector<Joc> &JocRepo::getAll() const {
    return jocuri;
}