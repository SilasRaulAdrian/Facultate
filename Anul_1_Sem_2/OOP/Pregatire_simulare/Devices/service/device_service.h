//
// Created by Raul on 16.05.2025.
//

#ifndef DEVICES_DEVICE_SERVICE_H
#define DEVICES_DEVICE_SERVICE_H

#include "../domain/device.h"
#include "../repository/device_repo.h"
#include <vector>

class DeviceService {
private:
    DeviceRepo &repo;

public:
    DeviceService(DeviceRepo &repo) : repo(repo) {}

    std::vector<Device> getAll() const;
    std::vector<Device> sortByModel() const;
    std::vector<Device> sortByPret() const;
};

#endif //DEVICES_DEVICE_SERVICE_H
