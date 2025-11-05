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

uniform vec3 LightColor = vec3(1.0, 0.5, 0.2);
uniform float LightIntensity = 2.0;
uniform float LightRadius = 4.0;
const vec3 LightPos = vec3(0.0, 0.0, 0.0);

float sphereSDF(vec3 p, float r) {
    return length(p - LightPos) - r;
}

float sceneSDF(vec3 p) {
    return sphereSDF(p, 0.0);
}

float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * NEAR * FAR) / (FAR + NEAR - z * (FAR - NEAR));
}

void main() {
    vec3 ro = near_4.xyz / near_4.w;
    vec3 far3 = far_4.xyz / far_4.w;
    vec3 rd = normalize(far3 - ro);

    vec3 col = texture(DiffuseSampler, texCoord).xyz;
    float sceneDepth = linearizeDepth(texture(DepthSampler, texCoord).r);

    float dO = 0.0;
    bool hit = false;

    for(int i = 0; i < MAX_STEPS; i++) {
        if(dO > MAX_DIST || dO > sceneDepth) {
            break;
        }
        vec3 p = ro + rd * dO;
        float dS = sceneSDF(p);
        if(dS < SURF_DIST) {
            hit = true;
            break; 
        }
        dO += dS;
    }

    if (!hit) {
        vec3 vecToLight = LightPos - ro;
        float tca = dot(vecToLight, rd);
        
        if (tca >= 0.0) {
            float d2 = dot(vecToLight, vecToLight) - tca * tca;
            float lightRadiusSq = LightRadius * LightRadius;

            if (d2 <= lightRadiusSq) {
                vec3 worldPos = ro + rd * sceneDepth;
                float distToLight = length(worldPos - LightPos);
                float attenuation = 1.0 - smoothstep(0.0, LightRadius, distToLight);
                attenuation *= attenuation;
                vec3 lighting = LightColor * LightIntensity * attenuation;
                col += lighting;
            }
        }
    }

    fragColor = vec4(col, 1.0);
}