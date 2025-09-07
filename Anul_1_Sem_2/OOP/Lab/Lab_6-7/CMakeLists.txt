cmake_minimum_required(VERSION 3.28)
project(Lab_6_7)

set(CMAKE_CXX_STANDARD 23)

add_executable(Lab_6_7 main.cpp
        domain/carte.h
        repository/carte_repo.h
        repository/carte_repo.cpp
        service/carte_service.h
        service/carte_service.cpp
        ui/console.h
        ui/console.cpp
        tests/tests.h
        tests/tests.cpp
        domain/carte.cpp
        vector_dinamic/lista.h
        exceptions/exceptii.h
        cos_inchirieri/cos_inchirieri.h
        cos_inchirieri/cos_inchirieri.cpp
        undo/undo.h
)
