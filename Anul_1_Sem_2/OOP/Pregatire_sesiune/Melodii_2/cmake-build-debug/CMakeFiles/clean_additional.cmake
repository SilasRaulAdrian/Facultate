# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles\\Melodii_2_autogen.dir\\AutogenUsed.txt"
  "CMakeFiles\\Melodii_2_autogen.dir\\ParseCache.txt"
  "Melodii_2_autogen"
  )
endif()
