package good.stuff.myapplication.render

import android.view.Surface

// dont work dont use
class VulkanRenderer {
    companion object {
        init {
            System.loadLibrary("vulkan-lib")
        }
    }

    external fun initVulkan(surface: Surface)
}
