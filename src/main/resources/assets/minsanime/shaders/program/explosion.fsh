#version 330

// ------------------------------------
// U N I F O R M S
// ------------------------------------
uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform sampler2D iChannel0;
uniform sampler2D iChannel1;

uniform ivec2 iResolution;
uniform vec2 OutSize;
uniform float iTime;
uniform float Scale;

in vec2 texCoord;
in vec4 near_4;
in vec4 far_4;

out vec4 fragColor;

// ------------------------------------
// C O N S T A N T S & P R E - C A L C U L A T E D
// ------------------------------------
#define near 0.05
#define far 1000.0
#define NEAR_FAR_PRODUCT_2 (2.0 * near * far) // 100.0
#define NEAR_PLUS_FAR (far + near)           // 1000.05
#define FAR_MINUS_NEAR (far - near)          // 999.95

#define PI 3.14159265358
#define SAMPLE samplePos3D 
const vec3 up =  vec3(0.0, 1.0, 0.01);

float RADIUS = 0.;
float THICKNESS = 0.0;
float T = 0.0;
float ROTATION_T = 0.0;
float DECAY = .0;

// ------------------------------------
// D E P T H
// ------------------------------------
float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return NEAR_FAR_PRODUCT_2 / (NEAR_PLUS_FAR - z * FAR_MINUS_NEAR);
}

// ------------------------------------
// S D F
// ------------------------------------
float sdSphere( vec3 p, float s )
{
  return length(p)-s;
}

// ------------------------------------
// N O I S E (I Q)
// ------------------------------------
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
    return sdSphere(rp, RADIUS)-dot(v, v)*.005;
}

float mapLo(in vec3 rp)
{
    return sdSphere(rp, RADIUS)*.005;
}

// ------------------------------------
// R O T A T I O N & S A M P L E P O S
// ------------------------------------
vec3 samplePos3D(in vec3 rp)
{
    vec3 fw = normalize(vec3(rp.x, 0.0, rp.z));
    vec3 pIn = fw * RADIUS;
    vec3 rt =  cross(fw, up);

    vec3 localP = rp-pIn;
    
    mat3 rot; 
    rot[0] = fw; rot[1] = up; rot[2] = rt;
    
    localP = transpose(rot) * localP; 
    
    mat3 rotz; 
    rotz[0] = vec3(cos(-ROTATION_T), -sin(-ROTATION_T), 0.0); 
    rotz[1] = vec3(sin(-ROTATION_T), cos(-ROTATION_T), 0.0); 
    rotz[2] = vec3(0.0, 0.0, 1.0);
    
    localP = rotz * localP;
    localP = rot * localP;
    return (localP+pIn);
}

// ------------------------------------
// V O L U M E S A M P L I N G
// ------------------------------------
float sampleVolume(in vec3 rp)
{
    float t = map(rp);
    t = -smoothstep(0., -THICKNESS*.5, t);
    
    float d = noise(SAMPLE(rp)*22.)*.8;
    d += noise(SAMPLE(rp)*100.)*.4;
    d += noise(SAMPLE(rp)*100.)*.2;
    d += noise(SAMPLE(rp)*350.)*.45*d;
    
    float density = clamp(-t, 0.0, 1.0)*d;
    return density;
}

// ------------------------------------
// P A L E T T E
// ------------------------------------
vec3 heatToColor(float heat)
{
    vec3 col = mix(vec3(0.0), vec3(1., .3, .0),clamp(heat * 15. -2.,   0., 1.));
    col = mix(col, vec3(1., 1., .6), clamp(heat * 15.1-4.,   0., 1.));
    col = mix(col, vec3(1., .9, .8), clamp(heat * 190. - 60.,   0., 1.));
    return col;
}

// ------------------------------------
// T R A C E R
// ------------------------------------
vec4 trace(vec3 ro, vec3 rd, float sceneDepth)
{
	vec4 col = vec4(0.0);
    bool hit = false;
    vec3 rayOrigin = ro;
    float dist = 0.0;
    
    for (int i = 0; i < 150; ++i)
    {
        float currentWorldDist = length(ro - rayOrigin) * Scale;
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
			float currentWorldDist = length(ro - rayOrigin) * Scale;
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
                if (col.a >= 0.9) break; 
	            col.rgb = mix(col.rgb, dcol, (1.0-col.a)*d);
	            col.a += d;
	        }
	        if (dot(ro, ro) > 10.0) break;
	        if (ro.y < -0.2) break;
            
	        ro += rd*(.0012)*(1.0+max(dist*1000., 1.0)); 
	    }
    }
    col.rgb = smoothstep(0.0, .3, col.rgb);
    col.a = smoothstep(0.0, 0.95, col.a);
    return col;
}

// ------------------------------------
// E A S I N G
// ------------------------------------
float easeOut(float t, float b, float c, float d)
{
	t /= d;
	return -c * t*(t-2.) + b;    
}

// ------------------------------------
// M A I N
// ------------------------------------
void main()
{
    vec2 uv = (gl_FragCoord.xy-iResolution.xy*.5) / iResolution.x;
    vec4 col = texture(DiffuseSampler, texCoord);
    float depth = texture(DepthSampler, texCoord).r;
    float linearizedDepth = linearizeDepth(depth);
    
    T = mod(iTime, 4.5)*8.;
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
    
    ro /= Scale;
   	
   	vec4 trace_result = trace(ro, rd, linearizedDepth);
    col = mix(col, trace_result, trace_result.a);
    
	fragColor = vec4(col.rgb, 1.0);
}