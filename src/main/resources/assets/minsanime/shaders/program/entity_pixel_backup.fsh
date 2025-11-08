#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D SceneSampler;

uniform vec2 iResolution;
uniform float iTime;

in vec2 texCoord;

out vec4 fragColor;

vec2 hash22(vec2 p) {
    p = fract(p * vec2(234.34, 567.21));
    p += dot(p, p + 45.32);
    return fract(p * vec2(0.321, 0.654));
}

void main() {
    vec4 backgroundPixel = texture(SceneSampler, texCoord);

    float animationPhase = sin(iTime * 1.5);
    float dissolveFactor = animationPhase * animationPhase;

    vec2 randomDirection = (hash22(texCoord) - 0.5) * 2.0;
    float maxOffset = 0.1;
    vec2 offset = randomDirection * dissolveFactor * maxOffset;

    vec2 lookupCoord = texCoord + offset;

    vec4 entityPixel = texture(DiffuseSampler, lookupCoord);

    fragColor = mix(backgroundPixel, entityPixel, entityPixel.a);
}