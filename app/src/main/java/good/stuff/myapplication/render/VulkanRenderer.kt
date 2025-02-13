package good.stuff.myapplication.render

class VulkanRenderer {
    companion object {
        init {
            System.loadLibrary("vulkan-lib")
        }
    }

    external fun initVulkan(surface: Surface)
}