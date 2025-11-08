#version 330

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform vec2 OutSize;
uniform float iTime;

uniform float MaxScale;
uniform vec3 EndPos;
uniform vec2 Rotation;

in vec2 texCoord;
in vec4 near_4;
in vec4 far_4;

out vec4 fragColor;

#define NEAR 0.05
#define FAR 1000.0
#define MAX_STEPS 100
#define SURF_DIST 0.001
#define ANIMATION_DURATION 4.0

float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * NEAR * FAR) / (FAR + NEAR - z * (FAR - NEAR));
}

float sdCapsule( vec3 p, vec3 a, vec3 b, float r )
{
    vec3 pa = p - a, ba = b - a;
    float h = clamp( dot(pa,ba)/dot(ba,ba), 0.0, 1.0 );
    return length( pa - ba*h ) - r;
}

float map(vec3 p)
{
    float progress = mod(iTime, ANIMATION_DURATION) / ANIMATION_DURATION;

    float growPhase = smoothstep(0.0, 0.5, progress);
    float shrinkPhase = smoothstep(0.5, 1.0, progress);

    vec3 currentEndPos = mix(vec3(0.0), EndPos, growPhase);
    float currentRadius = mix(0.0, MaxScale, growPhase);

    currentRadius = mix(currentRadius, 0.0, shrinkPhase);

    float d = sdCapsule(p, vec3(0.0), currentEndPos, currentRadius);

    return d;
}

void main() {
    vec3 ro = near_4.xyz / near_4.w;
    vec3 far3 = far_4.xyz / far_4.w;
    vec3 rd = normalize(far3 - ro);

    vec3 col = texture(DiffuseSampler, texCoord).xyz;
    float sceneDepth = linearizeDepth(texture(DepthSampler, texCoord).r);

    float t = 0.0;
    bool hit = false;
    float min_dist = FAR;

    for (int i = 0; i < MAX_STEPS; i++)
    {
        vec3 p = ro + rd * t;
        float d = map(p);
        min_dist = min(min_dist, d);
        if (d < SURF_DIST)
        {
            hit = true;
            break;
        }
        t += d;
        if (t > sceneDepth || t > FAR)
        {
            break;
        }
    }

    vec3 laserColor = vec3(0.7, 1.0, 1.0);

    if (hit)
    {
        col = laserColor;
    }

    float glowFactor = 0.15 * exp(-min_dist * 5.0);
    col += laserColor * glowFactor;

    fragColor = vec4(col, 1.0);
}