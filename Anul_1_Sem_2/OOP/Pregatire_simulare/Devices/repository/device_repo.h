//
// Created by Raul on 16.05.2025.
//

#ifndef DEVICES_DEVICE_REPO_H
#define DEVICES_DEVICE_REPO_H

#include "../domain/device.h"
#include <vector>
#include <QString>

class DeviceRepo {
private:
    std::vector<Device> devices;
    QString filename;

    void loadFromFile();

public:
    DeviceRepo(const QString &filename);
    const std::vector<Device>& getAll() const;
};

#endif //DEVICES_DEVICE_REPO_H
