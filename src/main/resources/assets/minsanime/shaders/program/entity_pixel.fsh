#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D SceneSampler;
uniform sampler2D DepthSampler;

uniform mat4 InverseProjectionMatrix;

in vec2 texCoord;
in vec4 near_4;
in vec4 far_4;

out vec4 fragColor;

const float CUBE_SIZE = 0.3;

#define near 0.05
#define far 1000.0
float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * near * far) / (far + near - z * (far - near));
}

float sdBox(vec3 p, vec3 b) {
    vec3 q = abs(p) - b;
    return length(max(q, 0.0)) + min(max(q.x, max(q.y, q.z)), 0.0);
}

float map_static_cube(vec3 p) {
    return sdBox(p, vec3(CUBE_SIZE / 2.0));
}

float raymarch(vec3 ro, vec3 rd) {
    float d = 0.0;
    for (int i = 0; i < 30; i++) {
        vec3 p = ro + rd * d;
        float ds = map_static_cube(p);
        if (ds < 0.001) { return d; }
        if (d > 20.0) { break; }
        d += ds;
    }
    return -1.0;
}

vec3 getNormal(vec3 p) {
    vec2 e = vec2(0.001, 0.0);
    return normalize(vec3(
        map_static_cube(p + e.xyy) - map_static_cube(p - e.xyy), 
        map_static_cube(p + e.yxy) - map_static_cube(p - e.yxy), 
        map_static_cube(p + e.yyx) - map_static_cube(p - e.yyx)
    ));
}

void main() {
    vec4 entityPixel = texture(DiffuseSampler, texCoord);
    vec4 backgroundPixel = texture(SceneSampler, texCoord);
    float depth = linearizeDepth(texture(DepthSampler, texCoord).r);
    
    vec3 ro = near_4.xyz / near_4.w;
    vec3 rd = normalize(far_4.xyz / far_4.w - ro);

    vec4 cubeLayer = vec4(0.0);
    float distance = raymarch(ro, rd);

    if (distance > 0.0) {
        vec3 hitPos = ro + rd * distance;
        vec3 normal = getNormal(hitPos);
        vec3 lightDir = normalize(vec3(0.5, 0.8, -0.2));
        float diffuse = max(0.0, dot(normal, lightDir)) * 0.7 + 0.3;
        vec3 blueColor = vec3(0.2, 0.5, 1.0);
        
        cubeLayer = backgroundPixel;
    }

    fragColor = mix(entityPixel, cubeLayer, cubeLayer.a);
}