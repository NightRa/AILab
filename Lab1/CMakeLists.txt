cmake_minimum_required(VERSION 3.3)
project(Lab1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    Hw1/Hw1.cpp)

add_executable(Lab1 ${SOURCE_FILES})