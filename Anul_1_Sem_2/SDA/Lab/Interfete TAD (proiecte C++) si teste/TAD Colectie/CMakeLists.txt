cmake_minimum_required(VERSION 3.28)
project(TAD_Colectie)

set(CMAKE_CXX_STANDARD 14)

include_directories("Proiect_C++/Colectie")
include_directories("Proiect_C++/Colectie")
include_directories(Teste)
include_directories("Teste")

add_executable(TAD_Colectie
        "Proiect_C++/Colectie/App.cpp"
        "Proiect_C++/Colectie/Colectie.cpp"
        "Proiect_C++/Colectie/Colectie.h"
        "Proiect_C++/Colectie/IteratorColectie.h"
        "Proiect_C++/Colectie/IteratorColectie.cpp"
        "Teste/TestExtins.cpp"
        "Teste/TestExtins.h"
        "Teste/TestScurt.cpp"
        "Teste/TestScurt.h")
