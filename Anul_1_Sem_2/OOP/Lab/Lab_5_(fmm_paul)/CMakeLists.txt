cmake_minimum_required(VERSION 3.28)
project(Lab_5__fmm_paul_ C)

set(CMAKE_C_STANDARD 23)

add_executable(Lab_5__fmm_paul_ main.c
        list.h
        list.c
        oferte.h
        oferte.c
        service.h
        service.c
        validare.h
        validare.c
        ui.h
        ui.c
        teste.h
        teste.c)
