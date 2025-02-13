cmake_minimum_required(VERSION 3.10.2)

project("myapplication")

add_library(
        vulkan-lib
        SHARED
        vulkan_renderer.cpp
)

find_package(Vulkan REQUIRED)
target_link_libraries(vulkan-lib Vulkan::Vulkan android log)

target_include_directories(vulkan-lib PRIVATE ${Vulkan_INCLUDE_DIRS})

# Copy shaders
add_custom_target(copy_shaders ALL
        COMMAND ${CMAKE_COMMAND} -E copy_directory
        ${CMAKE_SOURCE_DIR}/shaders
        ${CMAKE_BINARY_DIR}/shaders
)
add_dependencies(vulkan-lib copy_shaders)
