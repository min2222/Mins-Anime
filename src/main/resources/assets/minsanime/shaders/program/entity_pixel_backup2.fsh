#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D SceneSampler;
uniform float iTime;

uniform vec2 iResolution;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    float chunkSize = 0.04;

    float animationSpeed = 0.4;

    float transitionHeight = 0.15;

    float maxOffset = 0.1;

    float progress = fract(iTime * animationSpeed);
    float scanLineY = progress * (1.0 + transitionHeight) - transitionHeight;

    vec2 chunkCoord = floor(texCoord / chunkSize) * chunkSize;

    float dist = scanLineY - chunkCoord.y;

    float localProgress = smoothstep(0.0, transitionHeight, dist);

    vec2 offset = vec2(0.0, -localProgress * maxOffset);
    float alphaMultiplier = 1.0 - localProgress;

    vec2 lookupCoord = texCoord + offset;

    vec4 backgroundPixel = texture(SceneSampler, texCoord);
    vec4 entityPixel = texture(DiffuseSampler, lookupCoord);

    entityPixel.a *= alphaMultiplier;

    fragColor = mix(backgroundPixel, entityPixel, entityPixel.a);
}