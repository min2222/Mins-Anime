package com.min01.minsanime.misc;

import com.min01.minsanime.obj.WavefrontObject;
import com.min01.minsanime.shader.AnimeEntityEffects;
import com.min01.minsanime.util.AnimeClientUtil;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class AnimeRenderType extends RenderType
{
	public static final RenderStateShard.OutputStateShard PIXEL_OUTPUT = new RenderStateShard.OutputStateShard("pixel_target", () -> 
    {
        RenderTarget target = AnimeEntityEffects.ENTITY_PIXEL.entityTarget;
        if(target != null) 
        {
            target.copyDepthFrom(AnimeClientUtil.MC.getMainRenderTarget());
            target.bindWrite(false);
        }
    }, 
    () ->
    {
        AnimeClientUtil.MC.getMainRenderTarget().bindWrite(false);
    });
    
	public AnimeRenderType(String p_173178_, VertexFormat p_173179_, Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_)
	{
		super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
	}
	
    public static RenderType entityPixel(ResourceLocation texture)
    {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(NO_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setOutputState(PIXEL_OUTPUT).createCompositeState(true);
        return create("entity_pixel", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, state);
    }
    
    public static RenderType objBlend(ResourceLocation texture)
    {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER).setOutputState(TRANSLUCENT_TARGET).setTextureState(new TextureStateShard(texture, false, false)).setTransparencyState(NO_TRANSPARENCY).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(true);
        return create("obj_blend", WavefrontObject.POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
    
    public static RenderType holopsicon(ResourceLocation texture)
    {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(LIGHTNING_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setLightmapState(LIGHTMAP).createCompositeState(false);
        return create("holopsicon", WavefrontObject.POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, true, rendertype$compositestate);
    }
    
    public static RenderType eyesFix(ResourceLocation texture) 
    {
    	RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(new TextureStateShard(texture, false, false)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).createCompositeState(false);
        return create("eyes_fix", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, state);
    }
    
    public static RenderType eyesNoAlpha(ResourceLocation texture) 
    {
        RenderStateShard.TextureStateShard state = new RenderStateShard.TextureStateShard(texture, false, false);
        return create("eyes_no_alpha", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(state).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    }
    
    public static RenderType additive(ResourceLocation texture)
    {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(LIGHTNING_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false);
        return create("additive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    }
}
