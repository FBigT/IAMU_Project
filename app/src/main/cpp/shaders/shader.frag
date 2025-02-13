#version 450
layout(location = 0) in vec2 fragUV;
layout(location = 0) out vec4 outColor;

void main() {
    outColor = vec4(fragUV, 1.0, 1.0); // UV gradient color (R = U, G = V, B = 1.0)
}
