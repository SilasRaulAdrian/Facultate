cmake_minimum_required(VERSION 3.28)
project(TAD_Multime)

set(CMAKE_CXX_STANDARD 14)

include_directories("Proiect C++/Multime (cu rel)")
include_directories("Teste(cu relatie)")

add_executable(TAD_Multime
        "Proiect C++/Multime (cu rel)/App.cpp"
        "Proiect C++/Multime (cu rel)/IteratorMultime.cpp"
        "Proiect C++/Multime (cu rel)/IteratorMultime.h"
        "Proiect C++/Multime (cu rel)/Multime.cpp"
        "Proiect C++/Multime (cu rel)/Multime.h"
        "Teste(cu relatie)/TestExtins.cpp"
        "Teste(cu relatie)/TestExtins.h"
        "Teste(cu relatie)/TestScurt.cpp"
        "Teste(cu relatie)/TestScurt.h")
