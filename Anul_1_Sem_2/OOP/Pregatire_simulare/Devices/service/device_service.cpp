#include "device_service.h"
#include <algorithm>

std::vector<Device> DeviceService::getAll() const {
    return repo.getAll();
}

std::vector<Device> DeviceService::sortByModel() const {
    std::vector<Device> sorted = repo.getAll();
    std::sort(sorted.begin(), sorted.end(), [](const Device &a, const Device &b) {
       return a.getModel().toLower() < b.getModel().toLower();
    });
    return sorted;
}

std::vector<Device> DeviceService::sortByPret() const {
    std::vector<Device> sorted = repo.getAll();
    std::sort(sorted.begin(), sorted.end(), [](const Device &a, const Device &b) {
       return a.getPret() < b.getPret();
    });
    return sorted;
}