cmake_minimum_required(VERSION 3.28.3)
project(Pb_8 C)

set(CMAKE_C_STANDARD 23)
set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra")

add_executable(Pb_8 main.c
        produs.h
        validare.h
        validare.c
        produs.c
        list.h
        list.c
        service.h
        service.c
        ui.h
        ui.c
        test.h
        test.c)
