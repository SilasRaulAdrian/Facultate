# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles\\Jucator_v1_autogen.dir\\AutogenUsed.txt"
  "CMakeFiles\\Jucator_v1_autogen.dir\\ParseCache.txt"
  "Jucator_v1_autogen"
  )
endif()
