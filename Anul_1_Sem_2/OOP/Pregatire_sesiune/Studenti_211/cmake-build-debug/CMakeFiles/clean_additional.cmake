# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles\\Studenti_211_autogen.dir\\AutogenUsed.txt"
  "CMakeFiles\\Studenti_211_autogen.dir\\ParseCache.txt"
  "Studenti_211_autogen"
  )
endif()
