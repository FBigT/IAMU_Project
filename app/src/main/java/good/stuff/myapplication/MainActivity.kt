package good.stuff.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import good.stuff.myapplication.databinding.ActivityMainBinding
import good.stuff.myapplication.render.LavaLampGLSurfaceView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var glSurfaceView: LavaLampGLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize OpenGL SurfaceView and add it to the layout
        glSurfaceView = LavaLampGLSurfaceView(this)
        binding.root.addView(glSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }
}
