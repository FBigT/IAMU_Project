#version 450
layout(location = 0) in vec2 inPosition;
layout(location = 0) out vec2 fragUV;

void main() {
    fragUV = (inPosition + vec2(1.0)) * 0.5; // Normalize UV to 0-1 range
    gl_Position = vec4(inPosition, 0.0, 1.0);
}
