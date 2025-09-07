cmake_minimum_required(VERSION 3.28.3)
project(lab2_4)

set(CMAKE_CXX_STANDARD 20)
set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra")

add_executable(lab2_4 main.c
        domain/oferta.h
        repo/repo_oferte.h
        repo/repo_oferte.c
        teste/teste.h
        teste/teste.c
        service/service.h
        service/service.c
        validator/validator.c
        validator/validator.h
        ui/consola.c
        ui/consola.h
        domain/oferta.c)
