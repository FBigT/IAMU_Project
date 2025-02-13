#define VK_USE_PLATFORM_ANDROID_KHR
#include <vulkan/vulkan.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <vector>
#include <stdexcept>

// Vulkan instance
VkInstance instance;

// Vulkan surface
VkSurfaceKHR surface;

// Function to create a Vulkan instance
void createInstance() {
    VkApplicationInfo appInfo{};
    appInfo.sType = VK_STRUCTURE_TYPE_APPLICATION_INFO;
    appInfo.pApplicationName = "VulkanApp";
    appInfo.applicationVersion = VK_MAKE_VERSION(1, 0, 0);
    appInfo.pEngineName = "No Engine";
    appInfo.engineVersion = VK_MAKE_VERSION(1, 0, 0);
    appInfo.apiVersion = VK_API_VERSION_1_0;

    // List of required extensions
    const std::vector<const char*> extensions = {
            VK_KHR_SURFACE_EXTENSION_NAME,
            VK_KHR_ANDROID_SURFACE_EXTENSION_NAME
    };

    VkInstanceCreateInfo createInfo{};
    createInfo.sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
    createInfo.pApplicationInfo = &appInfo;
    createInfo.enabledExtensionCount = static_cast<uint32_t>(extensions.size());
    createInfo.ppEnabledExtensionNames = extensions.data();

    if (vkCreateInstance(&createInfo, nullptr, &instance) != VK_SUCCESS) {
        throw std::runtime_error("Failed to create Vulkan instance!");
    }
}

// Function to create a Vulkan surface
void createSurface(ANativeWindow* window) {
    typedef VkResult (VKAPI_PTR *PFN_vkCreateAndroidSurfaceKHR)(VkInstance, const VkAndroidSurfaceCreateInfoKHR*, const VkAllocationCallbacks*, VkSurfaceKHR*);
    auto vkCreateAndroidSurfaceKHR = reinterpret_cast<PFN_vkCreateAndroidSurfaceKHR>(
            vkGetInstanceProcAddr(instance, "vkCreateAndroidSurfaceKHR")
    );

    if (!vkCreateAndroidSurfaceKHR) {
        throw std::runtime_error("Failed to load vkCreateAndroidSurfaceKHR!");
    }

    VkAndroidSurfaceCreateInfoKHR createInfo{};
    createInfo.sType = VK_STRUCTURE_TYPE_ANDROID_SURFACE_CREATE_INFO_KHR;
    createInfo.window = window;

    if (vkCreateAndroidSurfaceKHR(instance, &createInfo, nullptr, &surface) != VK_SUCCESS) {
        throw std::runtime_error("Failed to create Vulkan surface!");
    }
}

// Function to initialize Vulkan
extern "C"
JNIEXPORT void JNICALL
Java_com_example_vulkanapp_VulkanRenderer_initVulkan(JNIEnv* env, jobject thiz, jobject javaSurface) {
    (void)env; // Suppress unused parameter warning
    (void)thiz; // Suppress unused parameter warning

    // Get the native window from the Java surface
    ANativeWindow* window = ANativeWindow_fromSurface(env, javaSurface);

    // Create Vulkan instance
    createInstance();

    // Create Vulkan surface
    createSurface(window);
}