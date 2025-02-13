package good.stuff.myapplication.render

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class LavaLampRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private var program = 0
    private var timeLocation = 0
    private var resolutionLocation = 0
    private var positionHandle = 0
    private var startTime = System.currentTimeMillis()

    private lateinit var vertexBuffer: FloatBuffer

    // Define a full-screen quad
    private val vertices = floatArrayOf(
        -1f, -1f,   // Bottom Left
        1f, -1f,   // Bottom Right
        -1f,  1f,   // Top Left
        1f,  1f    // Top Right
    )

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)

        val vertexShaderCode = """
            attribute vec4 vPosition;
            void main() {
                gl_Position = vPosition;
            }
        """.trimIndent()

        val fragmentShaderCode = """
            precision mediump float;
            uniform vec2 uResolution;
            uniform float uTime;
            
            // Smoothstep function for blending
            float smoothBlob(vec2 uv, float x, float y, float radius) {
                return smoothstep(radius, radius * 0.9, length(uv - vec2(x, y)));
            }

            void main() {
                vec2 uv = gl_FragCoord.xy / uResolution.xy;
                uv.y = 1.0 - uv.y; // Flip Y axis for OpenGL
                
                // Metaballs (Lava Lamp effect)
                float blob1 = smoothBlob(uv, 0.3 + 0.1 * sin(uTime * 1.2), 0.5 + 0.2 * cos(uTime * 1.3), 0.2);
                float blob2 = smoothBlob(uv, 0.7 + 0.1 * cos(uTime * 1.5), 0.5 + 0.2 * sin(uTime * 1.1), 0.2);
                float blob3 = smoothBlob(uv, 0.5 + 0.15 * cos(uTime * 1.8), 0.4 + 0.15 * sin(uTime * 2.0), 0.2);
                
                float colorIntensity = blob1 + blob2 + blob3;
                
                vec3 color = mix(vec3(0.1, 0.0, 0.3), vec3(1.0, 0.2, 0.0), colorIntensity);
                
                gl_FragColor = vec4(color, 1.0);
            }
        """.trimIndent()

        program = createProgram(vertexShaderCode, fragmentShaderCode)
        GLES20.glUseProgram(program)

        // Get uniform locations
        timeLocation = GLES20.glGetUniformLocation(program, "uTime")
        resolutionLocation = GLES20.glGetUniformLocation(program, "uResolution")
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")

        // Create a buffer for vertex data
        val bb = ByteBuffer.allocateDirect(vertices.size * 4) // Float = 4 bytes
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(vertices)
        vertexBuffer.position(0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        val currentTime = (System.currentTimeMillis() - startTime) / 1000f
        GLES20.glUniform1f(timeLocation, currentTime)
        GLES20.glUniform2f(resolutionLocation, 1080f, 1920f)

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    private fun createProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)
        return program
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }
}
