#include "jucator_repo.h"
#include <QFile>
#include <QTextStream>

JucatorRepo::JucatorRepo(const QString &filename) : filename(filename) {
    loadFromFile();
}

void JucatorRepo::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.");
    }
    QTextStream in(&file);

    while(!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 4) continue;

        QString nume = tokens[0];
        QString tara = tokens[1];
        int puncte = tokens[2].toInt();
        int ranking = tokens[3].toInt();

        jucatori.emplace_back(nume, tara, puncte, ranking);
    }

    file.close();
}

std::vector<Jucator> &JucatorRepo::getAll() {
    return jucatori;
}