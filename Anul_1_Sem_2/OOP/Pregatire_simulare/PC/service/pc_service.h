//
// Created by Raul on 18.05.2025.
//

#ifndef PC_PC_SERVICE_H
#define PC_PC_SERVICE_H

#include "../repository/procesor_repo.h"
#include "../repository/placa_de_baza_repo.h"
#include "../domain/placa_de_baza.h"
#include "../domain/procesor.h"
#include <vector>

class PCService {
private:
    ProcesorRepo &procesorRepo;
    PlacaDeBazaRepo &placaDeBazaRepo;

public:
    PCService(ProcesorRepo &procesorRepo, PlacaDeBazaRepo &placaDeBazaRepo) :
        procesorRepo(procesorRepo), placaDeBazaRepo(placaDeBazaRepo) {}

    std::vector<Procesor> getAllProcesoare() const;
    std::vector<PlacaDeBaza> getAllPlaciDeBaza() const;
    void adaugaPlaca(const QString& nume, const QString &socluProcesor, int pret) const;
    std::vector<PlacaDeBaza> filterBySoclu(const QString &socluProcesor);
    int pretTotal(const Procesor &p, const PlacaDeBaza &pb);
};

#endif //PC_PC_SERVICE_H
