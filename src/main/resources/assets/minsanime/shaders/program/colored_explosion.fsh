#version 330

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform sampler2D iChannel0;
uniform sampler2D iChannel1;

uniform ivec2 iResolution;
uniform vec2 OutSize;
uniform float iTime;
uniform float Scale;
uniform vec3 Color;

in vec2 texCoord;
in vec4 near_4;
in vec4 far_4;

out vec4 fragColor;

#define near 0.05
#define far 1000.0

const int MAX_STEPS = 100;
const float MAX_DIST = 200.0;
const float SURF_DIST = 0.001;

const int NUM_SAMPLES = 64;
const float DENSITY_MULTIPLIER = 1.0;

#define PI 3.14159265358
#define SAMPLE samplePos3D 
const vec3 up =  vec3(0.0, 1.0, 0.01);

float RADIUS = 0.;
float THICKNESS = 0.0;
float T = 0.0;
float ROTATION_T = 0.0;
float DECAY = .0;

float c, s;

float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * near * far) / (far + near - z * (far - near));
}

float sdSphere( vec3 p, float s )
{
  return length(p)-s;
}

float noise( in vec3 x )
{
    vec3 p = floor(x);
    vec3 f = fract(x);
	vec2 uv = (p.xy+vec2(37.0,17.0)*p.z) + f.xy;
	vec2 rg = texture(iChannel1, (uv+ 0.5)/256.0).yx;
	return mix( rg.x, rg.y, f.z );
}

float mapLo(in vec3 rp)
{
    return sdSphere(rp, RADIUS)*.005;
}

vec3 samplePos3D(in vec3 rp)
{
    vec3 fw = normalize(vec3(rp.x, 0.0, rp.z));
    vec3 pIn = fw * RADIUS;
    vec3 rt =  cross(fw, up);

    vec3 localP = rp-pIn;
    
    vec3 localP_rot = vec3(dot(fw, localP), dot(up, localP), dot(rt, localP));

    vec3 localP_rot_z = vec3(
        localP_rot.x * c - localP_rot.y * s,
        localP_rot.x * s + localP_rot.y * c,
        localP_rot.z
    );

    vec3 localP_final = fw * localP_rot_z.x + up * localP_rot_z.y + rt * localP_rot_z.z;

    return (localP_final + pIn);
}

vec2 sampleVolumeAndDist(in vec3 rp)
{
    float s = sdSphere(rp, RADIUS);

    float mapLo_dist = s * 0.005;

    vec3 v = cos(T*.15+rp*15.)+ sin(T*.25+rp*10.);
    float t_map = s - dot(v, v)*.005;
    float t_smooth = -smoothstep(0., -THICKNESS*.5, t_map);
    
    float d_noise = noise(SAMPLE(rp)*22.)*.8;
    d_noise += noise(SAMPLE(rp)*100.)*.4;
    
    float density = clamp(-t_smooth, 0.0, 1.0) * d_noise;

    return vec2(density, mapLo_dist);
}

vec3 heatToColor(float heat)
{
    vec3 col = mix(Color, vec3(1., .3, .0),clamp(heat * 15. -2.,   0., 1.));
    col = mix(col, vec3(1., 1., .6), clamp(heat * 15.1-4.,   0., 1.));
    col = mix(col, vec3(1., .9, .8), clamp(heat * 190. - 60.,   0., 1.));
    return col;
}

vec4 trace(vec3 ro, vec3 rd, float sceneDepth)
{
    vec4 col = vec4(0.0);
    float dO = 0.0;
    for(int i = 0; i < MAX_STEPS; i++) {
        if(dO > MAX_DIST || dO > sceneDepth) {
            break;
        }

        vec3 p = ro + rd * dO;
        float dS = mapLo(p);
        if(dS < SURF_DIST) {
            
            vec3 p_entry = p;
            
            float b = dot(p_entry, rd);
            float c = dot(p_entry, p_entry) - RADIUS*RADIUS;
            float disc = b*b - c;
            
            if (disc < 0.0) {
                break;
            }
            
            float t_exit = -b + sqrt(disc); 

            float d_remaining_scene = sceneDepth - dO;
            float d_remaining_max = MAX_DIST - dO;
            
            float pathLength = min(t_exit, min(d_remaining_scene, d_remaining_max));

            if (pathLength < 0.001) {
                break;
            }
            
            float stepSize = pathLength / float(NUM_SAMPLES);
            
            for (int j = 0; j < NUM_SAMPLES; ++j) {
                
                vec3 p_sample = p_entry + rd * (float(j) + 0.5) * stepSize;

                float currentWorldDist = length(p_sample - ro) * Scale;
                if (currentWorldDist > sceneDepth) {
                    break;
                }
                
                vec2 vol_data = sampleVolumeAndDist(p_sample);
                float density = vol_data.x;
                
                float heat = density;
                heat = (heat) / (max(1.0, (T * 0.5) - 0.1));
                vec3 dcol = heatToColor(heat);

                float smoke = heat / 0.03;
                if (smoke < 1.0) {
                    dcol = vec3(smoke * 0.5);
                }

                float d = density * (stepSize * Scale) * DENSITY_MULTIPLIER * DECAY;
                
                if (col.a >= 0.9) break;
                col.rgb = mix(col.rgb, dcol, (1.0 - col.a) * d);
                col.a += d;
            }
            break; 
        }

        dO += max(dS, 0.01);
    }

    col.rgb = smoothstep(0.0, 0.3, col.rgb);
    col.a = smoothstep(0.0, 0.95, col.a);
    return col;
}

float easeOut(float t, float b, float c, float d)
{
	t /= d;
	return -c * t*(t-2.) + b;    
}

void main()
{
    vec2 uv = (gl_FragCoord.xy-iResolution.xy*.5) / iResolution.x;
    vec4 col = texture(DiffuseSampler, texCoord);
    float depth = texture(DepthSampler, texCoord).r;
    float linearizedDepth = linearizeDepth(depth);
    
    T = mod(iTime, 4.5)*8.;
    ROTATION_T = pow(T*.2, 1.2);
    DECAY = 1.-smoothstep(7.0, 20., T);
    
    c = cos(-ROTATION_T);
    s = sin(-ROTATION_T);
    
    float expandTime = 2.;
    float ease = easeOut(min(T, expandTime), 0.01, 1.0, expandTime);
	float r = clamp(ease, 0.0, 1.0)*.1;    
    
    RADIUS = r*1.2;
    THICKNESS = r*2.;
    RADIUS += T*.02;

    vec3 ro = near_4.xyz / near_4.w;
    vec3 rd = normalize(far_4.xyz / far_4.w - ro);
    
    ro /= Scale;
   	
   	vec4 trace_result = trace(ro, rd, linearizedDepth);
    col = mix(col, trace_result, trace_result.a);
    
	fragColor = vec4(col.rgb, 1.0);
}