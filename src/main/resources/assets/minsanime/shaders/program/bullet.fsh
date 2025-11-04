#version 330

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform sampler2D ImageSampler;
uniform vec2 OutSize;
uniform float iTime;

in vec2 texCoord;
in vec4 near_4;
in vec4 far_4;

out vec4 fragColor;

#define NEAR 0.05
#define FAR 1000.0
#define PI 3.14159265359
#define MAX_STEPS 100
#define MAX_DIST 100.0
#define SURF_DIST 0.001

const float BulletSize = 0.25;
const float FlameSpeed = 2.0;
const float LavaFlowSpeed = 0.5;

float sphereSDF(vec3 p, float r) {
    return length(p) - r;
}

float sceneSDF(vec3 p) {
    return sphereSDF(p, BulletSize);
}

vec3 getNormal(vec3 p) {
    const vec2 e = vec2(0.001, 0);
    float d = sceneSDF(p);
    vec3 n = d - vec3(
        sceneSDF(p - e.xyy),
        sceneSDF(p - e.yxy),
        sceneSDF(p - e.yyx)
    );
    return normalize(n);
}

float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * NEAR * FAR) / (FAR + NEAR - z * (FAR - NEAR));
}

vec3 coreEffect(vec3 p) {
    // Lava flow pattern
    float lava = sin(p.x * 4.0 + iTime * LavaFlowSpeed) *
                 cos(p.y * 3.0 + iTime * LavaFlowSpeed) *
                 sin(p.z * 2.0 + iTime * LavaFlowSpeed);

    float flame = texture(ImageSampler, vec2(iTime * FlameSpeed, 0.0)).r;

    // Core colors
    vec3 lavaColor = mix(vec3(1.0, 0.3, 0.0), vec3(0.8, 0.1, 0.0), lava);
    vec3 flameColor = mix(vec3(1.0, 0.5, 0.0), vec3(0.9, 0.1, 0.0), flame);

    return (lavaColor * 0.7 + flameColor * 0.3);
}

void main() {
    vec3 ro = near_4.xyz / near_4.w;
    vec3 far3 = far_4.xyz / far_4.w;
    vec3 rd = normalize(far3 - ro);

    vec3 col = texture(DiffuseSampler, texCoord).xyz;
    float depth = linearizeDepth(texture(DepthSampler, texCoord).r);

    float dO = 0.0;
    
    for(int i = 0; i < MAX_STEPS; i++) {
        if(dO > MAX_DIST || dO > depth) {
            break;
        }

        vec3 p = ro + rd * dO;
        float dS = sceneSDF(p);

        if(dS < SURF_DIST) {
            vec3 normal = getNormal(p);
            vec3 core = coreEffect(p);

            float ember = texture(ImageSampler, vec2(iTime * 5.0, 0.5)).g;
            vec3 emberColor = vec3(1.0, 0.4, 0.1) * ember;

            col = mix(col, core, 0.8);
            col += emberColor * 2.0;

            float edge = smoothstep(0.0, 1.0, 1.0 - dot(normal, -rd));
            col += edge * vec3(1.0, 0.3, 0.0) * 0.6;
            
            break; 
        }
        
        dO += dS;
    }

    fragColor = vec4(col, 1.0);
}