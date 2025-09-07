# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles\\Devices_autogen.dir\\AutogenUsed.txt"
  "CMakeFiles\\Devices_autogen.dir\\ParseCache.txt"
  "Devices_autogen"
  )
endif()
