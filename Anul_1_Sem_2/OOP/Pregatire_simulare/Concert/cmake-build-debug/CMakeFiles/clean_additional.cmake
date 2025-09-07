# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles\\Concert_autogen.dir\\AutogenUsed.txt"
  "CMakeFiles\\Concert_autogen.dir\\ParseCache.txt"
  "Concert_autogen"
  )
endif()
