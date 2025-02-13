package good.stuff.myapplication.render

import android.content.Context
import android.opengl.GLSurfaceView

class LavaLampGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: LavaLampRenderer

    init {
        setEGLContextClientVersion(2) // Use OpenGL ES 2.0
        renderer = LavaLampRenderer(context)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }
}
