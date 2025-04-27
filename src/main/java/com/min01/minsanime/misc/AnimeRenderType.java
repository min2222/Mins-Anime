package com.min01.minsanime.misc;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.obj.WavefrontObject;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

public class AnimeRenderType extends RenderType
{
    public static ShaderInstance holopsiconShader;

    private static final ShaderStateShard HOLOPSICON_SHADER = new ShaderStateShard(() -> holopsiconShader);
    
	public AnimeRenderType(String p_173178_, VertexFormat p_173179_, Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_)
	{
		super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
	}
	
    public static List<Pair<ShaderInstance, Consumer<ShaderInstance>>> registerShaders(ResourceProvider resourceManager)
    {
		try 
		{
			return List.of(Pair.of(new ShaderInstance(resourceManager, new ResourceLocation(MinsAnime.MODID, "holopsicon"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), shaderInstance -> 
			{
				holopsiconShader = shaderInstance;
			}));
		}
		catch (IOException e) 
		{
			throw new RuntimeException(e);
		}
    }
    
    public static RenderType objBlend(ResourceLocation texture)
    {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER).setOutputState(TRANSLUCENT_TARGET).setTextureState(new TextureStateShard(texture, false, false)).setTransparencyState(NO_TRANSPARENCY).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(true);
        return create("obj_blend", WavefrontObject.POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
    
    public static RenderType holopsicon(ResourceLocation texture) 
    {
        RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(HOLOPSICON_SHADER).setOutputState(TRANSLUCENT_TARGET).setTextureState(new TextureStateShard(texture, true, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setLightmapState(LIGHTMAP).setWriteMaskState(COLOR_WRITE).createCompositeState(true);
        return RenderType.create("holopsicon", WavefrontObject.POSITION_TEX_LMAP_COL_NORMAL, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    }
    
    public static RenderType eyesFix(ResourceLocation texture) 
    {
    	RenderType.CompositeState state = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(new TextureStateShard(texture, false, false)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).createCompositeState(false);
        return create("eyes_fix", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, state);
    }
}
