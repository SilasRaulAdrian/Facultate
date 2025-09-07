# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles\\Tractoare_autogen.dir\\AutogenUsed.txt"
  "CMakeFiles\\Tractoare_autogen.dir\\ParseCache.txt"
  "Tractoare_autogen"
  )
endif()
