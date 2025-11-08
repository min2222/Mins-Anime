#version 150

in vec4 Position;

uniform mat4 InverseTransformMatrix;
uniform mat4 ProjMat;
uniform vec2 InSize;
uniform vec2 OutSize;

out vec4 near_4;
out vec4 far_4;
out vec2 texCoord;
out vec2 oneTexel;

void main(){
    vec4 outPos = ProjMat * vec4(Position.xy, 0.0, 1.0);
    gl_Position = vec4(outPos.xy, 0.2, 1.0);

    oneTexel = 1.0 / InSize;

    texCoord = Position.xy / OutSize;
    
    vec2 pos = gl_Position.xy/gl_Position.w;
    near_4 = InverseTransformMatrix * (vec4(pos, -1.0, 1.0));       
    far_4 = InverseTransformMatrix * (vec4(pos, +1.0, 1.0));
}
