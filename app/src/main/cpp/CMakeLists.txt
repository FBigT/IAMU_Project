cmake_minimum_required(VERSION 3.10.2)

project("myapplication")

# Add your native source files
add_library(
        vulkan-lib
        SHARED
        vulkan_renderer.cpp
)

# Find and link Vulkan
find_package(Vulkan REQUIRED)
target_link_libraries(vulkan-lib Vulkan::Vulkan)

# Include Vulkan headers
target_include_directories(vulkan-lib PRIVATE ${Vulkan_INCLUDE_DIRS})