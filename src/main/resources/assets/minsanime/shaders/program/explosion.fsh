#version 330

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform sampler2D iChannel0;
uniform sampler2D iChannel1;

uniform ivec2 iResolution;
uniform vec2 OutSize;
uniform float iTime;

in vec2 texCoord;
in vec4 near_4;
in vec4 far_4;

out vec4 fragColor;

#define near 0.05
#define far 1000.0
float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (2.0 * near * far) / (far + near - z * (far - near));
}

mat3 rotx(float a) { mat3 rot; rot[0] = vec3(1.0, 0.0, 0.0); rot[1] = vec3(0.0, cos(a), -sin(a)); rot[2] = vec3(0.0, sin(a), cos(a)); return rot; }
mat3 roty(float a) { mat3 rot; rot[0] = vec3(cos(a), 0.0, sin(a)); rot[1] = vec3(0.0, 1.0, 0.0); rot[2] = vec3(-sin(a), 0.0, cos(a)); return rot; }
mat3 rotz(float a) { mat3 rot; rot[0] = vec3(cos(a), -sin(a), 0.0); rot[1] = vec3(sin(a), cos(a), 0.0); rot[2] = vec3(0.0, 0.0, 1.0); return rot; }

#define PI 3.14159265358
#define SAMPLE samplePos3D 
const vec3 up =  vec3(0.0, 1.0, 0.01);

float RADIUS = 0.;
float THICKNESS = 0.0;
float T = 0.0;
float ROTATION_T = 0.0;
float DECAY = .0;
float SCALE = 50.0;

// https://iquilezles.org/articles/distfunctions
float sdTorus( vec3 p, vec2 t )
{
  vec2 q = vec2(length(p.xz)-t.x,p.y);
  return length(q)-t.y;
}

float sdSphere( vec3 p, float s )
{
  return length(p)-s;
}

// from IQ, various places where 3d noise is used.
// Without smoothing, in hope to gain a bit of performance.
float noise( in vec3 x )
{
    vec3 p = floor(x);
    vec3 f = fract(x);
	vec2 uv = (p.xy+vec2(37.0,17.0)*p.z) + f.xy;
	vec2 rg = texture(iChannel1, (uv+ 0.5)/256.0).yx;
	return mix( rg.x, rg.y, f.z );
}

float map(in vec3 rp)
{
    vec3 v = cos(T*.15+rp*15.)+ sin(T*.25+rp*10.);
    return sdTorus(rp, vec2(RADIUS, THICKNESS))-dot(v, v)*.005;
}

float mapLo(in vec3 rp)
{
    return sdTorus(rp, vec2(RADIUS, THICKNESS))*.005;
}

/////
// rotates the volume inside the torus, to get the smoke rolling effect.
/////
mat3 rot;
vec3 samplePos3D(in vec3 rp)
{
    vec3 fw = normalize(vec3(rp.x, 0.0, rp.z));
    vec3 pIn = fw * RADIUS;
    vec3 rt =  cross(fw, up);

    vec3 localP = rp-pIn;
    rot[0] = fw; rot[1] = up; rot[2] = rt;
    localP = transpose(rot) * localP; 
    localP = rotz(-ROTATION_T) * localP;
    localP = rot * localP;
    return (localP+pIn);
}

// actual volume sampling
float sampleVolume(in vec3 rp)
{
    float t = map(rp);
    t = -smoothstep(0., -THICKNESS*.5, t);
    float d = noise(SAMPLE(rp)*22.)*.8;
    d += noise(SAMPLE(rp)*70.)*.4;
    d += noise(SAMPLE(rp)*100.)*.2;
    d += noise(SAMPLE(rp)*350.)*.45*d;
    float density = clamp(-t, 0.0, 1.0)*d;
    return density;
}

// Palette for the effect
vec3 heatToColor(float heat)
{
    vec3 col = mix(vec3(0.0), vec3(1., .3, .0),clamp(heat * 15. -2.,   0., 1.));
    col = mix(col, vec3(1., 1., .6), clamp(heat * 15.1-4.,   0., 1.));
    col = mix(col, vec3(1., .9, .8), clamp(heat * 190. - 60.,   0., 1.));
    return col;
}

vec4 trace(vec3 ro, vec3 rd, float sceneDepth)
{
	vec4 col = vec4(0.0);
    bool hit = false;
    vec3 rayOrigin = ro;
    float dist = 0.0;
    
    for (int i = 0; i < 150; ++i)
    {
        float currentWorldDist = length(ro - rayOrigin) * SCALE;
        if (currentWorldDist > sceneDepth)
        {
            hit = false;
            break;
        }
        
        dist = mapLo(ro);
        if(dist < 0.0)
        {
            hit = true;
            break;
        }
        ro += rd * max(dist, 0.01);
        if(length(rayOrigin - ro) > 5.0) break;
    }
    
   	if(hit)
    {
	    for (int i = 0; i < 200; ++i)
	    {
			float currentWorldDist = length(ro - rayOrigin) * SCALE;
            if (currentWorldDist > sceneDepth)
            {
                break;
            }
            
	        float density = sampleVolume(ro);
			float dist = mapLo(ro);
	
	        if (dist < 0.0)
	        {
	            float heat = density;
	            heat = (heat)/(max(1.0, (T*.5)-.1));
	            vec3 dcol = heatToColor(heat);
	
	            float smoke = heat/.03;
	            if (smoke < 1.0)
	            {
	                dcol = vec3(smoke*.5);
	            }
	
	            float d = density * 0.024 * DECAY;
	            col.rgb = mix(col.rgb, dcol, (1.0-col.a)*d);
	            col.a += d;
	        }
	        if (dot(ro, ro) > 10.0) break;
	        if (ro.y < -0.2) break;
	        if (col.a >= 1.) break;
	        ro += rd*(.00075)*(1.0+max(dist*1000., 1.0));
	    }
    }
    col.rgb = smoothstep(0.0, .3, col.rgb);
    col.a = smoothstep(0.0, 0.95, col.a);
    return col;
}

// t = time, b = from, c = delta, d = duration
// http://gizma.com/easing
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
    
    T = mod(iTime, 4.5)*4.;
    ROTATION_T = pow(T*.2, 1.2);
    DECAY = 1.-smoothstep(7.0, 20., T);
    
    float expandTime = 2.;
    float ease = easeOut(min(T, expandTime), 0.01, 1.0, expandTime);
	float r = clamp(ease, 0.0, 1.0)*.1;    
    
    RADIUS = r*1.2;
    THICKNESS = r*2.;
    RADIUS += T*.02;

    vec3 ro = near_4.xyz / near_4.w;
    vec3 rd = normalize(far_4.xyz / far_4.w - ro);
    
    ro /= SCALE;
   	
   	vec4 trace = trace(ro, rd, linearizedDepth);
    col = mix(col, trace, trace.a);
    
	fragColor = vec4(col.rgb, 1.0);
}