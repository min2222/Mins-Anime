#version 330

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform sampler3D ImageSampler;

uniform ivec2 iResolution;
uniform float iTime;
uniform float Scale;
uniform vec3 Color;

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

// Hazel Quantock 2017
// License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
// https://creativecommons.org/licenses/by-nc-sa/3.0/

float g_blastTime;
vec3 g_cloudCentre;
void InitBlastParams()
{
    g_blastTime = fract(iTime/20.);
    g_cloudCentre = vec3(0,g_blastTime*5.,0);
}

vec3 Flow( vec3 pos )
{
    // make a toroidal roll, like a mushroom cloud
    vec3 p = pos - g_cloudCentre;
    vec3 v;
    v.xz = -normalize(p.xz)*p.y;
    v.y = length(p.xz)-.8;
    //v *= smoothstep(.0,.5,length(pos.xz)); bad: this squashes/stretches it
    v *= .1;
    
    // reduce velocity with distance from cloud top edge
    float g = length(vec2(p.y,length(p.xz)-.8))-1.; // this doesn't match the one in SDF, but it looks better with the mismatch
    v *= exp2(-pow(g*3.,2.));
    
    return v;
}

float SDF( vec3 pos )
{
    // multi fractal
    const float period = 1.6;
    float tt = fract(iTime/period);
    float t[2] = float[2]( tt*period, (tt-1.)*period );
    vec3 uvw = (pos-g_cloudCentre)/30.;
    float f[2] = float[2]( .0, .0 );

    for ( int i=0; i < 2; i++ )
    {
	    vec3 offset = Flow(pos)*t[i];
        vec3 u = uvw + offset*.2; offset *= .2;
        f[i] += texture(ImageSampler, offset+u*2.).x/2.;
        f[i] += texture(ImageSampler, offset+u*4.).x/4.;
        f[i] += texture(ImageSampler, offset+u*8.).x/8.;
        f[i] += texture(ImageSampler, offset+u*16.).x/16.;
        f[i] += texture(ImageSampler, offset+u*32.).x/32.;
    }
    
    float ff = mix( f[0], f[1], tt );
    ff *= .5;

    vec3 p = pos - g_cloudCentre;
    float bulge = 1.-exp2(-20.*g_blastTime);
    float g = length(vec2(p.y,length(p.xz)-1.*bulge))-1.;
    ff *= bulge;
    
    float h = length(pos.xz)-.7+.2*(g_cloudCentre.y-pos.y-1.2);
    h = max(h, pos.y-g_cloudCentre.y);
    h = max(h,(g_cloudCentre.y*1.25-4.-pos.y)*.3);
    
    g = min(g,h);
    ff += g*.6;
    
    return ff;
}


void main() {
    vec3 ro = near_4.xyz / near_4.w;
    vec3 rd = normalize(far_4.xyz / far_4.w - ro);
    
    vec4 originalCol = texture(DiffuseSampler, texCoord);
    
    float depth = linearizeDepth(texture(DepthSampler, texCoord).r);
	
	InitBlastParams();
    
    vec3 ray = rd;
    vec3 pos = ro / Scale;
    
    float softness = .1+pow(g_blastTime,2.)*.5;
    float density = 1.2/softness;
    
    const float epsilon = .001;
    float visibility = 1.0;
    float light0 = 0.5;
    float light1 = 0.5;
    vec3 sunDir = normalize(vec3(1));

    for ( int i=0; i < 20; i++ )
    {
        float h = SDF(pos);
        float vis = smoothstep(epsilon,softness,h);
        float currentWorldDist = length(pos * Scale - ro);
        if(currentWorldDist > depth) {
        	break;
        }
        if ( pos.y < .0 ) vis = 1.;
        h = max(h,epsilon);
        if ( vis < 1. )
        {
            float newvis = visibility * pow(vis,h*density);
	    	light0 += (visibility - newvis) * smoothstep( -.5, 1., (SDF(pos+sunDir*softness) - h)/softness );
            vec3 lightDelta = g_cloudCentre-pos;
	    	light1 += (visibility - newvis) * pow(smoothstep( -1., 1., (SDF(pos+normalize(lightDelta)*softness) - h)/softness ),2.) / (dot(lightDelta,lightDelta)+1.);
            visibility = newvis;
        }
        
        if ( vis <= 0.)
            break;
        pos += h*ray;
    }

    vec4 cloudCol = vec4(0.);
    cloudCol += light0*vec4(Color, 0);
	cloudCol *= pow(g_blastTime,.5)*.5;
    cloudCol += light1*vec4(Color, 0)/(25.*pow(g_blastTime,2.));
    
    cloudCol = pow(cloudCol,vec4(1./2.2));
    
    fragColor = mix(cloudCol, originalCol, visibility);
    fragColor.a = originalCol.a;
}