#include "concert_repo.h"
#include <QFile>
#include <QTextStream>

ConcertRepo::ConcertRepo(const QString &filename) : filename(filename) {
    loadFromFile();
}

void ConcertRepo::loadFromFile() {
    QFile file(filename);
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
        throw std::runtime_error("Nu s-a putut deschide fisierul.");
    }
    QTextStream in(&file);

    while (!in.atEnd()) {
        QString line = in.readLine();
        QStringList tokens = line.split(',');

        if (tokens.size() != 3) continue;

        QString numeFormatie = tokens[0].trimmed();
        QStringList dataParti = tokens[1].trimmed().split('/');
        if (dataParti.size() != 2) continue;

        bool ok;
        int luna = dataParti[0].toInt(&ok);
        if (!ok || luna < 1 || luna > 12) continue;

        int zi = dataParti[1].toInt(&ok);
        if (!ok || zi < 1 || zi > 31) continue;

        QDate data(QDate::currentDate().year(), luna, zi);
        if (!data.isValid()) continue;

        int nrBilete = tokens[2].toInt();
        if (nrBilete <= 0) continue;

        concerte.emplace_back(numeFormatie, data, nrBilete);
    }

    file.close();
}

std::vector<Concert> ConcertRepo::getAll() const {
    return concerte;
}

void ConcertRepo::actualizeazaConcert(int index, const Concert &concert) {
    if (index < concerte.size())
        concerte[index] = concert;
}