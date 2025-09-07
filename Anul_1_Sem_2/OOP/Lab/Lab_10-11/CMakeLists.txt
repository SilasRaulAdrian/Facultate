cmake_minimum_required(VERSION 3.28)
project(Lab_6_7)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_PREFIX_PATH "C:/Qt/6.9.0/mingw_64/lib/cmake/Qt6")
find_package(Qt6 REQUIRED COMPONENTS Widgets)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC ON)
set(CMAKE_AUTORCC ON)

add_executable(Lab_6_7 main.cpp
        domain/carte.h
        repository/carte_repo.h
        repository/carte_repo.cpp
        service/carte_service.h
        service/carte_service.cpp
        tests/tests.h
        tests/tests.cpp
        domain/carte.cpp
        vector_dinamic/lista.h
        exceptions/exceptii.h
        cos_inchirieri/cos_inchirieri.h
        cos_inchirieri/cos_inchirieri.cpp
        undo/undo.h
        windows/mainwindow.h
        windows/mainwindow.cpp
        windows/cos_window.h
        windows/cos_window.cpp
        observer/observer.h
        model_view/cos_model.h
        model_view/cos_model.cpp
        windows/cosCRUDGUI.h
        windows/cosCRUDGUI.cpp
        windows/cosReadOnlyGUI.h
        windows/cosReadOnlyGUI.cpp
)

target_link_libraries(Lab_6_7 Qt6::Widgets)
