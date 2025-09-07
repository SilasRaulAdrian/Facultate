#include "pc_service.h"

std::vector<Procesor> PCService::getAllProcesoare() const {
    return procesorRepo.getAll();
}

std::vector<PlacaDeBaza> PCService::getAllPlaciDeBaza() const {
    return placaDeBazaRepo.getAll();
}

void PCService::adaugaPlaca(const QString &nume, const QString &socluProcesor, int pret) const {
    placaDeBazaRepo.adaugaPlacaDeBaza(nume, socluProcesor, pret);
}

std::vector<PlacaDeBaza> PCService::filterBySoclu(const QString &socluProcesor) {
    std::vector<PlacaDeBaza> placi = placaDeBazaRepo.getAll();
    std::vector<PlacaDeBaza> result;

    for (const auto& pb : placi)
        if (pb.getSocluProcesor() == socluProcesor)
            result.push_back(pb);

    return result;
}

int PCService::pretTotal(const Procesor &p, const PlacaDeBaza &pb) {
    return p.getPret() + pb.getPret();
}